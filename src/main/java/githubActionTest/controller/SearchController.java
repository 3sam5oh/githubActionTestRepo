package githubActionTest.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import githubActionTest.service.DataIndexService;
import githubActionTest.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchController {

    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    private final DataIndexService dataIndexService;
    private final SearchService searchService;

    public SearchController(DataIndexService dataIndexService, SearchService searchService) {
        this.dataIndexService = dataIndexService;
        this.searchService = searchService;
    }

    @PostMapping("/index")
    public ResponseEntity<String> indexData() {
        logger.info("데이터 인덱싱 요청 수신");
        try {
            String csvFilePath = "classpath:testdata.csv";
            Resource resource = new ClassPathResource(csvFilePath);
            dataIndexService.indexCsvData(resource.getInputStream());
            return ResponseEntity.ok("데이터 인덱싱 성공");
        } catch (IOException e) {
            logger.error("데이터 인덱싱 오류: ", e);
            return ResponseEntity.internalServerError().body("데이터 인덱싱 오류: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Map<String, Object>>> search(@RequestParam String query) {
        logger.info("검색 요청 수신: {}", query);
        try {
            List<Map<String, Object>> results = searchService.search(query);
            return ResponseEntity.ok(results);
        } catch (IOException e) {
            logger.error("검색 실행 오류: ", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
