package com.server.app.service.srvImpl;

import com.server.app.dto.response.EmployeeDto;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
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

    // CREATE
    @Override
    public String add(EmployeeSaveRequest request) {
        try {
            Employee employee = new Employee();
            BeanUtils.copyProperties(request, employee);
            repository.save(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultMessages.PROCESS_FAILED;
        }
        return ResultMessages.SUCCESS;
    }

    // UPDATE
    @Override
    public EmployeeDto update(EmployeeUpdateRequest request) {
        try {
            Optional<Employee> employeeOpt = repository.findEmployeeByEmployeeId(request.getEmployeeId());
            if (employeeOpt.isEmpty()) {
                throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
            }
            Employee employee = employeeOpt.get();
            BeanUtils.copyProperties(request, employee);
            repository.save(employee);
            return employeeToEmployeeDtoMapper(employee);
        } catch (BusinessException be) {
            throw be;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ResultMessages.PROCESS_FAILED);
        }
    }

    // READ (one)
    @Override
    public EmployeeDto findEmployeeByEmployeeId(Long employeeId) {
        Optional<Employee> employeeOpt = repository.findEmployeeByEmployeeId(employeeId);
        if (employeeOpt.isEmpty()) {
            throw new BusinessException(ResultMessages.RECORD_NOT_FOUND);
        }
        return employeeToEmployeeDtoMapper(employeeOpt.get());
    }

    // DELETE
    @Override
    public void deleteEmployeeByEmployeeId(Long employeeId) {
        repository.deleteEmployeeByEmployeeId(employeeId);
    }

    // EXISTS
    @Override
    public boolean existsByEmployeeId(Long employeeId) {
        return repository.existsEmployeeByEmployeeId(employeeId);
    }

    // READ (all)
    @Override
    public List<EmployeeDto> findAllEmployees() {
        List<Employee> list = repository.findAll();
        List<EmployeeDto> result = new ArrayList<>();
        for (Employee e : list) {
            result.add(employeeToEmployeeDtoMapper(e));
        }
        return result;
    }

    // OrderMapper'ın ihtiyaç duyduğu: ENTITY döndürür
    @Override
    public Employee getEmployee(Long employeeId) {
        return repository.getEmployeeByEmployeeId(employeeId);
        // Alternatif: return repository.findById(employeeId).orElse(null);
    }

    // ---- helper ----
    private EmployeeDto employeeToEmployeeDtoMapper(Employee e) {
        if (e == null) return null;
        EmployeeDto dto = new EmployeeDto();
        BeanUtils.copyProperties(e, dto);
        return dto;
    }
}
