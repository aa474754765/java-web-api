package com.kazibu.sports.service;

import com.kazibu.sports.dto.VenueRelayDto;

public interface VenueRelayService {
  VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> queryRelayList(VenueRelayDto.RelayPageRequest request);
}
