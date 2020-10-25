package com.example.task.model.response;

import com.example.task.domain.BusinessCard;
import com.example.task.model.enums.SortDirection;
import com.example.task.model.enums.SortField;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BusinessCardListResult {

    private Long totalElements;
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private SortField sortField;
    private SortDirection sortDirection;
    private List<BusinessCard> businessCardList;
}
