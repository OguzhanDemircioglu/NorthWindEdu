package com.server.app.service.srvImpl;

import com.server.app.dto.request.customerDemo.CustomerDemoSaveRequest;
import com.server.app.dto.request.customerDemo.CustomerDemoUpdateRequest;
import com.server.app.dto.response.CustomerDemoDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CustomerDemoMapper;
import com.server.app.model.Customer;
import com.server.app.model.CustomerDemo;
import com.server.app.model.CustomerDemographics;
import com.server.app.model.embedded.CustomerDemoId;
import com.server.app.repository.CustomerDemoRepository;
import com.server.app.service.CustomerDemographicsService;
import com.server.app.service.CustomerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerDemoSrvImplTest {
    @Mock
    private CustomerDemoRepository customerDemoRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerDemographicsService customerDemographicsService;

    @InjectMocks
    private CustomerDemoMapper customerDemoMapper;

    @InjectMocks
    private CustomerDemoSrvImpl customerDemoSrv;

    CustomerDemoSaveRequest saveRequest = new CustomerDemoSaveRequest();
    CustomerDemoUpdateRequest updateRequest = new CustomerDemoUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setCustomerId("Cus001");
        saveRequest.setCustomerTypeId("Type 1");

        updateRequest.setCustomerId("Cus002");
        updateRequest.setCustomerTypeId("Type 2");

        customerDemoMapper = new CustomerDemoMapper(customerDemoRepository, customerService, customerDemographicsService);
        customerDemoSrv = new CustomerDemoSrvImpl(customerDemoRepository, customerDemoMapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest =  null;
        updateRequest = null;
        customerDemoMapper = null;
        customerDemoSrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyCustomerId() {
            saveRequest.setCustomerId("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerDemoSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_CUSTOMER_ID);
        }

        @Test
        void isEmptyCustomerTypeId() {
            saveRequest.setCustomerTypeId("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerDemoSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_CUSTOMER_TYPE_ID);
        }

        @Test
        void isSuccess() {
            Customer customer = new Customer();
            customer.setCustomerId("Cus001");

            CustomerDemographics customerDemographic = new CustomerDemographics();
            customerDemographic.setCustomerTypeId("Type 1");

            when(customerService.getCustomer("Cus001")).thenReturn(customer);
            when(customerDemographicsService.getCustomerDemographics("Type 1")).thenReturn(customerDemographic);
            when(customerDemoRepository.save(any(CustomerDemo.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = customerDemoSrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {

        @Test
        void isSuccess() {
            when(customerDemoRepository.existsByCustomerDemoId_CustomerIdAndCustomerDemoId_CustomerTypeId(updateRequest.getCustomerId(), updateRequest.getCustomerTypeId())).thenReturn(true);

            when(customerDemoRepository.save(any(CustomerDemo.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = customerDemoSrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findCustomerDemoById {
        @Test
        void isCustomerDemoNotFound() {
            CustomerDemoId customerDemoId = new CustomerDemoId("Cus001", "Type 1");
            when(customerDemoRepository.findCustomerCustomerDemoByCustomerDemoId(customerDemoId)).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerDemoSrv.findCustomerDemoByCustomerDemoId(customerDemoId)
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            Customer customer = new Customer();
            customer.setCustomerId("Cus001");

            CustomerDemographics customerDemographic = new CustomerDemographics();
            customerDemographic.setCustomerTypeId("Type 1");

            CustomerDemoId customerDemoId = new CustomerDemoId("Cus001", "Type 1");

            CustomerDemo customerDemo = new CustomerDemo();
            customerDemo.setCustomer(customer);
            customerDemo.setCustomerDemographics(customerDemographic);
            customerDemo.setCustomerDemoId(customerDemoId);

            when(customerDemoRepository.findCustomerCustomerDemoByCustomerDemoId(customerDemoId)).thenReturn(Optional.of(customerDemo));

            DataGenericResponse<CustomerDemoDto> response = customerDemoSrv.findCustomerDemoByCustomerDemoId(customerDemoId);

            assertThat(response).isNotNull();
            assertThat(response.getData().getCustomerId()).isEqualTo("Cus001");
            assertThat(response.getData().getCustomerTypeId()).isEqualTo("Type 1");
        }
    }

    @Nested
    class delete {
        @Test
        void isCustomerDemoNotFound() {
            when(customerDemoRepository.existsByCustomerDemoId_CustomerIdAndCustomerDemoId_CustomerTypeId("Cus001", "Type 1")).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerDemoSrv.deleteCustomerDemoByCustomerDemoId(new CustomerDemoId("Cus001", "Type 1"))
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(customerDemoRepository.existsByCustomerDemoId_CustomerIdAndCustomerDemoId_CustomerTypeId("Cus001", "Type 1")).thenReturn(true);

            GenericResponse response = customerDemoSrv.deleteCustomerDemoByCustomerDemoId(new CustomerDemoId("Cus001", "Type 1"));

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllCustomerDemos {
        @Test
        void isSuccess() {
            CustomerDemo  customerDemo1 = new CustomerDemo();

            CustomerDemo customerDemo2 = new CustomerDemo();

            when(customerDemoRepository.findAll()).thenReturn(List.of(customerDemo1, customerDemo2));

            DataGenericResponse<List<CustomerDemoDto>> response = customerDemoSrv.findAllCustomerDemos();

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData().size()).isEqualTo(2);
        }
    }
}