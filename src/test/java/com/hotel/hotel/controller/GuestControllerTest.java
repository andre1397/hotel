package com.hotel.hotel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hotel.hotel.dto.GuestDTO;
import com.hotel.hotel.service.GuestService;
import com.hotel.hotel.view.model.GuestResponse;

@ExtendWith(MockitoExtension.class)
public class GuestControllerTest {

    @Mock
    private GuestService guestService;

    @InjectMocks
    private GuestController guestController;

    private GuestDTO guestDto;

    @BeforeEach
    void setUp() {
        guestDto = new GuestDTO(1L, "Fulano", "123456", "123456789");
    }

    @Test
    void testFindGuestByName() {
        when(guestService.findGuest(any(), any(), any())).thenReturn(guestDto);

        ResponseEntity<?> response = guestController.findGuest("Fulano", null, null);

        GuestResponse responseBody = (GuestResponse) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(guestDto.getName(), responseBody.getName());
    }

    @Test
    void testFindGuestByDocument() {
        when(guestService.findGuest(any(), any(), any())).thenReturn(guestDto);

        ResponseEntity<?> response = guestController.findGuest(null, "123456", null);

        GuestResponse responseBody = (GuestResponse) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(guestDto.getDocument(), responseBody.getDocument());
    }

    @Test
    void testFindGuestByPhone() {
        when(guestService.findGuest(any(), any(), any())).thenReturn(guestDto);

        ResponseEntity<?> response = guestController.findGuest(null, null, "123456789");

        GuestResponse responseBody = (GuestResponse) response.getBody();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(guestDto.getPhone(), responseBody.getPhone());
    }

    @Test
    void testRegisterGuest() {
        // Arrange
        when(guestService.registerGuest(any())).thenReturn(guestDto);

        // Act
        ResponseEntity<?> response = guestController.registerGuest(guestDto);

        GuestResponse responseBody = (GuestResponse) response.getBody();

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(guestDto.getName(), responseBody.getName());
    }
}
