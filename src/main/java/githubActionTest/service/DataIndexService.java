package githubActionTest.service;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class DataIndexService {

    private static final Logger logger = LoggerFactory.getLogger(DataIndexService.class);
    private final RestHighLevelClient client;

    public DataIndexService(RestHighLevelClient client) {
        this.client = client;
    }

    public void indexCsvData(InputStream inputStream) throws IOException {
        logger.info("CSV 파일에서 데이터를 인덱싱 시작");

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .build();

        try (InputStreamReader reader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(reader, csvFormat)) {

            for (CSVRecord record : csvParser) {
                Map<String, Object> document = new HashMap<>();
                document.put("기업명", record.get("기업명"));
                document.put("상품유형", record.get("상품유형"));
                document.put("카테고리그룹", record.get("카테고리그룹"));
                document.put("1차카테고리", record.get("1차카테고리"));
                document.put("2차카테고리", record.get("2차카테고리"));
                document.put("상품명", record.get("상품명"));
                document.put("판매단위", record.get("판매단위"));
                document.put("판매가", Integer.parseInt(record.get("판매가").trim()));
                document.put("기준일", record.get("기준일"));

                IndexRequest indexRequest = new IndexRequest("products")
                        .source(document);
                client.index(indexRequest, RequestOptions.DEFAULT);
            }
            logger.info("CSV 파일에서 데이터를 성공적으로 인덱싱 완료");
        } catch (IOException e) {
            logger.error("CSV 데이터 인덱싱 오류: ", e);
            throw e;
        }
    }
}
