package com.hotel.hotel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hotel.hotel.model.Guest;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    public Optional<Guest> findByDocument(String document);
    public List<Guest> findByPhone(String phone);
    public List<Guest> findByNameContaining(String name);

}
