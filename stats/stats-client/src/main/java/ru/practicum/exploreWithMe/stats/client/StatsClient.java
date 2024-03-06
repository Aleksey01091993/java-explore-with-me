package ru.practicum.exploreWithMe.stats.client;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.exploreWithMe.stats.dto.RequestStatsDto;
import ru.practicum.exploreWithMe.stats.dto.ResponseGetStatsDto;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class StatsClient {
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String application;
    private final String statsServiceUri;
    private final ObjectMapper json;
    private final HttpClient httpClient;

    public StatsClient(ObjectMapper json) {
        this.application = "ewm-main-service";
        this.statsServiceUri = "http://localhost:9090";
        this.json = json;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(2))
                .build();
    }

    public void hit(HttpServletRequest userRequest) {
        RequestStatsDto hit = RequestStatsDto.builder()
                .app(application)
                .ip(userRequest.getRemoteAddr())
                .uri(userRequest.getRequestURI())
                .timestamp(LocalDateTime.now().format(DTF))
                .build();

        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest
                    .BodyPublishers
                    .ofString(json.writeValueAsString(hit));

            HttpRequest hitRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/hit"))
                    .POST(bodyPublisher)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();

            HttpResponse<Void> response = httpClient.send(hitRequest, HttpResponse.BodyHandlers.discarding());
            log.debug("Response from stats-service: {}", response);
        } catch (Exception e) {
            log.warn("Cannot record hit", e);
        }
    }

    public List<ResponseGetStatsDto> getStats(HttpServletRequest adminRequest, String uri) {
        try {
            HttpRequest statsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(statsServiceUri + "/stats?uris=/events" + uri + "&unique=true"))
                    .GET()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(statsRequest, HttpResponse.BodyHandlers.ofString());
            String string = response.body();
            return json.readValue(response.body(), new TypeReference<List<ResponseGetStatsDto>>() {
            });
        } catch (Exception e) {
            log.warn("I can't get statistics", e);
            return null;
        }
    }


}
