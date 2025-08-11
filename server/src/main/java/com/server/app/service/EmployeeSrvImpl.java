package com.server.app.service;

import com.server.app.model.Employees;
import com.server.app.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeSrvImpl implements EmployeeService {

    private final EmployeeRepository repo;

    public EmployeeSrvImpl(EmployeeRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employees> get(String firstName, String lastName) {
        return repo.findByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public Employees create(Employees employee) {
        boolean exists = repo.existsByFirstNameAndLastName(
                employee.getFirstName(), employee.getLastName());
        if (exists) {
            throw new IllegalStateException("Çalışan zaten var: "
                    + employee.getFirstName() + " " + employee.getLastName());
        }
        return repo.save(employee);
    }

    @Override
    public Employees update(String firstName, String lastName, Employees incoming) {
        Employees existing = repo.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Çalışan bulunamadı: " + firstName + " " + lastName));

        if (incoming.getFirstName() != null) existing.setFirstName(incoming.getFirstName());
        if (incoming.getLastName() != null)  existing.setLastName(incoming.getLastName());

        return repo.save(existing);
    }

    @Override
    public void delete(String firstName, String lastName) {
        Employees existing = repo.findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Çalışan bulunamadı: " + firstName + " " + lastName));
        repo.delete(existing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Employees> list() {
        return repo.findAll();
    }
}
