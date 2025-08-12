package com.server.app.service;

import com.server.app.model.Employees;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Optional<Employees> get(String firstName, String lastName);

    Employees create(Employees employee);

    Employees update(String firstName, String lastName, Employees employee);

    void delete(String firstName, String lastName);

    List<Employees> list();
}
