package com.server.app.service.srvImpl;

import com.server.app.dto.response.EmployeeDto;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.model.Employee;
import com.server.app.repository.EmployeeRepository;
import com.server.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeSrvImpl implements EmployeeService {

    private final EmployeeRepository repository;


    @Override
    public String add(EmployeeSaveRequest request) {
        try {
            Employee employee = new Employee();
            BeanUtils.copyProperties(request, employee);
            repository.save(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }


    @Override
    public EmployeeDto update(EmployeeUpdateRequest request) {
        try {
            Optional<Employee> employeeOpt = repository.findEmployeeByEmployeeId(request.getEmployeeId());
            if (employeeOpt.isEmpty()) {
                throw new RuntimeException("Update Edilecek Kayıt Bulunamadı");
            }
            Employee employee = employeeOpt.get();
            BeanUtils.copyProperties(request, employee);
            repository.save(employee);
            return employeeToEmployeeDtoMapper(employee);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("İşlem Başarısız");
        }
    }

    @Override
    public EmployeeDto findEmployeeByEmployeeId(Long employeeId) {
        Optional<Employee> employeeOpt = repository.findEmployeeByEmployeeId(employeeId);
        if (employeeOpt.isEmpty()) {
            throw new RuntimeException("Kayıt Bulunamadı");
        }
        return employeeToEmployeeDtoMapper(employeeOpt.get());
    }


    @Override
    public void deleteEmployeeByEmployeeId(Long employeeId) {
        repository.deleteEmployeeByEmployeeId(employeeId);
    }


    @Override
    public boolean existsByEmployeeId(Long employeeId) {
        return repository.existsEmployeeByEmployeeId(employeeId);
        // alternatif: return repository.existsById(employeeId);
    }

    @Override
    public List<EmployeeDto> findAllEmployees() {
        List<Employee> list = repository.findAll();
        List<EmployeeDto> result = new ArrayList<>();
        for (Employee e : list) {
            result.add(employeeToEmployeeDtoMapper(e));
        }
        return result;
    }

    @Override
    public Employee getEmployee(Long employeeId) {
        return repository.getEmployeeByEmployeeId(employeeId);
        // alternatif: return repository.findById(employeeId).orElse(null);
    }

    private EmployeeDto employeeToEmployeeDtoMapper(Employee e) {
        if (e == null) return null;
        EmployeeDto dto = new EmployeeDto();
        BeanUtils.copyProperties(e, dto);
        return dto;
    }
}
