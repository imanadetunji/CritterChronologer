package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ScheduleService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    public ScheduleService(EmployeeRepository employeeRepository, PetRepository petRepository,
                           CustomerRepository customerRepository, ScheduleRepository scheduleRepository) {
        this.employeeRepository = employeeRepository;
        this.petRepository = petRepository;
        this.customerRepository = customerRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Schedule saveSchedule(Schedule schedule, List<Long> employeeIds, List<Long> petIds) {
        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        List<Pet> pets = petRepository.findAllById(petIds);
        schedule.setEmployeeIds(employees);
        schedule.setPetIds(pets);
        return scheduleRepository.save(schedule);
    }

    public List<Schedule> getScheduleByEmployeeId(long employeeId) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("No Employee for ID : " + employeeId));
        return scheduleRepository.getAllEmployeesContains(employee);
    }

    public List<Schedule> getScheduleByPetId(long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException("No Pet for ID : " + petId));
        return scheduleRepository.getAllPetsIn(pet);
    }

    public List<Schedule> getScheduleByCustomerId(long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("No Customer with ID : " + customerId));
        List<Pet> customerPets = customer.getPets();
        return scheduleRepository.getAllByPetsIn(customerPets);
    }

    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }
}