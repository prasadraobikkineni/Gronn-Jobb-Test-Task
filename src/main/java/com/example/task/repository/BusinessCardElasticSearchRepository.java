package com.example.task.repository;

import com.example.task.exception.BusinessCardException;
import com.example.task.model.request.BusinessCardFilterRequest;
import com.example.task.model.response.BusinessCardListResult;
import com.example.task.model.response.BusinessCardResult;
import com.example.task.service.elastic.BusinessCardSearchByIdQueryBuilder;
import com.example.task.service.elastic.BusinessCardSearchByNameQueryBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BusinessCardElasticSearchRepository {

    private static final String INDEX = "business_card";

    private final RestHighLevelClient elasticSearchClient;
    private final BusinessCardSearchByIdQueryBuilder businessCardSearchByIdQueryBuilder;
    private final BusinessCardSearchByNameQueryBuilder businessCardSearchByNameQueryBuilder;
    private final BusinessCardJsonMapper businessCardJsonMapper;

    public BusinessCardResult getById(String id) {
        SearchRequest searchRequest = createSearchRequestForGetById(id);
        SearchResponse searchResponse = execute(searchRequest);
        return createBusinessCardResult(searchResponse);
    }

    public BusinessCardResult getByName(String name) {
        SearchRequest searchRequest = createSearchRequestForGetByName(name);
        SearchResponse searchResponse = execute(searchRequest);
        return createBusinessCardResult(searchResponse);
    }

    public BusinessCardListResult searchByName(BusinessCardFilterRequest businessCardFilterRequest) {
        SearchRequest searchRequest = createSearchRequestForSearchByName(businessCardFilterRequest);
        SearchResponse searchResponse = execute(searchRequest);
        return createBusinessCardListResult(searchResponse, businessCardFilterRequest);
    }

    private SearchRequest createSearchRequestForGetById(String id) {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder
                .searchSource()
                .query(businessCardSearchByIdQueryBuilder.build(id))
                .fetchSource(true)
                .trackTotalHits(true);
        return new SearchRequest(new String[]{INDEX}, searchSourceBuilder);
    }

    private SearchRequest createSearchRequestForGetByName(String name) {
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder
                .searchSource()
                .query(businessCardSearchByNameQueryBuilder.buildForGet(name))
                .fetchSource(true)
                .trackTotalHits(true);
        return new SearchRequest(new String[]{INDEX}, searchSourceBuilder);
    }

    private SearchRequest createSearchRequestForSearchByName(BusinessCardFilterRequest request) {
        Integer size = request.getSize();
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder
                .searchSource()
                .query(businessCardSearchByNameQueryBuilder.buildForSearch(request.getSearchName()))
                .fetchSource(true)
                .size(size)
                .from(size * (request.getPage() - 1))
                .trackTotalHits(true);
        return new SearchRequest(new String[]{INDEX}, searchSourceBuilder);
    }

    private SearchResponse execute(SearchRequest searchRequest) {
        try {
            return elasticSearchClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new BusinessCardException("Error occurred on payment search.", e);
        }
    }

    private BusinessCardResult createBusinessCardResult(SearchResponse searchResponse) {
        BusinessCardResult businessCardResult = new BusinessCardResult();
        businessCardResult.setTotalElements(totalHits(searchResponse));
        businessCardResult.setBusinessCard(businessCardJsonMapper.toBusinessCard(searchResponse.getHits().getHits()));
        return businessCardResult;
    }

    private BusinessCardListResult createBusinessCardListResult(SearchResponse searchResponse, BusinessCardFilterRequest businessCardFilterRequest) {
        BusinessCardListResult businessCardListResult = new BusinessCardListResult();
        businessCardListResult.setTotalElements(totalHits(searchResponse));
        businessCardListResult.setPage(businessCardFilterRequest.getPage());
        businessCardListResult.setSize(businessCardFilterRequest.getSize());
        businessCardListResult.setBusinessCardList(businessCardJsonMapper.toBusinessCardList(searchResponse.getHits().getHits()));
        return businessCardListResult;
    }

    private long totalHits(SearchResponse searchResponse) {
        TotalHits totalHits = searchResponse.getHits().getTotalHits();
        if (totalHits == null) {
            return 0L;
        }
        return totalHits.value;
    }
}
