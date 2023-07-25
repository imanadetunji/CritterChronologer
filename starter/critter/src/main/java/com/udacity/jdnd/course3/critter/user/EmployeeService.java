package com.udacity.jdnd.course3.critter.user;

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

    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        Employee retrievedEmployee = employeeRepository.save(employee);
        EmployeeDTO retrievedEmployeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(retrievedEmployee, retrievedEmployeeDTO);
        return retrievedEmployeeDTO;
    }

    public EmployeeDTO findById(long id) {
        Employee retrievedEmployee = employeeRepository.getOne(id);
        EmployeeDTO retrievedEmployeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(retrievedEmployee, retrievedEmployeeDTO);
        return retrievedEmployeeDTO;
    }

    public void setAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = employeeRepository.getOne(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
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

    private EmployeeDTO mapEntityToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }
}