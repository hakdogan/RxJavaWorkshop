package jugistanbul.service;

import com.google.gson.Gson;
import jugistanbul.entity.Passengers;
import jugistanbul.entity.Speaker;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hakdogan (hakdogan@kodcu.com)
 * Created on 8.02.2020
 **/

@Service
public class DAOServiceImpl implements DAOService
{

    private static final String DELETE_SPEAKER = "delete from speaker where id=%d";
    private static final String GET_ONE_SPEAKER = "select * from speaker where id=%d";
    private static final String INSERT_SPEAKER  = "insert into speaker values(%d, '%s', '%s', %s, %s, '%s', '%s')";
    private static final String UPDATE_SPEAKER  = "update speaker set name='%s', title='%s', " +
            "approve=%s, retracted=%s, mail='%s', updateTime='%s' where id=%d";
    private static final String ALL_CHANGE_DATA = "select * from speaker where updateTime > timestamp '%s'";
    private static final String ALL_PASSENGERS = "select * from passengers";

    private final JdbcTemplate jdbcTemplate;
    private final RestHighLevelClient client;
    private final Gson gson;
    private final Logger logger = LoggerFactory.getLogger(DAOServiceImpl.class);

    @Autowired
    public DAOServiceImpl(final JdbcTemplate jdbcTemplate,
                          @Qualifier("RestHighLevelClient") final RestHighLevelClient client, final Gson gson){
        this.jdbcTemplate = jdbcTemplate;
        this.client = client;
        this.gson = gson;
    }

    @Override
    public Speaker findSpeakerById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(String.format(GET_ONE_SPEAKER, id), new BeanPropertyRowMapper<>(Speaker.class));
        } catch (EmptyResultDataAccessException e){
            logger.error("EmptyResultDataAccessException");
        }
        return null;
    }

    @Override
    public List<Speaker> findAllSpeakers() {

        final List<Speaker> result = new ArrayList<>();
        SearchRequest searchRequest = new SearchRequest("speaker");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        try {
            final SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            searchResponse.getHits();

            final SearchHits hits = searchResponse.getHits();
            final SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                final Speaker speaker = gson.fromJson(hit.getSourceAsString(), Speaker.class);
                speaker.setId(Integer.parseInt(hit.getId()));
                result.add(speaker);
            }
        } catch (IOException e) {
            logger.error("An exception was thrown", e);
        }

        return result; // jdbcTemplate.query(ALL_SPEAKERS, new BeanPropertyRowMapper<>(Speaker.class));
    }

    @Override
    public List<Speaker> findAllChangeSpeakersByTime( final LocalDateTime localDateTime) {
        return jdbcTemplate.query(String.format(ALL_CHANGE_DATA, localDateTime), new BeanPropertyRowMapper<>(Speaker.class));
    }

    @Override
    public List<Passengers> findAllPassengers() {
        return jdbcTemplate.query(ALL_PASSENGERS, new BeanPropertyRowMapper<>(Passengers.class));
    }

    @Override
    public Speaker addSpeaker(Speaker speaker) {
        final Speaker oldSpeaker = findSpeakerById(speaker.getId());
        if(null != oldSpeaker){
            return updateSpeaker(speaker);
        }
        speaker.setUpdateTime(LocalDateTime.now());
        jdbcTemplate.update(String.format(INSERT_SPEAKER, speaker.getId(), speaker.getName(),
                speaker.getTitle(), speaker.isApprove(), speaker.isRetracted(), speaker.getMail(), speaker.getUpdateTime()));
        return speaker;
    }

    @Override
    public Speaker updateSpeaker(final Speaker speaker) {
        speaker.setUpdateTime(LocalDateTime.now());
        jdbcTemplate.update(String.format(UPDATE_SPEAKER, speaker.getName(), speaker.getTitle(),
                speaker.isApprove(), speaker.isRetracted(), speaker.getMail(), speaker.getUpdateTime(), speaker.getId()));
        return speaker;
    }

    @Override
    public int deleteSpeaker(final Integer id) {
        return jdbcTemplate.update(String.format(DELETE_SPEAKER, id));
    }
}
