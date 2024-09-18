package com.hotel.hotel.view.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.hotel.hotel.dto.GuestDTO;

public class CheckOutResponse {

    private Long id;
    private GuestDTO guestDto;
    private LocalDate checkInDate;
    private LocalTime checkInTime;
    private LocalDate checkOutDate;
    private LocalTime checkOutTime;
    private boolean hasCar;
    private boolean checkedIn;
    private boolean checkedOut;
    private List<String> rentedDays;
    private String carFee;
    private String lateCheckoutFee;
    private String totalValue;

    public CheckOutResponse(){

    }

    public CheckOutResponse(Long id, GuestDTO guestDto, LocalDate checkInDate, LocalTime checkInTime, LocalDate checkOutDate, LocalTime checkOutTime, boolean hasCar, boolean checkedIn) {
        this.id = id;
        this.guestDto = guestDto;
        this.checkedIn = checkedIn;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
        this.checkOutDate = checkOutDate;
        this.checkOutTime = checkOutTime;
        this.hasCar = hasCar;
        this.checkedIn = checkedIn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GuestDTO getGuest() {
        return guestDto;
    }

    public void setGuest(GuestDTO guestDto) {
        this.guestDto = guestDto;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public boolean getHasCar() {
        return hasCar;
    }

    public void setHasCar(boolean hasCar) {
        this.hasCar = hasCar;
    }

    public boolean isCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public List<String> getRentedDays() {
        return rentedDays;
    }

    public void setRentedDays(List<String> rentedDays) {
        this.rentedDays = rentedDays;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getCarFee() {
        return carFee;
    }

    public void setCarFee(String carFee) {
        this.carFee = carFee;
    }

    public String getLateCheckoutFee() {
        return lateCheckoutFee;
    }

    public void setLateCheckoutFee(String lateCheckoutFee) {
        this.lateCheckoutFee = lateCheckoutFee;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

}
