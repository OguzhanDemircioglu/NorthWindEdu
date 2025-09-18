package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.response.EmployeeDto;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.EmployeeMapper;
import com.server.app.model.Employee;
import com.server.app.repository.EmployeeRepository;
import com.server.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeSrvImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    @Override
    public GenericResponse add(EmployeeSaveRequest request) {
        Employee employee = mapper.saveEntityFromRequest(request);

        BusinessRules.validate(
                checkEmployeeForGeneralValidations(employee),
                checkTitleValidation(employee.getTitle()),
                checkPhoneFormat(employee.getHomePhone()),
                checkUniqueConstraints(employee)
        );

        repository.save(employee);
        return new GenericResponse();
    }

    @Override
    public GenericResponse update(EmployeeUpdateRequest request) {
        Employee employee = mapper.toEntity(request);

        BusinessRules.validate(
                checkEmployeeForGeneralValidations(employee),
                checkTitleValidation(employee.getTitle()),
                checkPhoneFormat(employee.getHomePhone()),
                checkUniqueConstraints(employee)
        );

        repository.save(employee);
        return GenericResponse.builder()
                .message(ResultMessages.RECORD_UPDATED)
                .build();
    }

    @Override
    public DataGenericResponse<EmployeeDto> findEmployeeByEmployeeId(Long employeeId) {
        Optional<Employee> employee = repository.findEmployeeByEmployeeId(employeeId);
        if (employee.isEmpty()) {
            throw new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND);
        }

        EmployeeDto dto = mapper.toDto(employee.get());
        return DataGenericResponse.<EmployeeDto>dataBuilder()
                .data(dto)
                .build();
    }

    @Override
    public GenericResponse deleteEmployeeByEmployeeId(Long employeeId) {
        boolean exists = repository.existsEmployeeByEmployeeId(employeeId);
        if (!exists) {
            throw new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND);
        }
        repository.deleteEmployeeByEmployeeId(employeeId);
        if (repository.count() == 0) {
            repository.resetEmployeeSequence();
        }
        return GenericResponse.builder()
                .message(ResultMessages.RECORD_DELETED)
                .build();
    }

    @Override
    public DataGenericResponse<List<EmployeeDto>> findAllEmployees() {
        List<EmployeeDto> dtos = repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();

        return DataGenericResponse.<List<EmployeeDto>>dataBuilder()
                .data(dtos)
                .build();
    }

    @Override
    public Employee getEmployee(Long employeeId) {
        Employee employee = repository.getEmployeeByEmployeeId(employeeId);
        if (Objects.isNull(employee)) {
            throw new BusinessException(ResultMessages.EMPLOYEE_NOT_FOUND);
        }
        return employee;
    }

    // ===== Helpers =====
    private String checkTitleValidation(String title) {
        if (!Strings.isNullOrEmpty(title) && title.length() > 30) {
            return ResultMessages.TITLE_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkPhoneFormat(String phone) {
        if (phone != null && !phone.matches("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$")) {
            return ResultMessages.WRONG_PHONE_FORMAT;
        }
        return null;
    }

    private String checkUniqueConstraints(Employee request) {
        if (repository.existsByFirstNameAndLastName(request.getFirstName(), request.getLastName())) {
            return ResultMessages.NAME_SURNAME_EXIST;
        }
        return null;
    }

    private String checkEmployeeForGeneralValidations(Employee request) {
        if (Strings.isNullOrEmpty(request.getFirstName())) {
            return ResultMessages.EMPTY_NAME;
        }
        if (Strings.isNullOrEmpty(request.getLastName())) {
            return ResultMessages.EMPTY_SURNAME;
        }
        if (request.getBirthDate() != null && request.getBirthDate().isAfter(LocalDate.now())) {
            return ResultMessages.INVALID_BIRTHDATE;
        }
        if (request.getHireDate() != null && request.getBirthDate() != null
                && request.getHireDate().isBefore(request.getBirthDate())) {
            return ResultMessages.HIRING_DATE_BEFORE_BIRTHDAY;
        }
        return null;
    }
}
