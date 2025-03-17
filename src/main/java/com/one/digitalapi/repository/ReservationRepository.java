package com.one.digitalapi.repository;

import com.one.digitalapi.entity.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservations, Integer> {

    public List<Reservations> findByReservationDate(LocalDate date);

}

