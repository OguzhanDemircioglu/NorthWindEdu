package com.server.app.repository;

import com.server.app.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findEmployeeByEmployeeId(Long employeeId);

    void deleteEmployeeByEmployeeId(Long employeeId);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsEmployeeByEmployeeId(Long employeeId);

    Employee getEmployeeByEmployeeId(Long employeeId);
}
