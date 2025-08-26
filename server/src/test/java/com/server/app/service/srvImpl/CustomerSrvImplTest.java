package com.server.app.service.srvImpl;

import com.server.app.dto.request.customer.CustomerSaveRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CustomerMapper;
import com.server.app.model.Customer;
import com.server.app.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerSrvImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerSrvImpl customerSrv;

    CustomerSaveRequest request = new CustomerSaveRequest();

    @BeforeEach
    void setUp() {
        request = new CustomerSaveRequest();
        request.setCustomerId("CUSTOMERID");
        request.setCompanyName("KAREL");
        request.setContactName("ahmet");
        request.setContactTitle("Sales");
        request.setAddress("adress");
        request.setCity("Ankara");
        request.setRegion("A");
        request.setPostalCode("60");
        request.setCountry("Turkiye");
        request.setPhone("0149218491");
        request.setFax("030-0076545");

        customerMapper = new CustomerMapper(customerRepository);
        customerSrv = new CustomerSrvImpl(customerRepository, customerMapper);
    }

    @AfterEach
    void tearDown() {
        request = null;
        customerMapper = null;
        customerSrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyCompanyName() {
            request.setCompanyName(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerSrv.add(request)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_COMPANY_NAME);
        }

        @Test
        void isIdNotDelivered() {
            request.setCustomerId(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerSrv.add(request)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        @Test
        void isWrongPhoneFormat() {
            request.setPhone("abcde");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerSrv.add(request)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.WRONG_PHONE_FORMAT);
        }

        @Test
        void isSuccess() {
            request.setPhone(null);
            request.setFax(null);

            when(customerRepository.save(any(Customer.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = customerSrv.add(request);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }
}
