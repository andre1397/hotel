package com.hotel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hotel.dto.GuestDTO;
import com.hotel.dto.ReservationDTO;
import com.hotel.model.Reservation;
import com.hotel.repository.ReservationRepository;
import com.hotel.service.ReservationService;
import com.hotel.view.model.CheckOutResponse;
import com.hotel.view.model.ReservationResponse;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationController reservationController;

    private ReservationDTO reservationDto;

    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationDto = new ReservationDTO();
        reservationDto.setId(1L);
        reservationDto.setGuest(new GuestDTO(5L, "Fulano", "123456", "123456789"));
        reservationDto.setHasCar(true);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setGuest(new GuestDTO(5L, "Fulano", "123456", "123456789"));
        reservation.setHasCar(true);
    }

    @Test
    public void testCreateReservation() {
        when(reservationService.createReservation(any(Long.class), any(ReservationDTO.class))).thenReturn(reservationDto);
        ResponseEntity<?> response = reservationController.createReservation(1L, reservationDto);

        ReservationResponse responseBody = (ReservationResponse) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(reservationDto.getId(), responseBody.getId());
        assertEquals(reservationDto.getGuest().getDocument(), responseBody.getGuest().getDocument());
    }

    @Test
    public void testCheckIn() {
        when(reservationService.checkIn(any(Long.class))).thenReturn(reservationDto);
        ResponseEntity<?> response = reservationController.checkIn(1L);

        ReservationResponse responseBody = (ReservationResponse) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservationDto.getId(), responseBody.getId());
        assertEquals(reservationDto.getGuest().getDocument(), responseBody.getGuest().getDocument());
    }

    @Test
    public void testCheckOut() {
        CheckOutResponse checkOutResponse = new CheckOutResponse();
        checkOutResponse.setId(reservationDto.getId());
        checkOutResponse.setGuest(new GuestDTO(reservationDto.getGuest().getId(), reservationDto.getGuest().getName(), reservationDto.getGuest().getDocument(), reservationDto.getGuest().getPhone()));
        when(reservationService.checkOut(any(Long.class))).thenReturn(checkOutResponse);
        ResponseEntity<?> response = reservationController.checkOut(1L);

        CheckOutResponse responseBody = (CheckOutResponse) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reservationDto.getId(), responseBody.getId());
        assertEquals(reservationDto.getGuest().getDocument(), responseBody.getGuest().getDocument());
    }

    @Test
    public void testFindActiveGuests() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationRepository.findByCheckedInTrueAndCheckedOutFalse()).thenReturn(reservations);
        ResponseEntity<?> response = reservationController.findActiveGuests();
        assertNotNull(response);
        Object body = response.getBody();
        if (body instanceof List) {
            assertEquals(1, ((List<?>) body).size());
        }
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindPendingCheckInGuests() {
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationRepository.findByCheckedInFalse()).thenReturn(reservations);
        ResponseEntity<?> response = reservationController.findPendingCheckInGuests();

        assertNotNull(response);
        Object body = response.getBody();
        if (body instanceof List) {
            assertEquals(1, ((List<?>) body).size());
        }
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
