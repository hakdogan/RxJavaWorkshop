package jugistanbul.scheduler;

import jugistanbul.entity.Speaker;
import jugistanbul.service.DAOService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@Component
public class SyncScheduler
{

    private final RestHighLevelClient client;
    private final DAOService daoService;
    private final Logger logger = LoggerFactory.getLogger(SyncScheduler.class);
    private LocalDateTime lastQueryTime = LocalDateTime.now();

    private static final String INDEX = "speaker";

    @Autowired
    public SyncScheduler(@Qualifier("RestHighLevelClient") final RestHighLevelClient client, final DAOService daoService){
        this.client = client;
        this.daoService = daoService;
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleTaskWithFixedRate() throws IOException {

        try {

            final List<Speaker> speakers = daoService.findAllChangeSpeakersByTime(lastQueryTime);
            final BulkRequest bulkRequest = new BulkRequest();
            speakers.stream().forEach(speaker -> {
                bulkRequest.add(new IndexRequest(INDEX).id(String.valueOf(speaker.getId()))
                        .source(XContentType.JSON, "name", speaker.getName(),
                                "title", speaker.getTitle(),
                                "approve", speaker.isApprove(),
                                "retracted", speaker.isRetracted(),
                                "mail", speaker.getMail(),
                                "updateTime", speaker.getUpdateTime()));
            });

            if (bulkRequest.numberOfActions() > 0) {
                client.bulk(bulkRequest, RequestOptions.DEFAULT);
                logger.info("Elasticsearch synced with database. Queried time time is {}", lastQueryTime);
            }

            lastQueryTime = LocalDateTime.now();
        } catch (Exception e){
            logger.error("An exception was thrown", e);
        }
    }
}
