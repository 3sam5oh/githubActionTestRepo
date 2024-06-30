package githubActionTest.service;

import org.opensearch.action.search.SearchRequest;
import org.opensearch.action.search.SearchResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.index.query.QueryBuilders;
import org.opensearch.search.SearchHit;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private final RestHighLevelClient client;

    public SearchService(RestHighLevelClient client) {
        this.client = client;
    }

    public List<Map<String, Object>> search(String query) throws IOException {
        logger.info("검색 실행: {}", query);

        SearchRequest searchRequest = new SearchRequest("products");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(query, "기업명", "상품명", "카테고리그룹", "1차카테고리", "2차카테고리"));
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            List<Map<String, Object>> results = Arrays.stream(searchResponse.getHits().getHits())
                    .map(SearchHit::getSourceAsMap)
                    .collect(Collectors.toList());
            logger.info("검색 완료, 결과 수: {}", results.size());
            return results;
        } catch (IOException e) {
            logger.error("검색 실행 오류: ", e);
            throw e;
        }
    }
}
