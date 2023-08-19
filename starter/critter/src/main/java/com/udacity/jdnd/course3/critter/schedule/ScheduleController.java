package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = new Schedule();
        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Long> petIds = scheduleDTO.getPetIds();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule = scheduleService.saveSchedule(schedule, employeeIds, petIds);
        BeanUtils.copyProperties(schedule, scheduleDTO);
        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        return getScheduleDTOs(scheduleService.getAllSchedules());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        return getScheduleDTOs(scheduleService.getScheduleByPetId(petId));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        return getScheduleDTOs(scheduleService.getScheduleByEmployeeId(employeeId));
    }

    private List<ScheduleDTO> getScheduleDTOs(List<Schedule> employeeSchedules) {
        List<ScheduleDTO> scheduleDTOS = new ArrayList<ScheduleDTO>();
        for (Schedule schedule : employeeSchedules) {
            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setEmployeeIds(getEmployeeIdsFromSchedule(schedule));
            scheduleDTO.setPetIds(getPetIdsFromSchedule(schedule));
            scheduleDTO.setActivities(schedule.getActivities());
            scheduleDTO.setDate(schedule.getDate());
            scheduleDTOS.add(scheduleDTO);
        }
        return scheduleDTOS;
    }

    private List<Long> getPetIdsFromSchedule(Schedule schedule) {
        List<Long> petIds = new ArrayList<>();
        List<Pet> pets = schedule.getPets();
        for(Pet pet : pets) {
            petIds.add(pet.getId());
        }
        return petIds;
    }

    private List<Long> getEmployeeIdsFromSchedule(Schedule schedule) {
        List<Long> employeeIds = new ArrayList<>();
        List<Employee> employees = schedule.getEmployees();
        for(Employee employee : employees) {
            employeeIds.add(employee.getId());
        }
        return employeeIds;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> customerSchedules = scheduleService.getScheduleByCustomerId(customerId);
        return getScheduleDTOs(customerSchedules);
    }
}