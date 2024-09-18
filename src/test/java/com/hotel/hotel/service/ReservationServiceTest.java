package com.hotel.hotel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.hotel.hotel.dto.GuestDTO;
import com.hotel.hotel.dto.ReservationDTO;
import com.hotel.hotel.model.Reservation;
import com.hotel.hotel.repository.ReservationRepository;
import com.hotel.hotel.view.model.CheckOutResponse;

public class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private GuestService guestService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private LocalTime localTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateLateCheckOutFee() {

    }

    @Test
    void testCalculateTotalStayCost() {

    }

    @Test
    void testCheckIn() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setCheckInDate(LocalDate.now());
        reservation.setCheckedIn(false);
        

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        ReservationDTO checkedInReservation = reservationService.checkIn(reservationId);

        assertNotNull(checkedInReservation);
        assertEquals(reservationId, checkedInReservation.getId());
        assertTrue(checkedInReservation.isCheckedIn());
        assertEquals(LocalDate.now(), checkedInReservation.getCheckInDate());
    }

    @Test
    void testCheckOutHasCarLateCheckoutFee() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        GuestDTO guestDto = new GuestDTO(1L, "João", "123456789", "123456789");
        reservation.setId(reservationId);
        reservation.setGuest(guestDto);
        reservation.setHasCar(true);
        reservation.setCheckInDate(LocalDate.of(2024, 9, 15));
        reservation.setCheckedIn(true);
        reservation.setCheckOutTime(LocalTime.of(14, 0));
        reservation.setCheckedOut(false);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        CheckOutResponse checkOutResponse = reservationService.checkOut(reservationId);

        assertNotNull(checkOutResponse);
        assertEquals(reservationId, checkOutResponse.getId());
        assertEquals(String.format("Valor total: R$ %.2f", reservation.getTotalValue()), checkOutResponse.getTotalValue());
        assertEquals("Valor total: R$ 530,00", checkOutResponse.getTotalValue());
        assertEquals(LocalDate.now(), checkOutResponse.getCheckOutDate());
        assertTrue(checkOutResponse.isCheckedOut());

    }

    @Test
    void testCheckOutNoCarNoLateCheckoutFee() {
        Long reservationId = 1L;
        Reservation reservation = new Reservation();
        GuestDTO guestDto = new GuestDTO(1L, "João", "123456789", "123456789");
        reservation.setId(reservationId);
        reservation.setGuest(guestDto);
        reservation.setHasCar(false);
        reservation.setCheckInDate(LocalDate.of(2024, 9, 15));
        reservation.setCheckedIn(true);
        reservation.setCheckOutTime(LocalTime.of(11, 0));
        reservation.setCheckedOut(false);

        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        CheckOutResponse checkOutResponse = reservationService.checkOut(reservationId);

        assertNotNull(checkOutResponse);
        assertEquals(reservationId, checkOutResponse.getId());
        assertEquals(String.format("Valor total: R$ %.2f", reservation.getTotalValue()), checkOutResponse.getTotalValue());
        assertEquals("Valor total: R$ 420,00", checkOutResponse.getTotalValue());
        assertEquals(LocalDate.now(), checkOutResponse.getCheckOutDate());
        assertTrue(checkOutResponse.isCheckedOut());

    }

    @Test
    void testCreateReservation() {
        Long guestId = 1L;
        GuestDTO guestDto = new GuestDTO(guestId, "João", "123456789", "123456789");
        Reservation reservation = new Reservation();
        reservation.setGuest(guestDto);
        reservation.setHasCar(true);
        ReservationDTO reservationDto = new ReservationDTO();
        reservationDto.setHasCar(true);

        when(guestService.findGuestById(guestId)).thenReturn(guestDto);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        ReservationDTO createdReservation = reservationService.createReservation(guestId, reservationDto);

        assertNotNull(createdReservation);
        assertEquals(guestId, createdReservation.getGuest().getId());
        assertEquals(reservation.getHasCar(), createdReservation.getHasCar());
    }

    @Test
    void testFindActiveGuests() {
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation1 = new Reservation();
        reservation1.setGuest(new GuestDTO(1L, "João", "123456789", "123456789"));
        reservation1.setId(1L);
        reservation1.setCheckInDate(LocalDate.now());
        reservation1.setCheckedIn(true);
        reservation1.setCheckedOut(false);
        reservations.add(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setGuest(new GuestDTO(2L, "Fábio", "987654321", "987654321"));
        reservation2.setId(2L);
        reservation2.setCheckInDate(LocalDate.now());
        reservation2.setCheckedIn(true);
        reservation2.setCheckedOut(false);
        reservations.add(reservation2);

        Reservation reservation3 = new Reservation();
        reservation3.setGuest(new GuestDTO(3L, "Carlos", "546789132", "478521364"));
        reservation3.setId(3L);
        reservation3.setCheckInDate(LocalDate.now());
        reservation3.setCheckedIn(true);
        reservation3.setCheckedOut(true);
        reservations.add(reservation3);

        when(reservationRepository.findByCheckedInTrueAndCheckedOutFalse()).thenReturn(reservations);

        List<GuestDTO> activeGuests = reservationService.findActiveGuests();

        assertNotNull(activeGuests);
        assertEquals(2, activeGuests.size());
        assertEquals(reservation1.getGuest().getId(), activeGuests.get(0).getId());
        assertEquals(reservation2.getGuest().getId(), activeGuests.get(1).getId());
    }

    @Test
    void testFindPendingCheckInGuests() {
        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation1 = new Reservation();
        reservation1.setGuest(new GuestDTO(1L, "João", "123456789", "123456789"));
        reservation1.setId(1L);
        reservation1.setCheckInDate(LocalDate.now());
        reservation1.setCheckedIn(false);
        reservations.add(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setGuest(new GuestDTO(2L, "Fábio", "987654321", "987654321"));
        reservation2.setId(2L);
        reservation2.setCheckInDate(LocalDate.now());
        reservation2.setCheckedIn(false);
        reservations.add(reservation2);

        Reservation reservation3 = new Reservation();
        reservation3.setGuest(new GuestDTO(3L, "Carlos", "546789132", "478521364"));
        reservation3.setId(3L);
        reservation3.setCheckInDate(LocalDate.now());
        reservation3.setCheckedIn(true);
        reservations.add(reservation3);

        when(reservationRepository.findByCheckedInFalse()).thenReturn(reservations);

        List<GuestDTO> pendingGuests = reservationService.findPendingCheckInGuests();

        assertNotNull(pendingGuests);
        assertEquals(2, pendingGuests.size());
        assertEquals(reservation1.getGuest().getId(), pendingGuests.get(0).getId());
        assertEquals(reservation2.getGuest().getId(), pendingGuests.get(1).getId());
    }
}
