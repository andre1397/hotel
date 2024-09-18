package com.hotel.view.model;

import java.util.List;

import com.hotel.dto.GuestDTO;

public class ActiveInactiveGuestsResponse {

    List<GuestDTO> guests;

    public ActiveInactiveGuestsResponse(List<GuestDTO> guests) {
        this.guests = guests;
    }

    public List<GuestDTO> getGuests() {
        return guests;
    }

    public void setGuests(List<GuestDTO> guests) {
        this.guests = guests;
    }

    

}
