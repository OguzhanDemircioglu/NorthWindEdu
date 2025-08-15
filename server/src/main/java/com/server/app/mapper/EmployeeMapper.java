package com.server.app.mapper;

import com.server.app.enums.ResultMessages;
import com.server.app.dto.response.EmployeeDto;
import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.helper.BusinessException;
import com.server.app.model.Employee;
import com.server.app.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {
    private final EmployeeRepository repository;

    public EmployeeDto toDto(Employee request) {
        return EmployeeDto.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .title(request.getTitle())
                .titleOfCourtesy(request.getTitleOfCourtesy())
                .birthDate(request.getBirthDate())
                .hireDate(request.getHireDate())
                .address(request.getAddress())
                .city(request.getCity())
                .region(request.getRegion())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .homePhone(request.getHomePhone())
                .extension(request.getExtension())
                .photo(request.getPhoto())
                .notes(request.getNotes())
                .reportsTo(request.getReportsTo())
                .photoPath(request.getPhotoPath())
                .build();
    }

    public Employee toEntity(EmployeeUpdateRequest request) {
        Employee existingEmployee = repository.findEmployeeByEmployeeId(request.getEmployeeId())
                .orElseThrow(() -> new BusinessException(ResultMessages.RECORD_NOT_FOUND));

        return updateEntityFromRequest(request, existingEmployee);
    }

    private Employee updateEntityFromRequest(EmployeeUpdateRequest request, Employee existingEmployee) {
        return Employee.builder()
                .employeeId(existingEmployee.getEmployeeId())
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .title(request.getTitle())
                .titleOfCourtesy(request.getTitleOfCourtesy())
                .birthDate(request.getBirthDate())
                .hireDate(request.getHireDate())
                .address(request.getAddress())
                .city(request.getCity())
                .region(request.getRegion())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .homePhone(request.getHomePhone())
                .extension(request.getExtension())
                .photo(request.getPhoto())
                .notes(request.getNotes())
                .reportsTo(request.getReportsTo())
                .photoPath(request.getPhotoPath())
                .build();
    }

    public Employee saveEntityFromRequest(EmployeeSaveRequest request) {
        return Employee.builder()
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .title(request.getTitle())
                .titleOfCourtesy(request.getTitleOfCourtesy())
                .birthDate(request.getBirthDate())
                .hireDate(request.getHireDate())
                .address(request.getAddress())
                .city(request.getCity())
                .region(request.getRegion())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .homePhone(request.getHomePhone())
                .extension(request.getExtension())
                .photo(request.getPhoto())
                .notes(request.getNotes())
                .reportsTo(request.getReportsTo())
                .photoPath(request.getPhotoPath())
                .build();
    }
}