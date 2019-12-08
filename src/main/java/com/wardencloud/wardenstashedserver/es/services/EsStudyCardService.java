package com.wardencloud.wardenstashedserver.es.services;

import java.io.IOException;
import java.util.List;

import com.wardencloud.wardenstashedserver.es.entities.EsStudyCard;

import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.Search;

@Service
public class EsStudyCardService {
    @Autowired
    private JestClient jestClient;

    public List<EsStudyCard> search(String content, int pageNumber) {
        final int pageSize = 4;
        int baseIndex = pageNumber * pageSize;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(
            QueryBuilders
            .boolQuery()
            .should(QueryBuilders.matchQuery("title", content).fuzziness("10").operator(Operator.OR))
            .should(QueryBuilders.matchQuery("description", content).fuzziness("10").operator(Operator.OR))
            .minimumShouldMatch(1)
        )
        .from(baseIndex)
        .size(pageSize);
        Search search = new Search
                            .Builder(searchSourceBuilder.toString())
                            .addIndex("cards")
                            .addType("card")
                            .build();
        try {
            JestResult result = jestClient.execute(search);
            return result.getSourceAsObjectList(EsStudyCard.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteStudyCardById(int id) {
        Delete delete = new Delete
                        .Builder(Integer.toString(id))
                        .index("cards")
                        .type("card")
                        .build();
        try {
            jestClient.execute(delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
