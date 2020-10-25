package com.example.task.controller;

import com.example.task.domain.BusinessCard;
import com.example.task.model.enums.SortDirection;
import com.example.task.model.enums.SortField;
import com.example.task.model.request.BusinessCardFilterRequest;
import com.example.task.model.response.BusinessCardListResult;
import com.example.task.model.response.BusinessCardResult;
import com.example.task.service.BusinessCardService;
import com.example.task.service.IndexDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BusinessCardController.class)
public class BusinessCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BusinessCardService businessCardService;

    @MockBean
    private IndexDataService indexDataService;

    @Test
    public void it_should_get_business_card_by_id() throws Exception {
        //given

        BusinessCard businessCard = new BusinessCard();
        businessCard.setBusinessCardId("id");

        BusinessCardListResult businessCardListResult = new BusinessCardListResult();
        businessCardListResult.setBusinessCardList(Collections.singletonList(businessCard));
        businessCardListResult.setSortField(SortField.DATE);
        businessCardListResult.setSortDirection(SortDirection.DESC);
        businessCardListResult.setTotalElements(100L);
        businessCardListResult.setPage(1);
        businessCardListResult.setSize(10);
        businessCardListResult.setTotalPages(3);

        BusinessCardResult businessCardResult = new BusinessCardResult();

        given(businessCardService.getBusinessCardById("id")).willReturn(businessCardResult);

        //when
        ResultActions result = mockMvc.perform(get("/business-card-list/get-by-id/id"));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    public void it_should_get_business_card_by_name() throws Exception {
        //given

        BusinessCard businessCard = new BusinessCard();
        businessCard.setBusinessCardId("id");

        BusinessCardListResult businessCardListResult = new BusinessCardListResult();
        businessCardListResult.setBusinessCardList(Collections.singletonList(businessCard));
        businessCardListResult.setSortField(SortField.DATE);
        businessCardListResult.setSortDirection(SortDirection.DESC);
        businessCardListResult.setTotalElements(100L);
        businessCardListResult.setPage(1);
        businessCardListResult.setSize(10);
        businessCardListResult.setTotalPages(3);

        BusinessCardResult businessCardResult = new BusinessCardResult();

        given(businessCardService.getBusinessCardByName("name")).willReturn(businessCardResult);

        //when
        ResultActions result = mockMvc.perform(get("/business-card-list/get-by-name/name"));

        //then
        result.andExpect(status().isOk());
    }

    @Test
    public void it_should_search_business_card_by_name() throws Exception {
        //given
        BusinessCard businessCard = new BusinessCard();
        businessCard.setBusinessCardId("id");

        BusinessCardListResult businessCardListResult = new BusinessCardListResult();
        businessCardListResult.setBusinessCardList(Collections.singletonList(businessCard));
        businessCardListResult.setSortField(SortField.DATE);
        businessCardListResult.setSortDirection(SortDirection.DESC);
        businessCardListResult.setTotalElements(100L);
        businessCardListResult.setPage(1);
        businessCardListResult.setSize(10);
        businessCardListResult.setTotalPages(3);

        given(businessCardService.searchBusinessCardListByName(any(BusinessCardFilterRequest.class))).willReturn(businessCardListResult);

        //when
        ResultActions result = mockMvc.perform(get("/business-card-list/search-by-name")
                .param("sortField", "DATE")
                .param("page", "1")
                .param("size", "24")
                .param("searchName", "name"));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.businessCardList.[0].businessCardId").value("id"))
                .andExpect(jsonPath("$.totalElements").value(100))
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.sortField").value("DATE"))
                .andExpect(jsonPath("$.sortDirection").value("DESC"))
                .andExpect(jsonPath("$.totalPages").value(3));

        ArgumentCaptor<BusinessCardFilterRequest> businessCardFilterRequestArgumentCaptor = ArgumentCaptor.forClass(BusinessCardFilterRequest.class);
        verify(businessCardService).searchBusinessCardListByName(businessCardFilterRequestArgumentCaptor.capture());
        BusinessCardFilterRequest businessCardFilterRequestValue = businessCardFilterRequestArgumentCaptor.getValue();

        assertThat(businessCardFilterRequestValue.getSearchName()).isEqualTo("name");
    }
}