package com.hotel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    public List<Reservation> findByCheckedInTrueAndCheckedOutFalse();
    public List<Reservation> findByCheckedInFalse();
    public List<Reservation> findByCheckedOutFalse();

}
