package com.hotel.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.dto.GuestDTO;
import com.hotel.service.GuestService;
import com.hotel.view.model.GuestResponse;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService guestService;

    public GuestController(GuestService guestService) {
        this.guestService = guestService;
    }

    @PostMapping
    public ResponseEntity<?> registerGuest(@RequestBody GuestDTO guestDto) {
        try {
            guestDto = guestService.registerGuest(guestDto);
            GuestResponse guestResponse = new ModelMapper().map(guestDto, GuestResponse.class);
            return new ResponseEntity<>(guestResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        
    }

    @GetMapping("/search")
    public ResponseEntity<?> findGuest(@RequestParam(required = false) String name, @RequestParam(required = false) String document, @RequestParam(required = false) String phone) {
        try {
            Object result = guestService.findGuest(name, document, phone);

            if (result instanceof List<?>) {
                List<GuestDTO> guestDtos = (List<GuestDTO>) result;
                List<GuestResponse> guestResponses = guestDtos.stream().map(guestDto -> new ModelMapper().map(guestDto, GuestResponse.class)).collect(Collectors.toList());
                return ResponseEntity.ok(guestResponses);

            } else {
                GuestDTO guestDto = (GuestDTO) result;
                GuestResponse guestResponse = new ModelMapper().map(guestDto, GuestResponse.class);
                return ResponseEntity.ok(guestResponse);

            }
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }  
    }

}
