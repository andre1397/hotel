package com.hotel.hotel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hotel.hotel.dto.GuestDTO;
import com.hotel.hotel.model.Guest;
import com.hotel.hotel.repository.GuestRepository;

import jakarta.persistence.EntityNotFoundException;

public class GuestServiceTest {

    @InjectMocks
    private GuestService guestService;

    @Mock
    private GuestRepository guestRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindGuestByDocumentFound() {
        Long id = 1L;
        String name = "Fulano";
        String document = "123456";
        String phone = "123456789";

        Guest guest = new Guest();
        guest.setId(id);
        guest.setName(name);
        guest.setDocument(document);
        guest.setPhone(phone);

        GuestDTO expectedGuest = new GuestDTO(id, name, document, phone);
        
        when(guestRepository.findByDocument(document)).thenReturn(Optional.of(guest));
        Object result = guestService.findGuest(null, document, null);

        assertNotNull(result);
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedGuest);
    }

    @Test
    void testFindGuestByDocumentNotFound() {
        String document = "123456";
        
        when(guestRepository.findByDocument(document)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            guestService.findGuest(null, document, null);
        });

        assertEquals("Nenhum hóspede encontrado com o documento: " + document, exception.getMessage());
    }

    @Test
    void testFindGuestByPhoneFoundMultiple() {
        String phone = "987654321";

        Guest guest1 = new Guest(1L, "Fulano", "123456", phone);
        Guest guest2 = new Guest(2L, "Siclano", "654321", phone);

        GuestDTO expectedGuest1 = new GuestDTO(1L, "Fulano", "123456", phone);
        GuestDTO expectedGuest2 = new GuestDTO(2L, "Siclano", "654321", phone);

        List<Guest> guests = List.of(guest1, guest2);

        when(guestRepository.findByPhone(phone)).thenReturn(guests);

        Object result = guestService.findGuest(null, null, phone);

        assertTrue(result instanceof List);
        List<GuestDTO> resultList = (List<GuestDTO>) result;

        assertEquals(2, resultList.size());

        assertThat(resultList.get(0))
            .usingRecursiveComparison()
            .isEqualTo(expectedGuest1);
        assertThat(resultList.get(1))
            .usingRecursiveComparison()
            .isEqualTo(expectedGuest2);
    }

    @Test
    void testFindGuestByPhoneSingleResult() {
    String phone = "123456789";
    Guest guest = new Guest(1L, "Fulano", "123456", phone);

    List<Guest> guests = List.of(guest);

    when(guestRepository.findByPhone(phone)).thenReturn(guests);

    GuestDTO expectedGuest = new GuestDTO(1L, "Fulano", "123456", phone);

    GuestDTO result = (GuestDTO) guestService.findGuest(null, null, phone);

    assertNotNull(result);
    assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(expectedGuest);
}

    @Test
    void testFindGuestByPhoneNotFound() {
        String phone = "987654321";
        
        when(guestRepository.findByPhone(phone)).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            guestService.findGuest(null, null, phone);
        });

        assertEquals("Nenhum hóspede encontrado com o telefone: " + phone, exception.getMessage());
    }

    @Test
    void testFindGuestByNameFoundMultiple() {
        String name = "Fulano dos Santos";
        Guest guest1 = new Guest(1L, name, "123456", "123456789");
        Guest guest2 = new Guest(2L, name, "654321", "987654321");

        GuestDTO expectedGuest1 = new GuestDTO(1L, name, "123456", "123456789");
        GuestDTO expectedGuest2 = new GuestDTO(2L, name, "654321", "987654321");

        List<Guest> guests = List.of(guest1, guest2);

        when(guestRepository.findByNameContaining(name)).thenReturn(guests);

        Object result = guestService.findGuest(name, null, null);

        assertTrue(result instanceof List);
        List<GuestDTO> resultList = (List<GuestDTO>) result;

        assertEquals(2, resultList.size());
        assertThat(resultList.get(0))
            .usingRecursiveComparison()
            .isEqualTo(expectedGuest1);
        assertThat(resultList.get(1))
            .usingRecursiveComparison()
            .isEqualTo(expectedGuest2);
    }

    @Test
    void testFindGuestByNameSingleResult() {
    String name = "Fulano dos Santos";
    Guest guest = new Guest(1L, name, "123456", "123456789");

    List<Guest> guests = List.of(guest);

    when(guestRepository.findByNameContaining(name)).thenReturn(guests);

    GuestDTO expectedGuest = new GuestDTO(1L, name, "123456", "123456789");

    GuestDTO result = (GuestDTO) guestService.findGuest(name, null, null);

    assertNotNull(result);
    assertThat(result)
        .usingRecursiveComparison()
        .isEqualTo(expectedGuest);
}

    @Test
    void testFindGuestByNameNotFound() {
        String name = "John";
        
        when(guestRepository.findByNameContaining(name)).thenReturn(Collections.emptyList());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            guestService.findGuest(name, null, null);
        });

        assertEquals("Nenhum hóspede encontrado com o nome: " + name, exception.getMessage());
    }

    @Test
    void testRegisterGuest() {
        GuestDTO guestDto = new GuestDTO(null, "Fulano", "123456", "123456789");

        // Cria um objeto Guest esperado após a registro
        Guest guest = new Guest(1L, "Fulano", "123456", "123456789");
        GuestDTO expectedGuestDto = new GuestDTO(1L, "Fulano", "123456", "123456789");

        // Configura o mock do repositório para retornar o guest esperado
        when(guestRepository.save(any(Guest.class))).thenReturn(guest);

        // Chama o método registerGuest
        GuestDTO result = guestService.registerGuest(guestDto);

        // Verifica se o resultado é igual ao esperado
        assertNotNull(result);
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedGuestDto);
    }

    @Test
    void testFindGuestByIdFound() {
        Long guestId = 1L;
        Guest guest = new Guest(guestId, "Fulano", "123456", "123456789");
        GuestDTO expectedGuest = new GuestDTO(guestId, "Fulano", "123456", "123456789");
        
        when(guestRepository.findById(guestId)).thenReturn(Optional.of(guest));

        GuestDTO result = guestService.findGuestById(guestId);

        assertNotNull(result);
        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(expectedGuest);
    }

    @Test
    void testFindGuestByIdNotFound() {
        Long guestId = 1L;

        when(guestRepository.findById(guestId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            guestService.findGuestById(guestId);
        });

        assertEquals("Hóspede com o id  " + guestId + " não encontrado!", exception.getMessage());
    }
}
