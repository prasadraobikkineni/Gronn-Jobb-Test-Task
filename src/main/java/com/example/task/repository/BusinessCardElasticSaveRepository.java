package com.example.task.repository;

import com.example.task.domain.BusinessCard;
import com.example.task.exception.BusinessCardException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class BusinessCardElasticSaveRepository {

    private final RestHighLevelClient elasticSearchClient;
    private final ObjectMapper objectMapper;

    @Value("${elasticsearch.businessCardIndex}")
    private String businessCardIndex;

    public void save(BusinessCard businessCard) {
        GetRequest getRequest = new GetRequest(businessCardIndex, businessCard.getBusinessCardId());
        try {
            GetResponse response = elasticSearchClient.get(getRequest, RequestOptions.DEFAULT);
            if (!response.isExists()) {
                saveBusinessCard(businessCard);
            }
        } catch (Exception e) {
            throw new BusinessCardException("Error occurred on businessCard search.", e);
        }
    }

    private void saveBusinessCard(BusinessCard businessCard) {
        try {
            IndexRequest request = new IndexRequest(businessCardIndex)
                    .id(businessCard.getBusinessCardId())
                    .source(objectMapper.writeValueAsString(businessCard), XContentType.JSON);
            elasticSearchClient.indexAsync(request, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                }

                @Override
                public void onFailure(Exception e) {
                    log.error(e.getMessage());
                }
            });
        } catch (Exception e) {
            throw new BusinessCardException("Error occurred on businessCard save.", e);
        }
    }
}
