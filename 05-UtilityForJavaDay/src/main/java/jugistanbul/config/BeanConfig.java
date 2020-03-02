package jugistanbul.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@Component
public class BeanConfig {

    private static final String INDEX_NAME = "speaker";
    private final Logger logger = LoggerFactory.getLogger(BeanConfig.class);

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Gson getGsonInstance() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                ZonedDateTime.parse(json.getAsJsonPrimitive().getAsString()).toLocalDateTime()).create();
    }

    @Bean(destroyMethod = "close")
    @Qualifier("RestHighLevelClient")
    public RestHighLevelClient getClient() {
        final RestHighLevelClient
                client = new RestHighLevelClient(RestClient
                .builder(new HttpHost("localhost", 9200, "http")));

        createIndexIfNotExist(client);
        return client;
    }

    public void createIndexIfNotExist(final RestHighLevelClient client) {

        try {

            final GetIndexRequest request = new GetIndexRequest(INDEX_NAME);
            final boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);

            if (exists) {
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(INDEX_NAME);
                client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            }

            final CreateIndexRequest indexRequest = new CreateIndexRequest(INDEX_NAME);
            indexRequest.settings(Settings.builder()
                    .put("index.number_of_shards", 2)
                    .put("index.number_of_replicas", 1)
            );

            final CreateIndexResponse createIndexResponse = client.indices().create(indexRequest, RequestOptions.DEFAULT);
            if (createIndexResponse.isAcknowledged() && createIndexResponse.isShardsAcknowledged()) {
                logger.info("{} index created successfully", INDEX_NAME);
            } else {
                logger.debug("Failed to create {} index", INDEX_NAME);
            }

            final PutMappingRequest mappingRequest = new PutMappingRequest(INDEX_NAME);
            final XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.startObject("properties");
                {
                    builder.startObject("name");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    builder.startObject("title");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    builder.startObject("approve");
                    {
                        builder.field("type", "boolean");
                    }
                    builder.endObject();

                    builder.startObject("retracted");
                    {
                        builder.field("type", "boolean");
                    }
                    builder.endObject();

                    builder.startObject("mail");
                    {
                        builder.field("type", "text");
                    }
                    builder.endObject();

                    builder.startObject("updateTime");
                    builder.field("type", "text");
                    builder.endObject();
                }
                builder.endObject();
            }
            builder.endObject();
            mappingRequest.source(builder);
            final AcknowledgedResponse putMappingResponse = client.indices().putMapping(mappingRequest, RequestOptions.DEFAULT);

            if (putMappingResponse.isAcknowledged()) {
                logger.info("Mapping of {} was successfully created", INDEX_NAME);
            } else {
                logger.debug("Creating mapping of {} failed", INDEX_NAME);
            }

        } catch (IOException e) {
            logger.error("An exception was thrown ", e);
        }
    }
}
