package com.example.task.model.response;

import com.example.task.domain.BusinessCard;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessCardResult {

    private Long totalElements;
    private BusinessCard businessCard;
}
