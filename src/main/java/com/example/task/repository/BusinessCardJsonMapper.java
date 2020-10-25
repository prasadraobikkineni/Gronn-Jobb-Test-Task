package com.example.task.repository;

import com.example.task.domain.BusinessCard;
import com.example.task.exception.BusinessCardSearchException;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BusinessCardJsonMapper {

    private final ObjectReader businessCardObjectReader;

    public BusinessCard toBusinessCard(SearchHit[] searchHits) {
        try {
            return Arrays.stream(searchHits)
                    .parallel()
                    .map(searchHit -> toBusinessCard(searchHit.getSourceRef().toBytesRef().bytes, searchHit.getId()))
                    .findFirst()
                    .orElse(new BusinessCard());
        } catch (Exception e) {
            throw new BusinessCardSearchException("Error occurred on bulk businessCard mapping", e);
        }
    }

    public List<BusinessCard> toBusinessCardList(SearchHit[] searchHits) {
        try {
            return Arrays.stream(searchHits)
                    .parallel()
                    .map(searchHit -> toBusinessCard(searchHit.getSourceRef().toBytesRef().bytes, searchHit.getId()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessCardSearchException("Error occurred on bulk businessCard mapping", e);
        }
    }

    private BusinessCard toBusinessCard(byte[] sourceBytes, String businessCardId) {
        try {
            if (sourceBytes.length == 0) {
                return null;
            }
            BusinessCard businessCard = businessCardObjectReader.readValue(sourceBytes);
            businessCard.setBusinessCardId(businessCardId);
            return businessCard;
        } catch (Exception e) {
            throw new BusinessCardSearchException("Error occurred on businessCard mapping", e);
        }
    }
}
