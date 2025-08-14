package com.server.app.service.srvImpl;

import com.server.app.dto.CustomerDemographicsDto;
import com.server.app.dto.request.CustomerDemographicsSaveRequest;
import com.server.app.dto.request.CustomerDemographicsUpdateRequest;
import com.server.app.model.CustomerDemographics;
import com.server.app.repository.CustomerDemographicsRepository;
import com.server.app.service.CustomerDemographicsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerDemographicsSrvImpl implements CustomerDemographicsService {

    private final CustomerDemographicsRepository repository;

    @Override
    public String add(CustomerDemographicsSaveRequest request) {
        try {
            repository.save(
                    CustomerDemographics.builder()
                            .customerTypeId(request.getCustomerTypeId())
                            .customerDesc(request.getCustomerDesc())
                            .build()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return "İşlem Başarısız";
        }
        return "İşlem Başarılı";
    }

    @Override
    public CustomerDemographicsDto update(CustomerDemographicsUpdateRequest request) {
        CustomerDemographicsDto result = new CustomerDemographicsDto();

        Optional<CustomerDemographics> cdOpt =
                repository.findCustomerDemographicsByCustomerTypeId(request.getCustomerTypeId());
        if (cdOpt.isEmpty()) {
            throw new RuntimeException("Update Edilecek Kayıt bulunamadı");
        }

        CustomerDemographics cd = cdOpt.get();
        cd.setCustomerDesc(request.getCustomerDesc());

        // DTO map
        result.setCustomerTypeId(cd.getCustomerTypeId());
        result.setCustomerDesc(cd.getCustomerDesc());

        repository.save(cd);
        return result;
    }

    @Override
    public CustomerDemographicsDto findCustomerDemographicsByCustomerTypeId(String customerTypeId) {
        CustomerDemographicsDto result = new CustomerDemographicsDto();

        Optional<CustomerDemographics> cdOpt =
                repository.findCustomerDemographicsByCustomerTypeId(customerTypeId);
        if (cdOpt.isEmpty()) {
            throw new RuntimeException("Update Edilecek Kayıt bulunamadı");
        }

        CustomerDemographics cd = cdOpt.get();
        result.setCustomerTypeId(cd.getCustomerTypeId());
        result.setCustomerDesc(cd.getCustomerDesc());

        return result;
    }

    @Override
    public void deleteCustomerDemographicsByCustomerTypeId(String customerTypeId) {
        repository.deleteCustomerDemographicsByCustomerTypeId(customerTypeId);
    }

    @Override
    public List<CustomerDemographicsDto> findAllCustomerDemographics() {
        List<CustomerDemographics> list = repository.findAll();
        List<CustomerDemographicsDto> result = new ArrayList<>();

        for (CustomerDemographics cd : list) {
            CustomerDemographicsDto dto = new CustomerDemographicsDto();
            dto.setCustomerTypeId(cd.getCustomerTypeId());
            dto.setCustomerDesc(cd.getCustomerDesc());
            result.add(dto);
        }

        return result;
    }
}
