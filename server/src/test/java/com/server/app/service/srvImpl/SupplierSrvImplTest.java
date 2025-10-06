package com.server.app.service.srvImpl;

import com.server.app.dto.request.supplier.SupplierSaveRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.SupplierMapper;
import com.server.app.model.Supplier;
import com.server.app.repository.SupplierRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SupplierSrvImplTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierSrvImpl supplierSrv;

    SupplierSaveRequest request;

    @BeforeEach
    void setUp() {
        request = new SupplierSaveRequest();
        request.setCompanyName("karel");
        request.setContactName("contact name");
        request.setContactTitle("Supplier");
        request.setAddress("Adress");
        request.setCity("Tokat");
        request.setRegion("NA");
        request.setPostalCode("646464");
        request.setCountry("Turkey");
        request.setPhone(null);
        request.setFax(null);
        request.setHomepage("supplierHomePage..//");


    }

    @AfterEach
    void tearDown() {
        request = null;
        supplierMapper = null;
        supplierSrv = null;
    }

    @Nested
    class add {

        @Test
        void isEmptyCompanyName() {
            request.setCompanyName(null);
            when(supplierMapper.saveEntityFromRequest(any(SupplierSaveRequest.class)))
                    .thenReturn(Supplier.builder()
                            .supplierId(1L)
                            .companyName(null)
                            .build());

            BusinessException ex = assertThrows(
                    BusinessException.class,
                    () -> supplierSrv.add(request)
            );
            assertThat(ex.getMessage()).isEqualTo(ResultMessages.EMPTY_COMPANY_NAME);
        }

        @Test
        void isWrongPhoneFormat() {
            when(supplierMapper.saveEntityFromRequest(any(SupplierSaveRequest.class)))
                    .thenReturn(Supplier.builder()
                            .supplierId(1L)
                            .companyName("karel")
                            .build());

            request.setPhone("adqehhvlqhelq");

            BusinessException ex = assertThrows(
                    BusinessException.class,
                    () -> supplierSrv.add(request)
            );
            assertThat(ex.getMessage()).isEqualTo(ResultMessages.WRONG_PHONE_FORMAT);
        }

        @Test
        void isSuccess() {
            when(supplierMapper.saveEntityFromRequest(any(SupplierSaveRequest.class)))
                    .thenReturn(Supplier.builder()
                            .supplierId(1L)
                            .companyName("karel")
                            .build());

            when(supplierRepository.save(any(Supplier.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse resp = supplierSrv.add(request);

            assertThat(resp).isNotNull();
            assertThat(resp.isSuccess()).isTrue();
            assertThat(resp.getMessage()).isEqualTo(ResultMessages.SUCCESS);

            verify(supplierRepository, times(1)).save(any(Supplier.class));
        }
    }
}
