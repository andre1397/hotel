package com.hotel.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.dto.GuestDTO;
import com.hotel.dto.ReservationDTO;
import com.hotel.service.ReservationService;
import com.hotel.view.model.ActiveInactiveGuestsResponse;
import com.hotel.view.model.CheckOutResponse;
import com.hotel.view.model.ReservationResponse;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/guest/{guestId}")
    public ResponseEntity<?> createReservation(@PathVariable Long guestId, @RequestBody ReservationDTO reservationDetails) {
        try {
            reservationDetails = reservationService.createReservation(guestId, reservationDetails);
            ReservationResponse ReservationResponse = new ModelMapper().map(reservationDetails, ReservationResponse.class);
            return new ResponseEntity<>(ReservationResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/check-in/{reservationId}")
    public ResponseEntity<?> checkIn(@PathVariable Long reservationId) {
        try {
            ReservationDTO reservationDTO = reservationService.checkIn(reservationId);
            ReservationResponse ReservationResponse = new ModelMapper().map(reservationDTO, ReservationResponse.class);
            return new ResponseEntity<>(ReservationResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        
    }

    @PutMapping("/check-out/{reservationId}")
    public ResponseEntity<?> checkOut(@PathVariable Long reservationId) {
        try {
            CheckOutResponse checkOutResponse = reservationService.checkOut(reservationId);
            return new ResponseEntity<>(checkOutResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> findActiveGuests() {
        try {
            List<GuestDTO> activeGuests = reservationService.findActiveGuests();
            ActiveInactiveGuestsResponse activeGuestsResponse = new ActiveInactiveGuestsResponse(activeGuests);
            return new ResponseEntity<>(activeGuestsResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        
    }

    @GetMapping("/pending-checkin")
    public ResponseEntity<?> findPendingCheckInGuests() {
        try {
            List<GuestDTO> inactiveGuests = reservationService.findPendingCheckInGuests();
            ActiveInactiveGuestsResponse inactiveGuestsResponse = new ActiveInactiveGuestsResponse(inactiveGuests);
            return new ResponseEntity<>(inactiveGuestsResponse, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
