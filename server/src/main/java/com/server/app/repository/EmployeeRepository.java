package com.server.app.repository;

import com.server.app.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findEmployeeByEmployeeId(Integer employeeId);

    void deleteEmployeeByEmployeeId(Integer employeeId);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
