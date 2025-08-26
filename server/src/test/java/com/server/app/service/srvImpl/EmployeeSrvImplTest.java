package com.server.app.service.srvImpl;

import com.server.app.dto.request.employee.EmployeeSaveRequest;
import com.server.app.dto.request.employee.EmployeeUpdateRequest;
import com.server.app.dto.response.EmployeeDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.EmployeeMapper;
import com.server.app.model.Employee;
import com.server.app.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeSrvImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeSrvImpl employeeSrv;

    EmployeeSaveRequest saveRequest = new EmployeeSaveRequest();
    EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setLastName("Yılmaz");
        saveRequest.setFirstName("Ahmet");

        updateRequest.setEmployeeId(1L);
        updateRequest.setLastName("Yıldız");
        updateRequest.setFirstName("Mehmet");

        employeeMapper = new EmployeeMapper(employeeRepository);
        employeeSrv = new EmployeeSrvImpl(employeeRepository, employeeMapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        employeeMapper = null;
        employeeSrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyFirstName() {
            saveRequest.setFirstName("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_NAME);
        }

        @Test
        void isEmptyLastName() {
            saveRequest.setLastName("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_SURNAME);
        }

        @Test
        void isInvalidBirthDate() {
            saveRequest.setBirthDate(LocalDate.of(2026, 5, 15));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.INVALID_BIRTHDATE);
        }

        @Test
        void isInvalidHireDate() {
            saveRequest.setBirthDate(LocalDate.of(1990, 5, 15));
            saveRequest.setHireDate(LocalDate.of(1989, 1, 10));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.HIRING_DATE_BEFORE_BIRTHDAY);
        }

        @Test
        void isNameAlreadyExists() {
            when(employeeRepository.existsByFirstNameAndLastName("Ahmet", "Yılmaz"))
                    .thenReturn(true);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.NAME_SURNAME_EXIST);
        }

        @Test
        void isInvalidPhoneFormat() {
            saveRequest.setHomePhone("5-55-5-5-5-555-5");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.WRONG_PHONE_FORMAT);
        }

        @Test
        void isTitleOutOfRange() {
            saveRequest.setTitle("abcabcabcabcabcabcabcabcabcabca");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.TITLE_OUT_OF_RANGE);
        }

        @Test
        void isSuccess() {
            saveRequest.setFirstName("Ahmet");
            saveRequest.setLastName("Yılmaz");

            when(employeeRepository.save(any(Employee.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = employeeSrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {

        @Test
        void isSuccess() {
            updateRequest.setFirstName("Mehmet");
            updateRequest.setLastName("Yıldız");

            when(employeeRepository.existsEmployeeByEmployeeId(updateRequest.getEmployeeId())).thenReturn(true);

            when(employeeRepository.save(any(Employee.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = employeeSrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findEmployeeById {

        @Test
        void isEmployeeNotFound() {
            when(employeeRepository.findEmployeeByEmployeeId(1L)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.findEmployeeByEmployeeId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.EMPLOYEE_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            Employee employee = new Employee();
            employee.setEmployeeId(1L);
            employee.setFirstName("Ahmet");
            employee.setLastName("Yılmaz");

            when(employeeRepository.findEmployeeByEmployeeId(1L)).thenReturn(Optional.of(employee));

            DataGenericResponse<EmployeeDto> response = employeeSrv.findEmployeeByEmployeeId(1L);

            assertThat(response).isNotNull();
            assertThat(response.getData().getFirstName()).isEqualTo("Ahmet");
            assertThat(response.getData().getLastName()).isEqualTo("Yılmaz");
        }
    }

    @Nested
    class delete {

        @Test
        void isEmployeeNotFound() {
            when(employeeRepository.existsEmployeeByEmployeeId(1L)).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> employeeSrv.deleteEmployeeByEmployeeId(1L)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.EMPLOYEE_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(employeeRepository.existsEmployeeByEmployeeId(1L)).thenReturn(true);

            GenericResponse response = employeeSrv.deleteEmployeeByEmployeeId(1L);

            assertThat(response).isNotNull();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllEmployees {

        @Test
        void isSuccess() {
            Employee emp1 = new Employee();
            emp1.setEmployeeId(1L);
            emp1.setFirstName("Ahmet");
            emp1.setLastName("Yılmaz");

            Employee emp2 = new Employee();
            emp2.setEmployeeId(2L);
            emp2.setFirstName("Mehmet");
            emp2.setLastName("Yıldız");

            when(employeeRepository.findAll()).thenReturn(List.of(emp1, emp2));
            DataGenericResponse<List<EmployeeDto>> response = employeeSrv.findAllEmployees();

            assertThat(response).isNotNull();
            assertThat(response.getData().size()).isEqualTo(2);
        }
    }

}