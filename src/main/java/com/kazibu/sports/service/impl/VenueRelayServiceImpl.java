package com.kazibu.sports.service.impl;

import com.kazibu.sports.dto.VenueRelayDto;
import com.kazibu.sports.entity.VenueRelay;
import com.kazibu.sports.repository.VenueRelayRepository;
import com.kazibu.sports.service.VenueRelayService;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VenueRelayServiceImpl implements VenueRelayService {

  private final VenueRelayRepository venueRelayRepository;

  public VenueRelayServiceImpl(VenueRelayRepository venueRelayRepository) {
    this.venueRelayRepository = venueRelayRepository;
  }

  @Override
  @Transactional(readOnly = true)
  public VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> queryRelayList(
      VenueRelayDto.RelayPageRequest request) {
    final VenueRelayDto.RelayPageRequest req =
        request != null ? request : new VenueRelayDto.RelayPageRequest();
    int page = req.getPage() != null && req.getPage() >= 0 ? req.getPage() : 0;
    int size = req.getSize() != null && req.getSize() > 0 ? req.getSize() : 10;
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createTime"));

    Specification<VenueRelay> spec =
        (root, query, cb) -> {
          List<Predicate> predicates = new ArrayList<>();
          if (req.getVenueId() != null) {
            predicates.add(cb.equal(root.get("venue").get("id"), req.getVenueId()));
          }
          if (req.getCity() != null && !req.getCity().isBlank()) {
            predicates.add(cb.equal(root.get("venue").get("city"), req.getCity().trim()));
          }
          if (req.getStatus() != null && !req.getStatus().isBlank()) {
            predicates.add(cb.equal(root.get("status"), req.getStatus().trim()));
          }
          if (req.getSkillLevel() != null && !req.getSkillLevel().isBlank()) {
            predicates.add(cb.equal(root.get("skillLevel"), req.getSkillLevel().trim()));
          }
          if (req.getStartDate() != null) {
            predicates.add(cb.equal(root.get("startDate"), req.getStartDate()));
          }
          return cb.and(predicates.toArray(new Predicate[0]));
        };

    Page<VenueRelay> pageResult = venueRelayRepository.findAll(spec, pageable);
    List<VenueRelayDto.RelayListItem> items =
        pageResult.getContent().stream().map(this::toListItem).toList();

    VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> resp =
        new VenueRelayDto.PageResponse<>();
    resp.setList(items);
    long total = pageResult.getTotalElements();
    resp.setTotal(total);
    resp.setPage(page);
    resp.setSize(size);
    resp.setTotalPages(size > 0 ? (int) Math.ceil((double) total / size) : 0);
    return resp;
  }

  private VenueRelayDto.RelayListItem toListItem(VenueRelay relay) {
    VenueRelayDto.RelayListItem item = new VenueRelayDto.RelayListItem();
    item.setId(relay.getId());
    if (relay.getVenue() != null) {
      item.setVenueId(relay.getVenue().getId());
      item.setVenueName(relay.getVenue().getName());
    }
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
    item.setJoinedByCurrentUser(false);
    item.setCreateTime(relay.getCreateTime());
    return item;
  }
}
