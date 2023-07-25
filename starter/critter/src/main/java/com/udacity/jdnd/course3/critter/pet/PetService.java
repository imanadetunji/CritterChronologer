package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerRepository customerRepository;

    public PetDTO save(PetDTO petDTO) {
        Pet pet = petRepository.save(mapDTOToEntity(petDTO));
        if (pet.getCustomer() != null) {
            Customer customer = pet.getCustomer();
            customer.insertPet(pet);
            customerRepository.save(customer);
        }
        return mapEntityToDTO(pet);
    }

    public PetDTO getOne(long petId) {
        return mapEntityToDTO(petRepository.getOne(petId));
    }

    public List<PetDTO> getPetsByOwner(long id) {
        Customer customer = customerRepository.getOne(id);
        List<PetDTO> petDTOs = new ArrayList<>();
        if (customer != null && customer.getPets() != null) {
            for (Pet pet : customer.getPets()) {
                petDTOs.add(mapEntityToDTO(pet));
            }
        }
        return petDTOs;
    }

    public List<PetDTO> getPets() {
        List<Pet> petList = petRepository.findAll();
        List<PetDTO> petDTOs = new ArrayList<>();
        for (Pet pet : petList) {
            petDTOs.add(mapEntityToDTO(pet));
        }
        return petDTOs;
    }

    private Pet mapDTOToEntity(PetDTO petDTO) {
        Pet pet = new Pet();
        BeanUtils.copyProperties(petDTO, pet);
        if (petDTO.getOwnerId() != 0) {
            pet.setCustomer(customerRepository.getOne(petDTO.getOwnerId()));
        }
        return pet;
    }

    private PetDTO mapEntityToDTO(Pet pet) {
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getCustomer() != null) {
            petDTO.setOwnerId(pet.getCustomer().getId());
        }
        return petDTO;
    }
}