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
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
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
    relay.setCourtName(request.getCourtName().trim());
    relay.setMaxPeople(request.getMaxPeople());
    relay.setJoinedPeople(0);
    relay.setContactInfo(safeTrim(request.getContactInfo()));
    relay.setStatus("1");
    relay.setAvgCost(request.getAvgCost());
    relay.setRemark(safeTrim(request.getRemark()));
    relay.setCreatorUserId(currentUser.getId());
    relay.setCreatorUsername(currentUser.getUsername());

    return venueRelayRepository.save(relay).getId();
  }

  @Override
  @Transactional
  public VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> queryRelayList(VenueRelayDto.RelayPageRequest request) {
    refreshExpiredRelays();

    int page = (request != null && request.getPage() != null && request.getPage() >= 0) ? request.getPage() : 0;
    int size = (request != null && request.getSize() != null && request.getSize() > 0) ? request.getSize() : 10;
    size = Math.min(size, 20);

    Pageable pageable = PageRequest.of(page, size, Sort.by(
        Sort.Order.desc("startDate"),
        Sort.Order.asc("startTime"),
        Sort.Order.desc("createTime")));

    Specification<VenueRelay> spec = (root, query, cb) -> {
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
      }
      return cb.and(predicates.toArray(new Predicate[0]));
    };

    Long currentUserId = getCurrentUser().getId();
    Page<VenueRelay> relayPage = venueRelayRepository.findAll(spec, pageable);
    List<VenueRelayDto.RelayListItem> list = relayPage.getContent().stream()
        .map(relay -> toListItem(relay, currentUserId))
        .collect(Collectors.toList());

    VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> response = new VenueRelayDto.PageResponse<>();
    response.setList(list);
    response.setPage(page);
    response.setSize(size);
    response.setTotal(relayPage.getTotalElements());
    response.setTotalPages(relayPage.getTotalPages());
    return response;
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
    item.setVenueId(relay.getVenue() != null ? relay.getVenue().getId() : null);
    item.setVenueName(relay.getVenue() != null ? relay.getVenue().getName() : null);
    item.setStartDate(relay.getStartDate());
    item.setStartTime(relay.getStartTime());
    item.setEndTime(relay.getEndTime());
    item.setCourtName(relay.getCourtName());
    item.setMaxPeople(relay.getMaxPeople());
    item.setJoinedPeople(relay.getJoinedPeople());
    item.setContactInfo(relay.getContactInfo());
    item.setStatus(relay.getStatus());
    item.setAvgCost(relay.getAvgCost());
    item.setRemark(relay.getRemark());
    item.setCreatorUserId(relay.getCreatorUserId());
    item.setCreatorUsername(relay.getCreatorUsername());
    item.setCreateTime(relay.getCreateTime());
    item.setJoinedByCurrentUser(
        participantRepository.existsByRelay_IdAndUserIdAndStatus(relay.getId(), currentUserId, "1"));
    List<String> participantUserNames = participantRepository
        .findAllByRelay_IdAndStatusOrderByJoinTimeAsc(relay.getId(), "1")
        .stream()
        .map(VenueRelayParticipant::getUserName)
        .collect(Collectors.toList());
    item.setParticipantUserNames(participantUserNames);
    return item;
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
    if (request.getCourtName() == null || request.getCourtName().trim().isEmpty()) {
      throw new IllegalArgumentException("场地不能为空");
    }
    if (request.getMaxPeople() == null || request.getMaxPeople() <= 0) {
      throw new IllegalArgumentException("报名人数上限必须大于0");
    }
    if (request.getRemark() != null && request.getRemark().length() > 1000) {
      throw new IllegalArgumentException("备注长度不能超过1000");
    }
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
}
