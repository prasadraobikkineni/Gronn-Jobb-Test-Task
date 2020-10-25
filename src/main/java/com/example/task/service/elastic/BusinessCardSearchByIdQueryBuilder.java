package com.example.task.service.elastic;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessCardSearchByIdQueryBuilder {

    public static final String ID_FIELD = "participant.value";

    public QueryBuilder build(String id) {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.filter(QueryBuilders.termQuery(ID_FIELD, id));
        return queryBuilder;
    }
}
