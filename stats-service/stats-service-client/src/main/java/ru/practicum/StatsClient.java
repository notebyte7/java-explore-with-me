package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Component
@RequiredArgsConstructor
public class StatsClient {
    private WebClient client;

    public StatsClient(@Value("${client.url}") String baseUrl) {
        this.client = WebClient.create(baseUrl);
    }

    public ResponseEntity<Void> postEndpointHit(EndpointHitDto endpointHitDto) {
        return client
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path("/hit")
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public ResponseEntity<List<ViewStatsDto>> getViewStats(String start, String end, String[] uris, boolean unique) {
        return client
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", (Object[]) uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .toEntityList(ViewStatsDto.class)
                .block();
    }
}