package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    public Employee findById(long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found for ID: " + id));
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        saveEmployee(employee);
    }

    public List<EmployeeDTO> findEmployeesForService(EmployeeRequestDTO employeeRequestDTO) {
        Set service = employeeRequestDTO.getSkills();
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        if (employees != null && employees.size() > 0) {
            for (Employee employee : employees) {
                System.out.println(employee);
                if (employee.getSkills().containsAll(service)) {
                    employeeDTOs.add(mapEntityToDTO(employee));
                }
            }
        }
        return employeeDTOs;
    }

    public List<Employee> getEmployeeBySkillAndDate(Set<EmployeeSkill> skills, LocalDate date) {

        DayOfWeek dayOfWeek = date.getDayOfWeek();
        List<Employee> employees = new ArrayList<Employee>();
        for(EmployeeSkill skill : skills) {
            List<Employee> resultSet = employeeRepository.getAllBySkills(skill);
            for (Employee empl : resultSet) {
                if (!employees.contains(empl) && empl.getDaysAvailable().contains(dayOfWeek) && empl.getSkills().containsAll(skills)) {
                    employees.add(empl);
                }
            }
        }
        return employees;
    }

    public Employee getEmployeeById(long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found, ID: " + id));
    }

    private EmployeeDTO mapEntityToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }
}