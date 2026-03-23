package com.kazibu.sports.service.impl;

import com.kazibu.auth.entity.User;
import com.kazibu.auth.repository.UserRepository;
import com.kazibu.sports.dto.VenueRelayDto;
import com.kazibu.sports.entity.VenueRelay;
import com.kazibu.sports.entity.VenueRelayParticipant;
import com.kazibu.sports.repository.VenueRelayParticipantRepository;
import com.kazibu.sports.repository.VenueRelayRepository;
import com.kazibu.sports.service.VenueRelayService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("sportsVenueRelayService")
public class VenueRelayServiceImpl implements VenueRelayService {
  @Autowired
  private VenueRelayRepository venueRelayRepository;

  @Autowired
  private VenueRelayParticipantRepository participantRepository;

  @Autowired
  private UserRepository userRepository;

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
        if (request.getCity() != null && !request.getCity().trim().isEmpty()) {
          predicates.add(cb.equal(root.get("venue").get("city"), request.getCity().trim()));
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
