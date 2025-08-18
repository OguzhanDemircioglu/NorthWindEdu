package com.server.app.service.srvImpl;

import com.google.common.base.Strings;
import com.server.app.dto.response.EmployeeDto;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.BusinessRules;
import com.server.app.mapper.EmployeeMapper;
import com.server.app.model.Employee;
import com.server.app.repository.EmployeeRepository;
import com.server.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeSrvImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    @Override
    public String add(EmployeeSaveRequest request) {
        try {
            Employee employee = mapper.saveEntityFromRequest(request);

            BusinessRules.validate(
                    checkEmployeeForGeneralValidations(employee),
                    checkTitleValidation(employee.getTitle()),
                    checkPhoneFormat(employee.getHomePhone()),
                    checkUniqueConstraints(employee)
            );

            repository.save(employee);
        } catch (BusinessException e) {
            log.error("Business validation failed for employee add: {}", request.getFirstName() +" "+ request.getLastName(), e);
            throw e;
        } catch (Exception e) {
            return ResultMessages.PROCESS_FAILED;
        }
        return ResultMessages.SUCCESS;
    }

    @Override
    public EmployeeDto update(EmployeeUpdateRequest request) {
        try {
            Employee employee = mapper.toEntity(request);

            BusinessRules.validate(
                    checkEmployeeForGeneralValidations(employee),
                    checkTitleValidation(employee.getTitle()),
                    checkPhoneFormat(employee.getHomePhone()),
                    checkUniqueConstraints(employee)
            );

            Employee updatedEmployee = repository.save(employee);

            return mapper.toDto(updatedEmployee);

        } catch (BusinessException e) {
            log.error("Business validation failed for employee update: {}", request.getEmployeeId(), e);
            throw e;
        } catch (Exception e) {
            log.error("Employee update failed for ID: {}", request.getEmployeeId(), e);
            throw new BusinessException(ResultMessages.PROCESS_FAILED + ": " + e.getMessage());
        }
    }

    @Override
    public EmployeeDto findEmployeeByEmployeeId(Integer employeeId) {
        Optional<Employee> employee = repository.findEmployeeByEmployeeId(employeeId);
        if (employee.isEmpty()) {
            throw new RuntimeException(ResultMessages.RECORD_NOT_FOUND);
        }

        return mapper.toDto(employee.get());
    }

    @Override
    public void deleteEmployeeByEmployeeId(Integer employeeId) {
        repository.deleteEmployeeByEmployeeId(employeeId);
    }

    @Override
    public List<EmployeeDto> findAllEmployees() {
        List<Employee> list = repository.findAll();
        List<EmployeeDto> result = new ArrayList<>();

        for (Employee e : list) {
            EmployeeDto dto = mapper.toDto(e);;
            result.add(dto);
        }

        return result;
    }

    private String checkTitleValidation(String title) {
        if(!Strings.isNullOrEmpty(title) && title.length() > 30) {
            return ResultMessages.TITLE_OUT_OF_RANGE;
        }
        return null;
    }

    private String checkPhoneFormat(String phone) {
        if(phone != null && !phone.matches("^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$")) {
            return ResultMessages.WRONG_PHONE_FORMAT;
        }
        return null;
    }

    private String checkUniqueConstraints(Employee request) {
        if (repository.existsByFirstNameAndLastName(
                request.getFirstName(),
                request.getLastName())) {
            return ResultMessages.NAME_SURNAME_EXIST;
        }
        return null;
    }

    private String checkEmployeeForGeneralValidations(Employee request) {

        if(Strings.isNullOrEmpty(request.getFirstName())) {
            return ResultMessages.EMPTY_NAME;
        }

        if(Strings.isNullOrEmpty(request.getLastName())) {
            return ResultMessages.EMPTY_SURNAME;
        }

        if(request.getBirthDate() != null && request.getBirthDate().isAfter(LocalDate.now())) {
            return ResultMessages.INVALID_BIRTHDATE;
        }

        if(request.getHireDate() != null && request.getBirthDate() != null
                && request.getHireDate().isBefore(request.getBirthDate())) {
            return ResultMessages.HIRING_DATE_BEFORE_BIRTHDAY;
        }

        return null;
    }
    @Override
    public boolean existsByEmployeeId(Integer employeeId) {
        return repository.existsById(employeeId);
    }

}
