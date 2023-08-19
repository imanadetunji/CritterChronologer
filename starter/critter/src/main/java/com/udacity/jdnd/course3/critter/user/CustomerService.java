package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.pet.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PetService petService;

    public CustomerService(CustomerRepository customerRepository, PetService petService) {
        this.customerRepository = customerRepository;
        this.petService = petService;
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();

    }

    public Customer getOwnerByPetId(long petId) {
        Long customerId = petService.getPetById(petId).getOwnerId().getId();
        return customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("No Customer found for Pets Owner ID : " + customerId + ", Pet Id is : " + petId));
    }
}