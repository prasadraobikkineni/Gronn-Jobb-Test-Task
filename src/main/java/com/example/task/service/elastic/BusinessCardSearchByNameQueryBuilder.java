package com.example.task.service.elastic;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BusinessCardSearchByNameQueryBuilder {

    public static final String NAME_FIELD = "entityList.name.name";

    public QueryBuilder buildForGet(String name) {
        String[] splitKeywords = name.toLowerCase().split("\\s+");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        Arrays.stream(splitKeywords).forEach(word -> boolQueryBuilder.must(QueryBuilders.prefixQuery(NAME_FIELD, word)));
        return boolQueryBuilder;
    }

    public QueryBuilder buildForSearch(String name) {
        String[] splitKeywords = name.toLowerCase().split("\\s+");
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        Arrays.stream(splitKeywords).forEach(word -> boolQueryBuilder.should(QueryBuilders.prefixQuery(NAME_FIELD, word)));
        return boolQueryBuilder;
    }
}
