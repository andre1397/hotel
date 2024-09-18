package com.hotel.hotel.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hotel.hotel.dto.GuestDTO;
import com.hotel.hotel.dto.ReservationDTO;
import com.hotel.hotel.model.Reservation;
import com.hotel.hotel.repository.ReservationRepository;
import com.hotel.hotel.view.model.CheckOutResponse;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final GuestService guestService;

    public ReservationService(ReservationRepository reservationRepository, GuestService guestService) {
        this.reservationRepository = reservationRepository;
        this.guestService = guestService;
    }

    public ReservationDTO createReservation(Long guestId, ReservationDTO reservationDto) {
        GuestDTO guestDto = guestService.findGuestById(guestId);
        reservationDto.setGuest(guestDto);
        Reservation reservation = new ModelMapper().map(reservationDto, Reservation.class);
        reservation = reservationRepository.save(reservation);
        return new ModelMapper().map(reservation, ReservationDTO.class);
    }

    public ReservationDTO checkIn(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada!"));

        LocalTime checkTime = LocalTime.now();

        if (checkTime.isBefore(LocalTime.of(14, 0))) {
            throw new IllegalArgumentException("Check-in não pode ser efetuado antes das 14:00!");
        }

        reservation.setCheckInDate(LocalDate.now());
        reservation.setCheckInTime(checkTime);
        reservation.setCheckedIn(true);
        reservation = reservationRepository.save(reservation);
        return new ModelMapper().map(reservation, ReservationDTO.class);
    }

    public CheckOutResponse checkOut (Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new EntityNotFoundException("Reserva não encontrada!"));

        boolean checkedIn = reservation.isCheckedIn();
        if (!checkedIn) {
            throw new IllegalArgumentException("Hóspede ainda não fez check-in!");
        }

        boolean checkedOut = reservation.isCheckedOut();
        if(checkedOut) {
            throw new IllegalArgumentException("Hóspede já fez check-out!");
        }

        if (reservation.getCheckOutTime() == null) {
            reservation.setCheckOutTime(LocalTime.now());
        }

        LocalDate checkInDate = reservation.getCheckInDate();
        LocalDate checkOutDate = LocalDate.now();

        LocalTime checkinTime = reservation.getCheckInTime();
        LocalTime checkOutTime = reservation.getCheckOutTime();

        boolean hasCar = reservation.getHasCar();

        GuestDTO guestDto = new ModelMapper().map(reservation.getGuest(), GuestDTO.class);

        CheckOutResponse checkOutResponse = new CheckOutResponse(reservationId, guestDto, checkInDate, checkinTime, checkOutDate, checkOutTime, hasCar, checkedIn);

        Double totalValue = calculateTotalStayCost(checkOutResponse);

        reservation.setCheckOutDate(checkOutDate);
        reservation.setCheckedOut(true);
        reservation.setTotalValue(totalValue);
        reservation = reservationRepository.save(reservation);

        checkOutResponse.setCheckedOut(true);
        checkOutResponse.setTotalValue("Valor total: R$ " + String.format("%.2f", totalValue));

        return checkOutResponse;
    }

    public Double calculateTotalStayCost(CheckOutResponse checkOutResponse) {
        Double totalValue = 0.0;
        Double carFee = 0.0;
        Double lateCheckoutFee = 0.0;

        LocalDate checkInDate = checkOutResponse.getCheckInDate();
        LocalDate checkOutDate = checkOutResponse.getCheckOutDate();
        LocalDate currentDate = checkInDate;

        boolean hasCar = checkOutResponse.getHasCar();

        List<String> rentedDays = new ArrayList<>();

        while (checkInDate.isEqual(checkOutDate)? !currentDate.isAfter(checkOutDate) : currentDate.isBefore(checkOutDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            Double currentDayRate = 0.0;
            Double dailyCarFee = 0.0;
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                currentDayRate += 180.0;
                if (hasCar) {
                    dailyCarFee += 20.0;
                    carFee += dailyCarFee;
                    totalValue += dailyCarFee;
                }
                totalValue += currentDayRate;

            } else {
                currentDayRate += 120.0;
                if (hasCar) {
                    dailyCarFee += 15.0;
                    carFee += dailyCarFee;
                    totalValue += dailyCarFee;
                }
                totalValue += currentDayRate;
            }

            rentedDays.add(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " 
                + dayOfWeek.getDisplayName(TextStyle.FULL, new Locale("pt", "BR")) 
                + " - R$ " + String.format("%.2f", currentDayRate)
                + (dailyCarFee > 0.0 ? " - Estacionamento: R$ " + String.format("%.2f", dailyCarFee) :""));
            currentDate = currentDate.plusDays(1);
        }

        if (carFee > 0.0) {
            checkOutResponse.setCarFee(String.format("%.2f", carFee));
        }
        
        checkOutResponse.setRentedDays(rentedDays);

        if (!checkInDate.isEqual(checkOutDate) && checkOutResponse.getCheckOutTime().isAfter(LocalTime.of(12, 0))) {
            lateCheckoutFee = calculateLateCheckOutFee(checkOutDate);
            totalValue += lateCheckoutFee;

            checkOutResponse.setLateCheckoutFee(String.format("%.2f", lateCheckoutFee));
        }

        return totalValue;
    }

    public Double calculateLateCheckOutFee(LocalDate checkOutDate) {
        Double lateCheckoutFee = 0.0;
        if (checkOutDate.getDayOfWeek() == DayOfWeek.SATURDAY || checkOutDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            lateCheckoutFee += 90.0;
        } else {
            lateCheckoutFee += 60.0;
        }

        return lateCheckoutFee;
    }

    public List<GuestDTO> findActiveGuests() {
        List<Reservation> reservations = reservationRepository.findByCheckedInTrueAndCheckedOutFalse();
        if (reservations.isEmpty()) {
            throw new EntityNotFoundException("Nenhum hóspede ativo encontrado!");
        }

        List<GuestDTO> guestDtos = reservations.stream()
            .filter(reservation -> reservation.isCheckedIn())
            .filter(reservation -> !reservation.isCheckedOut())
            .map(reservation -> new ModelMapper().map(reservation.getGuest(), GuestDTO.class))
            .collect(Collectors.toList());
        return guestDtos;
    }

    public List<GuestDTO> findPendingCheckInGuests() {
        List<Reservation> reservations = reservationRepository.findByCheckedInFalse();
        if (reservations.isEmpty()) {
            throw new EntityNotFoundException("Nenhum hóspede pendente de check-in encontrado!");
        }

        List<GuestDTO> guestDtos = reservations.stream()
            .filter(reservation -> !reservation.isCheckedIn())
            .map(reservation -> new ModelMapper().map(reservation.getGuest(), GuestDTO.class))
            .collect(Collectors.toList());
        return guestDtos;
    }

}
