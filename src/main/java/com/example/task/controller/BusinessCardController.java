package com.example.task.controller;

import com.example.task.model.request.BusinessCardFilterRequest;
import com.example.task.model.response.BusinessCardListResult;
import com.example.task.model.response.BusinessCardResult;
import com.example.task.service.BusinessCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/business-card-list")
public class BusinessCardController {

    private final BusinessCardService businessCardService;

    @GetMapping("/get-by-id/{id}")
    public BusinessCardResult getBusinessCardById(@PathVariable String id) {
        return businessCardService.getBusinessCardById(id);
    }

    @GetMapping("/search-by-name")
    public BusinessCardListResult searchBusinessCardByName(@Valid BusinessCardFilterRequest businessCardFilterRequest) {
        return businessCardService.searchBusinessCardListByName(businessCardFilterRequest);
    }

    @GetMapping("/get-by-name/{name}")
    public BusinessCardResult getBusinessCardByName(@PathVariable String name) {
        return businessCardService.getBusinessCardByName(name);
    }

}
