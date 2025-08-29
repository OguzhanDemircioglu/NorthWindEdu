package com.server.app.service.srvImpl;

import com.server.app.dto.request.customerDemographics.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.customerDemographics.CustomerDemographicsUpdateRequest;
import com.server.app.dto.response.CustomerDemographicsDto;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.DataGenericResponse;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.CustomerDemographicsMapper;
import com.server.app.model.CustomerDemographics;
import com.server.app.repository.CustomerDemographicsRepository;
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
class CustomerDemographicsSrvImplTest {

    @Mock
    private CustomerDemographicsRepository repository;

    @Mock
    private CustomerDemographicsMapper mapper;

    @InjectMocks
    private CustomerDemographicsSrvImpl customerDemographicsSrv;

    CustomerDemographicsSaveRequest saveRequest = new CustomerDemographicsSaveRequest();
    CustomerDemographicsUpdateRequest updateRequest = new CustomerDemographicsUpdateRequest();

    @BeforeEach
    void setUp() {
        saveRequest.setCustomerTypeId("1");
        saveRequest.setCustomerDesc("Old");

        updateRequest.setCustomerTypeId("1");
        updateRequest.setCustomerDesc("Updated");

        mapper = new CustomerDemographicsMapper(repository);
        customerDemographicsSrv = new CustomerDemographicsSrvImpl(repository, mapper);
    }

    @AfterEach
    void tearDown() {
        saveRequest = null;
        updateRequest = null;
        customerDemographicsSrv = null;
        mapper = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyId() {
            saveRequest.setCustomerTypeId("");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerDemographicsSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_CUSTOMER_TYPE_ID);
        }

        void isInvalidId() {
            saveRequest.setCustomerTypeId("12");

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerDemographicsSrv.add(saveRequest)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.ID_IS_NOT_DELIVERED);
        }

        @Test
        void isSuccess() {
            when(repository.save(any(CustomerDemographics.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = customerDemographicsSrv.add(saveRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }

    @Nested
    class update {
        @Test
        void isSuccess() {
            when(repository.existsCustomerDemographicsByCustomerTypeId(updateRequest.getCustomerTypeId())).thenReturn(true);

            when(repository.save(any(CustomerDemographics.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = customerDemographicsSrv.update(updateRequest);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_UPDATED);
        }
    }

    @Nested
    class findCustomerDemographicById {
        @Test
        void isCustomerDemographicNotFound() {
            when(repository.findCustomerDemographicsByCustomerTypeId("1")).thenReturn(Optional.empty());

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerDemographicsSrv.findCustomerDemographicsByCustomerTypeId("1")
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.CUSTOMER_DEMOGRAPHICS_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            CustomerDemographics customerDemographic = new CustomerDemographics();
            customerDemographic.setCustomerTypeId("1");
            customerDemographic.setCustomerDesc("Old");

            when(repository.findCustomerDemographicsByCustomerTypeId(customerDemographic.getCustomerTypeId())).thenReturn(Optional.of(customerDemographic));

            DataGenericResponse<CustomerDemographicsDto> response = customerDemographicsSrv.findCustomerDemographicsByCustomerTypeId(customerDemographic.getCustomerTypeId());

            assertThat(response).isNotNull();
            assertThat(response.getData().getCustomerDesc()).isEqualTo("Old");
        }
    }

    @Nested
    class delete {

        @Test
        void isCustomerDemographicNotFound() {
            when(repository.existsCustomerDemographicsByCustomerTypeId("1")).thenReturn(false);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> customerDemographicsSrv.deleteCustomerDemographicsByCustomerTypeId("1")
            );

            assertThat(exception.getMessage()).isEqualTo(ResultMessages.RECORD_NOT_FOUND);
        }

        @Test
        void isSuccess() {
            when(repository.existsCustomerDemographicsByCustomerTypeId("1")).thenReturn(true);

            GenericResponse response = customerDemographicsSrv.deleteCustomerDemographicsByCustomerTypeId("1");

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.RECORD_DELETED);
        }
    }

    @Nested
    class findAllCustomerDemographics {

        @Test
        void isSuccess() {
            CustomerDemographics customerDemographic1 = new CustomerDemographics();
            customerDemographic1.setCustomerTypeId("1");

            CustomerDemographics customerDemographic2 = new CustomerDemographics();
            customerDemographic2.setCustomerTypeId("2");

            when(repository.findAll()).thenReturn(List.of(customerDemographic1, customerDemographic2));

            DataGenericResponse<List<CustomerDemographicsDto>> response = customerDemographicsSrv.findAllCustomerDemographics();

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getData()).size().isEqualTo(2);
        }
    }

}