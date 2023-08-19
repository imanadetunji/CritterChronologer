package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
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

    public PetService(PetRepository petRepository, CustomerRepository customerRepository) {
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
    }

    public Pet save(Pet pet, Long customerId) {
        Pet newPet = new Pet();
        Customer customer =
                customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("No Customer Exist for ID : " + customerId));
        pet.setOwnerId(customer);
        newPet = petRepository.save(pet);
        customer.insertPet(newPet);
        customerRepository.save(customer);
        return newPet;
    }

    public Pet getPetById(Long id) {
        return petRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet does not exist for ID: " + id));
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public List<Pet> getPetsByOwner(long ownerId) {
        Customer customer = customerRepository.findById(ownerId).orElseThrow(()-> new ResourceNotFoundException("No Customer Exist for ID : " + ownerId));
        return petRepository.findByCustomer(customer);
    }
}