package com.hotel.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.hotel.dto.GuestDTO;
import com.hotel.model.Guest;
import com.hotel.repository.GuestRepository;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

@Service
public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Object findGuest(String name, String document, String phone) {
        if (document != null) {
            Optional<Guest> guest = guestRepository.findByDocument(document);

            if (guest.isEmpty()) {
                throw new EntityNotFoundException("Nenhum hóspede encontrado com o documento: " + document);
            }
            GuestDTO guestDto = new ModelMapper().map(guest, GuestDTO.class);
            return guestDto;
        }  
        
        if (phone != null) {
            List<Guest> guests = guestRepository.findByPhone(phone);
            if (guests.isEmpty()) {
                throw new EntityNotFoundException("Nenhum hóspede encontrado com o telefone: " + phone);
            }
            List<GuestDTO> guestDtos = guests.stream().map(guest -> new ModelMapper().map(guest, GuestDTO.class)).collect(Collectors.toList());
            return (guestDtos.size() == 1 ? guestDtos.get(0) : guestDtos);
        }

        if (name != null) {
            List<Guest> guests = guestRepository.findByNameContaining(name);
            if (guests.isEmpty()) {
                throw new EntityNotFoundException("Nenhum hóspede encontrado com o nome: " + name);
            }
            List<GuestDTO> guestDtos = guests.stream().map(guest -> new ModelMapper().map(guest, GuestDTO.class)).collect(Collectors.toList());
            return (guestDtos.size() == 1 ? guestDtos.get(0) : guestDtos);
        } 

        throw new IllegalArgumentException("Nenhum parâmetro de busca foi informado!");
    }

    public GuestDTO registerGuest(GuestDTO guestDto) {
        Optional<Guest> guestExistenceStatus = guestRepository.findByDocument(guestDto.getDocument());
        if (guestExistenceStatus.isPresent()) {
            throw new EntityExistsException("Hóspede com o documento " + guestDto.getDocument() + " já existe!");
        }

        Guest guest = new ModelMapper().map(guestDto, Guest.class);
        guest = guestRepository.save(guest);

        guestDto = new ModelMapper().map(guest, GuestDTO.class);

        return guestDto;
    }

    public GuestDTO findGuestById(Long id) {
        Optional<Guest> guest = guestRepository.findById(id);
        if (guest.isEmpty()) {
            throw new EntityNotFoundException("Hóspede com o id  " + id + " não encontrado!");
        }

        GuestDTO guestDto = new ModelMapper().map(guest.get(), GuestDTO.class);

        return guestDto;
    }

}
