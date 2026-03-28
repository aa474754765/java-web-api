package com.kazibu.mobile.service;

import com.kazibu.mobile.dto.VenueRelayDto;

public interface VenueRelayService {
  Long createRelay(VenueRelayDto.CreateRequest request);

  void updateRelay(VenueRelayDto.EditRequest request);

  void deleteRelay(VenueRelayDto.CancelRequest request);

  VenueRelayDto.PageResponse<VenueRelayDto.RelayListItem> queryRelayList(VenueRelayDto.RelayPageRequest request);

  void joinRelay(VenueRelayDto.JoinRequest request);

  void cancelJoin(VenueRelayDto.CancelRequest request);
}
