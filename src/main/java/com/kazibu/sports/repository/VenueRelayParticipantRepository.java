package com.kazibu.sports.repository;

import com.kazibu.sports.entity.VenueRelayParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VenueRelayParticipantRepository extends JpaRepository<VenueRelayParticipant, Long> {
  Optional<VenueRelayParticipant> findByRelay_IdAndUserId(Long relayId, Long userId);

  boolean existsByRelay_IdAndUserIdAndStatus(Long relayId, Long userId, String status);

  List<VenueRelayParticipant> findAllByUserIdAndStatus(Long userId, String status);

  List<VenueRelayParticipant> findAllByRelay_IdAndStatusOrderByJoinTimeAsc(Long relayId, String status);
}
