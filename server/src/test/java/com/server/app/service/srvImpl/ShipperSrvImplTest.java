package com.server.app.service.srvImpl;

import com.server.app.dto.request.shipper.ShipperSaveRequest;
import com.server.app.enums.ResultMessages;
import com.server.app.helper.BusinessException;
import com.server.app.helper.results.GenericResponse;
import com.server.app.mapper.ShipperMapper;
import com.server.app.model.Shipper;
import com.server.app.repository.ShipperRepository;
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
class ShipperSrvImplTest {

    @Mock
    private ShipperRepository shipperRepository;

    @InjectMocks
    private ShipperMapper shipperMapper;

    @InjectMocks
    private ShipperSrvImpl shipperSrv;

    ShipperSaveRequest request = new ShipperSaveRequest();

    @BeforeEach
    void setUp() {
        request = new ShipperSaveRequest();
        request.setCompanyName("karel");
        request.setPhone("1321313123");

        shipperMapper = new ShipperMapper(shipperRepository);
        shipperSrv = new ShipperSrvImpl(shipperRepository, shipperMapper);
    }

    @AfterEach
    void tearDown() {
        request = null;
        shipperMapper = null;
        shipperSrv = null;
    }
    @Nested
    class add {

        @Test
        void isEmptyName() {
            request.setCompanyName(null);

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> shipperSrv.add(request)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.EMPTY_NAME);
        }

        @Test
        void isCompanyNameOutOfRange() {
            request.setCompanyName("A".repeat(41));

            BusinessException exception = assertThrows(
                    BusinessException.class,
                    () -> shipperSrv.add(request)
            );

            assertThat(exception.getMessage())
                    .isEqualTo(ResultMessages.COMPANY_NAME_OUT_OF_RANGE);
        }
        @Test
        void isSuccess() {
            when(shipperRepository.save(any(Shipper.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            GenericResponse response = shipperSrv.add(request);

            assertThat(response).isNotNull();
            assertThat(response.isSuccess()).isTrue();
            assertThat(response.getMessage()).isEqualTo(ResultMessages.SUCCESS);
        }
    }
}
