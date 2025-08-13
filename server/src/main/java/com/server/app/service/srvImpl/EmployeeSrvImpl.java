package com.server.app.service.srvImpl;

import com.server.app.dto.EmployeeDto;
import com.server.app.dto.request.EmployeeSaveRequest;
import com.server.app.dto.request.EmployeeUpdateRequest;
import com.server.app.model.Employee;
import com.server.app.repository.EmployeeRepository;
import com.server.app.service.EmployeeService;
import lombok.RequiredArgsConstructor;
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
            repository.save(
                    Employee.builder()
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
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    @Override
    public EmployeeDto update(EmployeeUpdateRequest request) {
        try {
            Optional<Employee> employee = repository.findEmployeeByEmployeeId(request.getEmployeeId());
            if (employee.isEmpty()) {
                throw new RuntimeException("Update Edilecek Kayıt bulunamadı");
            }

            employee.get().setLastName(request.getLastName());
            employee.get().setFirstName(request.getFirstName());
            employee.get().setTitle(request.getTitle());
            employee.get().setTitleOfCourtesy(request.getTitleOfCourtesy());
            employee.get().setBirthDate(request.getBirthDate());
            employee.get().setHireDate(request.getHireDate());
            employee.get().setAddress(request.getAddress());
            employee.get().setCity(request.getCity());
            employee.get().setRegion(request.getRegion());
            employee.get().setPostalCode(request.getPostalCode());
            employee.get().setCountry(request.getCountry());
            employee.get().setHomePhone(request.getHomePhone());
            employee.get().setExtension(request.getExtension());
            employee.get().setPhoto(request.getPhoto());
            employee.get().setNotes(request.getNotes());
            employee.get().setReportsTo(request.getReportsTo());
            employee.get().setPhotoPath(request.getPhotoPath());

            repository.save(employee.get());

            return employeeToEmployeeDtoMapper(employee.get());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("İşlem Başarısız");
        }
    }

    @Override
    public EmployeeDto findEmployeeByEmployeeId(Integer employeeId) {
        Optional<Employee> employee = repository.findEmployeeByEmployeeId(employeeId);
        if (employee.isEmpty()) {
            throw new RuntimeException("Kayıt bulunamadı");
        }

        return employeeToEmployeeDtoMapper(employee.get());
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
            EmployeeDto dto = employeeToEmployeeDtoMapper(e);
            result.add(dto);
        }

        return result;
    }

    private EmployeeDto employeeToEmployeeDtoMapper(Employee e) {
        if (e == null) {
            return null;
        }

        EmployeeDto dto = new EmployeeDto();
        dto.setEmployeeId(e.getEmployeeId());
        dto.setLastName(e.getLastName());
        dto.setFirstName(e.getFirstName());
        dto.setTitle(e.getTitle());
        dto.setTitleOfCourtesy(e.getTitleOfCourtesy());
        dto.setBirthDate(e.getBirthDate());
        dto.setHireDate(e.getHireDate());
        dto.setAddress(e.getAddress());
        dto.setCity(e.getCity());
        dto.setRegion(e.getRegion());
        dto.setPostalCode(e.getPostalCode());
        dto.setCountry(e.getCountry());
        dto.setHomePhone(e.getHomePhone());
        dto.setExtension(e.getExtension());
        dto.setNotes(e.getNotes());
        dto.setReportsTo(e.getReportsTo());
        dto.setPhotoPath(e.getPhotoPath());
        dto.setPhoto(e.getPhoto());

        return dto;
    }
}
