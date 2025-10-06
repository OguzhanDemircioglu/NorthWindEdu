package com.server.app.repository;

import com.server.app.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findEmployeeByEmployeeId(Long employeeId);

    void deleteEmployeeByEmployeeId(Long employeeId);

    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    boolean existsEmployeeByEmployeeId(Long employeeId);

    Employee getEmployeeByEmployeeId(Long employeeId);

    boolean existsByFirstNameAndLastNameAndEmployeeIdNot(String firstName, String lastName, Long employeeId);

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE employee_seq RESTART WITH 1", nativeQuery = true)
    void resetEmployeeSequence();
}
