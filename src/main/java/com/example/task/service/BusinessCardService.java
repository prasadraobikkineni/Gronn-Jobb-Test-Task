package com.example.task.service;

import com.example.task.model.request.BusinessCardFilterRequest;
import com.example.task.model.response.BusinessCardListResult;
import com.example.task.model.response.BusinessCardResult;
import com.example.task.repository.BusinessCardElasticSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessCardService {

    private final BusinessCardElasticSearchRepository businessCardElasticSearchRepository;

    public BusinessCardResult getBusinessCardById(String id){
        return businessCardElasticSearchRepository.getById(id);
    }

    public BusinessCardListResult searchBusinessCardListByName(BusinessCardFilterRequest businessCardFilterRequest){
        return businessCardElasticSearchRepository.searchByName(businessCardFilterRequest);
    }

    public BusinessCardResult getBusinessCardByName(String name) {
        return businessCardElasticSearchRepository.getByName(name);
    }
}
