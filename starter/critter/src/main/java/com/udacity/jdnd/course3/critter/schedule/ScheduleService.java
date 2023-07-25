package com.udacity.jdnd.course3.critter.schedule;

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

    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.save(mapDTOToEntity(scheduleDTO));
        return mapEntityToDTO(schedule);
    }

    public List<ScheduleDTO> getAllSchedules() {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        List<Schedule> retrievedSchedules = scheduleRepository.findAll();
        if (retrievedSchedules != null) {
            for (Schedule schedule : retrievedSchedules) {
                scheduleDTOs.add(mapEntityToDTO(schedule));
            }
        }
        return scheduleDTOs;
    }

    public List<ScheduleDTO> getScheduleForPet(long petId) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        Pet pet = petRepository.getOne(petId);
        List<Schedule> retrievedSchedules = scheduleRepository.findAll();
        if (retrievedSchedules != null) {
            for (Schedule schedule : retrievedSchedules) {
                if (schedule.getPets().contains(pet)) {
                    scheduleDTOs.add(mapEntityToDTO(schedule));
                }
            }
        }
        return scheduleDTOs;
    }

    public List<ScheduleDTO> getScheduleForEmployee(long employeeId) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        Employee employee = employeeRepository.getOne(employeeId);
        List<Schedule> retrievedSchedules = scheduleRepository.findAll();
        if (retrievedSchedules != null) {
            for (Schedule schedule : retrievedSchedules) {
                if (schedule.getEmployees().contains(employee)) {
                    scheduleDTOs.add(mapEntityToDTO(schedule));
                }
            }
        }
        return scheduleDTOs;
    }

    public List<ScheduleDTO> getScheduleForCustomer(long customerId) {
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
        Customer customer = customerRepository.getOne(customerId);
        List<Schedule> retrievedSchedules = scheduleRepository.findAll();
        if (retrievedSchedules != null) {
            for (Schedule schedule : retrievedSchedules) {
                if (schedule.getPets() != null) {
                    for (Pet pet : schedule.getPets()) {
                        if (pet.getCustomer() != null && pet.getCustomer().equals(customer)) {
                            scheduleDTOs.add(mapEntityToDTO(schedule));
                        }
                    }
                }
            }
        }
        return scheduleDTOs;
    }

    private Schedule mapDTOToEntity(ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        List<Employee> employeeList = new ArrayList<>();
        if (scheduleDTO.getEmployeeIds() != null) {
            for (long employeeId : scheduleDTO.getEmployeeIds()) {
                employeeList.add(employeeRepository.getOne(employeeId));
            }
        }
        schedule.setEmployees(employeeList);

        List<Pet> petList = new ArrayList<>();

        if (scheduleDTO.getPetIds() != null) {
            for (long petId : scheduleDTO.getPetIds()) {
                petList.add(petRepository.getOne(petId));
            }
        }
        schedule.setPets(petList);
        return schedule;
    }

    private ScheduleDTO mapEntityToDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);
        List<Long> petIds = new ArrayList<>();
        List<Long> employeeIds = new ArrayList<>();
        if (schedule.getEmployees() != null) {
            for (Employee employee : schedule.getEmployees()) {
                employeeIds.add(employee.getId());
            }
        }
        scheduleDTO.setEmployeeIds(employeeIds);

        if (schedule.getPets() != null) {
            for (Pet pet : schedule.getPets()) {
                petIds.add(pet.getId());
            }
        }
        scheduleDTO.setPetIds(petIds);

        return scheduleDTO;
    }
}