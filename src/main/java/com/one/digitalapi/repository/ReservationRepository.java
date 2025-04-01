package com.one.digitalapi.repository;

import com.one.digitalapi.entity.Reservations;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservations, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Reservations r SET r.reservationStatus = :status, r.cancellationReason = :cancellationReason, r.refundAmount = :refundAmount WHERE r.reservationId = :reservationId")
    void updateReservationStatus(@Param("reservationId") Integer reservationId,
                                 @Param("status") String status,
                                 @Param("cancellationReason") String cancellationReason,
                                 @Param("refundAmount") Integer refundAmount);

    List<Reservations> findByBus_BusIdAndReservationStatus(Integer busId, String reservationStatus);

    List<Reservations> findByReservationStatus(String reservationStatus);

}

