package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.user.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findAllByEmployeesContains(Employee employee);

    List<Schedule> findAllByPetsContains(Pet pet);

    List<Schedule> findAllByPetsIn(List<Pet> customerPets);
}
