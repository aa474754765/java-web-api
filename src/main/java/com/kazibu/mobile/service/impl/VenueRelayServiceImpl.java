package com.kazibu.mobile.service.impl;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.repository.UserRepository;
import com.kazibu.mobile.dto.VenueRelayDto;
import com.kazibu.mobile.service.VenueRelayService;
import com.kazibu.sports.entity.Venue;
import com.kazibu.sports.entity.VenueRelay;
import com.kazibu.sports.entity.VenueRelayParticipant;
import com.kazibu.sports.repository.VenueRelayParticipantRepository;
import com.kazibu.sports.repository.VenueRelayRepository;
import com.kazibu.sports.repository.VenueRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("mobileVenueRelayService")
public class VenueRelayServiceImpl implements VenueRelayService {
  @Autowired
  private VenueRelayRepository venueRelayRepository;

  @Autowired
  private VenueRelayParticipantRepository participantRepository;

  @Autowired
  private VenueRepository venueRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public Long createRelay(VenueRelayDto.CreateRequest request) {
    validateCreateRequest(request);

    User currentUser = getCurrentUser();
    Venue venue = venueRepository.findById(request.getVenueId())
        .orElseThrow(() -> new IllegalArgumentException("场馆不存在"));

    VenueRelay relay = new VenueRelay();
    relay.setVenue(venue);
    relay.setStartDate(request.getStartDate());
    relay.setStartTime(request.getStartTime());
    relay.setEndTime(request.getEndTime());
    relay.setMaxPeople(request.getMaxPeople());
    relay.setContactInfo(safeTrim(request.getContactInfo()));
    relay.setStatus("1");
    relay.setIsPublic((request.getIsPublic() == null || request.getIsPublic().trim().isEmpty()) ? "1" : request.getIsPublic().trim());
    relay.setAvgCost(request.getAvgCost());
    relay.setRemark(safeTrim(request.getRemark()));
    relay.setVenueImage(safeTrim(request.getVenueImage()));
    relay.setSkillLevel(safeTrim(request.getSkillLevel()));
    relay.setCreatorUserId(currentUser.getId());
    relay.setCreatorUsername(currentUser.getUsername());
    relay.setJoinedPeople(1);

    VenueRelay savedRelay = venueRelayRepository.save(relay);
    VenueRelayParticipant creatorParticipant = new VenueRelayParticipant();
    creatorParticipant.setRelay(savedRelay);
    creatorParticipant.setUserId(currentUser.getId());
    creatorParticipant.setUserName(currentUser.getUsername());
    creatorParticipant.setUserAvatar(safeTrim(currentUser.getWxAvatarUrl()));
    creatorParticipant.setContactInfo(safeTrim(request.getContactInfo()));
    creatorParticipant.setStatus("1");
    creatorParticipant.setJoinTime(LocalDateTime.now());
    creatorParticipant.setCancelTime(null);
    participantRepository.save(creatorParticipant);
    return savedRelay.getId();
  }

  @Override
  @Transactional
  public void updateRelay(VenueRelayDto.EditRequest request) {
    validateEditRequest(request);
    User currentUser = getCurrentUser();
    VenueRelay relay = venueRelayRepository.findById(request.getRelayId())
        .orElseThrow(() -> new IllegalArgumentException("接龙不存在"));
    if (!currentUser.getId().equals(relay.getCreatorUserId())) {
      throw new IllegalArgumentException("仅发起人可编辑接龙");
    }
    Venue venue = venueRepository.findById(request.getVenueId())
        .orElseThrow(() -> new IllegalArgumentException("场馆不存在"));
    relay.setVenue(venue);
    relay.setStartDate(request.getStartDate());
    relay.setStartTime(request.getStartTime());
    relay.setEndTime(request.getEndTime());
    relay.setMaxPeople(request.getMaxPeople());
    relay.setContactInfo(safeTrim(request.getContactInfo()));
    relay.setIsPublic((request.getIsPublic() == null || request.getIsPublic().trim().isEmpty()) ? "1" : request.getIsPublic().trim());
    relay.setAvgCost(request.getAvgCost());
    relay.setRemark(safeTrim(request.getRemark()));
    relay.setVenueImage(safeTrim(request.getVenueImage()));
    relay.setSkillLevel(safeTrim(request.getSkillLevel()));
    venueRelayRepository.save(relay);
  }

  @Override
  @Transactional
  public void deleteRelay(VenueRelayDto.CancelRequest request) {
    if (request == null || request.getRelayId() == null) {
      throw new IllegalArgumentException("接龙ID不能为空");
    }
    User currentUser = getCurrentUser();
    VenueRelay relay = venueRelayRepository.findById(request.getRelayId())
        .orElseThrow(() -> new IllegalArgumentException("接龙不存在"));
    if (!currentUser.getId().equals(relay.getCreatorUserId())) {
      throw new IllegalArgumentException("仅发起人可删除接龙");
    }
    venueRelayRepository.delete(relay);
  }

  @Override
  @Transactional
  public VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> queryRelayList(VenueRelayDto.RelayPageRequest request) {
    refreshExpiredRelays();
    Long currentUserId = getCurrentUserIdIfLoggedIn();
    Integer tabType = request != null ? request.getTabType() : null;
    boolean requireLogin = Boolean.TRUE.equals(request != null ? request.getSelfOnly() : null)
        || (tabType != null && (tabType == 1 || tabType == 2));
    if (requireLogin && currentUserId == null) {
      VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> emptyResponse = new VenueRelayDto.PageResponse<>();
      emptyResponse.setList(new ArrayList<>());
      emptyResponse.setPage((request != null && request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0);
      int reqSize = (request != null && request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
      emptyResponse.setSize(Math.min(reqSize, 20));
      emptyResponse.setTotal(0L);
      emptyResponse.setTotalPages(0);
      return emptyResponse;
    }
    List<Long> joinedRelayIds = currentUserId == null ? new ArrayList<>()
        : participantRepository.findAllByUserIdAndStatus(currentUserId, "1")
            .stream()
            .map(p -> p.getRelay() != null ? p.getRelay().getId() : null)
            .filter(id -> id != null)
            .collect(Collectors.toList());

    int page = (request != null && request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
    int size = (request != null && request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
    size = Math.min(size, 20);

    Specification<VenueRelay> spec = (root, query, cb) -> {
      if (query != null && VenueRelay.class.equals(query.getResultType())) {
        var venueFetch = root.fetch("venue", JoinType.LEFT);
        venueFetch.fetch("sportsType", JoinType.LEFT);
        query.distinct(true);
      }
      List<Predicate> predicates = new ArrayList<>();
      if (request != null) {
        if (request.getVenueId() != null) {
          predicates.add(cb.equal(root.get("venue").get("id"), request.getVenueId()));
        }
        if (request.getStartDate() != null) {
          predicates.add(cb.equal(root.get("startDate"), request.getStartDate()));
        }
        if (request.getStatus() != null && !request.getStatus().trim().isEmpty()) {
          predicates.add(cb.equal(root.get("status"), request.getStatus().trim()));
        }
        if (request.getIsPublic() != null && !request.getIsPublic().trim().isEmpty()) {
          predicates.add(cb.equal(root.get("isPublic"), request.getIsPublic().trim()));
        }
        if (request.getSkillLevel() != null && !request.getSkillLevel().trim().isEmpty()) {
          predicates.add(cb.equal(root.get("skillLevel"), request.getSkillLevel().trim()));
        }
        if (tabType != null) {
          if (tabType == 1) {
            predicates.add(cb.equal(root.get("creatorUserId"), currentUserId));
          } else if (tabType == 2) {
            if (joinedRelayIds.isEmpty()) {
              predicates.add(cb.disjunction());
            } else {
              predicates.add(root.get("id").in(joinedRelayIds));
            }
            predicates.add(cb.notEqual(root.get("creatorUserId"), currentUserId));
          }
        }
        if (Boolean.TRUE.equals(request.getSelfOnly())) {
          predicates.add(cb.equal(root.get("creatorUserId"), currentUserId));
        }
      }
      boolean allTab = tabType == null || tabType == 3;
      boolean selfQuery = Boolean.TRUE.equals(request != null ? request.getSelfOnly() : null)
          || (tabType != null && (tabType == 1 || tabType == 2));
      if (allTab && !selfQuery) {
        predicates.add(cb.equal(root.get("isPublic"), "1"));
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };

    List<VenueRelay> filteredRelays = venueRelayRepository.findAll(spec);
    Comparator<VenueRelay> relayComparator = Comparator
        .comparingInt(this::statusRank)
        .thenComparingLong(this::startDistanceToNowSeconds)
        .thenComparing(VenueRelay::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder()));
    filteredRelays.sort(relayComparator);

    int fromIndex = Math.min(page * size, filteredRelays.size());
    int toIndex = Math.min(fromIndex + size, filteredRelays.size());
    List<VenueRelay> pageContent = filteredRelays.subList(fromIndex, toIndex);

    List<VenueRelayDto.RelayListItem> list = pageContent.stream()
        .map(relay -> toListItem(relay, currentUserId))
        .collect(Collectors.toList());

    VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> response = new VenueRelayDto.PageResponse<>();
    response.setList(list);
    response.setPage(page);
    response.setSize(size);
    long total = filteredRelays.size();
    response.setTotal(total);
    response.setTotalPages((int) Math.ceil((double) total / size));
    return response;
  }

  private int statusRank(VenueRelay relay) {
    String status = relay.getStatus();
    if ("1".equals(status)) {
      return 1;
    }
    if ("2".equals(status)) {
      return 2;
    }
    if ("3".equals(status)) {
      return 3;
    }
    return 99;
  }

  private long startDistanceToNowSeconds(VenueRelay relay) {
    if (relay.getStartDate() == null || relay.getStartTime() == null) {
      return Long.MAX_VALUE;
    }
    LocalDateTime startDateTime = LocalDateTime.of(relay.getStartDate(), relay.getStartTime());
    return Math.abs(java.time.Duration.between(LocalDateTime.now(), startDateTime).getSeconds());
  }

  @Override
  @Transactional
  public void joinRelay(VenueRelayDto.JoinRequest request) {
    refreshExpiredRelays();

    if (request == null || request.getRelayId() == null) {
      throw new IllegalArgumentException("接龙ID不能为空");
    }

    User currentUser = getCurrentUser();
    VenueRelay relay = venueRelayRepository.findById(request.getRelayId())
        .orElseThrow(() -> new IllegalArgumentException("接龙不存在"));
    if (!"1".equals(relay.getStatus())) {
      throw new IllegalArgumentException("接龙状态不可参与");
    }

    Optional<VenueRelayParticipant> optionalParticipant = participantRepository.findByRelay_IdAndUserId(
        request.getRelayId(), currentUser.getId());
    if (optionalParticipant.isPresent() && "1".equals(optionalParticipant.get().getStatus())) {
      throw new IllegalArgumentException("您已参与该接龙");
    }

    int affected = venueRelayRepository.incrementJoinedPeopleIfAvailable(request.getRelayId());
    if (affected <= 0) {
      throw new IllegalArgumentException("报名人数已满或接龙已关闭");
    }

    VenueRelayParticipant participant = optionalParticipant.orElseGet(VenueRelayParticipant::new);
    participant.setRelay(relay);
    participant.setUserId(currentUser.getId());
    participant.setUserName(currentUser.getUsername());
    participant.setUserAvatar(safeTrim(currentUser.getWxAvatarUrl()));
    participant.setContactInfo(safeTrim(request.getContactInfo()));
    participant.setStatus("1");
    participant.setJoinTime(LocalDateTime.now());
    participant.setCancelTime(null);
    participantRepository.save(participant);
  }

  @Override
  @Transactional
  public void cancelJoin(VenueRelayDto.CancelRequest request) {
    refreshExpiredRelays();

    if (request == null || request.getRelayId() == null) {
      throw new IllegalArgumentException("接龙ID不能为空");
    }
    User currentUser = getCurrentUser();

    VenueRelayParticipant participant = participantRepository.findByRelay_IdAndUserId(
        request.getRelayId(), currentUser.getId()).orElse(null);
    if (participant == null || !"1".equals(participant.getStatus())) {
      throw new IllegalArgumentException("您未参与该接龙，无权取消");
    }

    participant.setStatus("0");
    participant.setCancelTime(LocalDateTime.now());
    participantRepository.save(participant);
    venueRelayRepository.decrementJoinedPeople(request.getRelayId());
  }

  private VenueRelayDto.RelayListItem toListItem(VenueRelay relay, Long currentUserId) {
    VenueRelayDto.RelayListItem item = new VenueRelayDto.RelayListItem();
    item.setId(relay.getId());
    item.setVenueInfo(buildVenueInfo(relay.getVenue()));
    item.setStartDate(relay.getStartDate());
    item.setStartTime(relay.getStartTime());
    item.setEndTime(relay.getEndTime());
    item.setMaxPeople(relay.getMaxPeople());
    item.setJoinedPeople(relay.getJoinedPeople());
    item.setContactInfo(relay.getContactInfo());
    item.setStatus(relay.getStatus());
    item.setIsPublic(relay.getIsPublic());
    item.setAvgCost(relay.getAvgCost());
    item.setRemark(relay.getRemark());
    item.setVenueImage(relay.getVenueImage());
    item.setSkillLevel(relay.getSkillLevel());
    item.setCreatorUserId(relay.getCreatorUserId());
    item.setCreatorUsername(relay.getCreatorUsername());
    item.setCreateTime(relay.getCreateTime());
    item.setJoinedByCurrentUser(currentUserId != null
        && participantRepository.existsByRelay_IdAndUserIdAndStatus(relay.getId(), currentUserId, "1"));
    List<VenueRelayParticipant> activeParticipants = participantRepository
        .findAllByRelay_IdAndStatusOrderByJoinTimeAsc(relay.getId(), "1");
    List<VenueRelayDto.RelayListItem.ParticipantInfo> participantInfos = activeParticipants
        .stream()
        .map(this::toParticipantInfo)
        .collect(Collectors.toList());
    item.setParticipants(participantInfos);
    return item;
  }

  private VenueRelayDto.RelayListItem.ParticipantInfo toParticipantInfo(VenueRelayParticipant participant) {
    VenueRelayDto.RelayListItem.ParticipantInfo info = new VenueRelayDto.RelayListItem.ParticipantInfo();
    info.setUserName(participant.getUserName());
    info.setUserAvatar(participant.getUserAvatar());
    return info;
  }

  private VenueRelayDto.VenueInfo buildVenueInfo(Venue venue) {
    if (venue == null) {
      return null;
    }
    VenueRelayDto.VenueInfo info = new VenueRelayDto.VenueInfo();
    info.setId(venue.getId());
    info.setName(venue.getName());
    info.setDescription(venue.getDescription());
    if (venue.getSportsType() != null) {
      info.setSportsTypeId(venue.getSportsType().getId());
      info.setSportsTypeName(venue.getSportsType().getType());
    }
    info.setProvince(venue.getProvince());
    info.setCity(venue.getCity());
    info.setDistrict(venue.getDistrict());
    info.setAddress(venue.getAddress());
    info.setLatitude(venue.getLatitude());
    info.setLongitude(venue.getLongitude());
    info.setContactType(venue.getContactType());
    info.setContactInfo(venue.getContactInfo());
    info.setRating(venue.getRating());
    return info;
  }

  private void refreshExpiredRelays() {
    venueRelayRepository.closeExpiredRelays();
  }

  private void validateCreateRequest(VenueRelayDto.CreateRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("请求参数不能为空");
    }
    if (request.getVenueId() == null) {
      throw new IllegalArgumentException("场馆ID不能为空");
    }
    if (request.getStartDate() == null) {
      throw new IllegalArgumentException("开始日期不能为空");
    }
    if (request.getStartTime() == null || request.getEndTime() == null) {
      throw new IllegalArgumentException("开始时间和结束时间不能为空");
    }
    if (!request.getEndTime().isAfter(request.getStartTime())) {
      throw new IllegalArgumentException("结束时间必须晚于开始时间");
    }
    if (request.getMaxPeople() == null || request.getMaxPeople() <= 0) {
      throw new IllegalArgumentException("报名人数上限必须大于0");
    }
    if (request.getRemark() != null && request.getRemark().length() > 1000) {
      throw new IllegalArgumentException("备注长度不能超过1000");
    }
    if (request.getVenueImage() != null && request.getVenueImage().trim().length() > 1000) {
      throw new IllegalArgumentException("场馆图片长度不能超过1000");
    }
    if (request.getSkillLevel() == null || request.getSkillLevel().trim().isEmpty()) {
      throw new IllegalArgumentException("水平等级不能为空");
    }
    if (request.getSkillLevel().trim().length() > 50) {
      throw new IllegalArgumentException("水平等级长度不能超过50");
    }
  }

  private void validateEditRequest(VenueRelayDto.EditRequest request) {
    if (request == null || request.getRelayId() == null) {
      throw new IllegalArgumentException("接龙ID不能为空");
    }
    validateCreateRequest(request);
  }

  private String safeTrim(String value) {
    return value == null ? null : value.trim();
  }

  private User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getName())) {
      throw new IllegalArgumentException("请先登录");
    }
    String username = authentication.getName();
    return userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("当前用户不存在"));
  }

  private Long getCurrentUserIdIfLoggedIn() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || !authentication.isAuthenticated()
          || "anonymousUser".equals(authentication.getName())) {
        return null;
      }
      String username = authentication.getName();
      return userRepository.findByUsername(username).map(User::getId).orElse(null);
    } catch (Exception ignored) {
      return null;
    }
  }
}
