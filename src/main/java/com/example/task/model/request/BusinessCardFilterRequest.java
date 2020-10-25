package com.example.task.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessCardFilterRequest {

    private Integer page = 1;
    private Integer size = 10;
    private String searchName;
}
