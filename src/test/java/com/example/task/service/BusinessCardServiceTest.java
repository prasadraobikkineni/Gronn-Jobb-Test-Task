package com.example.task.service;

import com.example.task.model.request.BusinessCardFilterRequest;
import com.example.task.model.response.BusinessCardListResult;
import com.example.task.model.response.BusinessCardResult;
import com.example.task.repository.BusinessCardElasticSearchRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class BusinessCardServiceTest {

    @InjectMocks
    private BusinessCardService businessCardService;

    @Mock
    private BusinessCardElasticSearchRepository businessCardElasticSearchRepository;

    @Test
    public void it_should_search_business_card_result_list_by_name() {
        //given
        BusinessCardFilterRequest businessCardFilterRequest = new BusinessCardFilterRequest();

        BusinessCardListResult businessCardListResult = new BusinessCardListResult();
        given(businessCardElasticSearchRepository.searchByName(businessCardFilterRequest)).willReturn(businessCardListResult);

        //when
        BusinessCardListResult result = businessCardService.searchBusinessCardListByName(businessCardFilterRequest);

        //then
        assertThat(result).isEqualTo(businessCardListResult);
    }

    @Test
    public void it_should_get_business_card_result_list_by_name() {
        //given
        BusinessCardResult businessCardResult = new BusinessCardResult();

        given(businessCardElasticSearchRepository.getByName("name")).willReturn(businessCardResult);

        //when
        BusinessCardResult result = businessCardService.getBusinessCardByName("name");

        //then
        assertThat(result).isEqualTo(businessCardResult);
    }

    @Test
    public void it_should_get_business_card_result_list_by_id() {
        //given
        BusinessCardResult businessCardResult = new BusinessCardResult();

        given(businessCardElasticSearchRepository.getById("id")).willReturn(businessCardResult);

        //when
        BusinessCardResult result = businessCardService.getBusinessCardById("id");

        //then
        assertThat(result).isEqualTo(businessCardResult);
    }
}