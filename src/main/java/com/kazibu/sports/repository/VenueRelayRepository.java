package com.kazibu.sports.repository;

import com.kazibu.sports.entity.VenueRelay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VenueRelayRepository extends JpaRepository<VenueRelay, Long>, JpaSpecificationExecutor<VenueRelay> {
  @Modifying
  @Query("update VenueRelay r set r.joinedPeople = r.joinedPeople + 1 " +
      "where r.id = :relayId and r.status = '1' and r.joinedPeople < r.maxPeople")
  int incrementJoinedPeopleIfAvailable(@Param("relayId") Long relayId);

  @Modifying
  @Query("update VenueRelay r set r.joinedPeople = r.joinedPeople - 1 " +
      "where r.id = :relayId and r.joinedPeople > 0")
  int decrementJoinedPeople(@Param("relayId") Long relayId);

  @Modifying
  @Query("update VenueRelay r set r.status = '2' " +
      "where r.status = '1' and (r.startDate < current_date or (r.startDate = current_date and r.endTime <= current_time))")
  int closeExpiredRelays();
}
