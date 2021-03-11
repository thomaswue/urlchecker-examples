package com.example.springurlchecker;

import org.jsoup.Jsoup;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Map;

@RestController("/")
public class MyController {

    private final WebClient webClient;

    public MyController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @GetMapping(value = "/check", params = "url", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Map<String, Serializable>> check(String url) {
        return webClient.get().uri(url).retrieve().bodyToFlux(String.class).flatMap(
                html -> Flux.fromIterable(Jsoup.parse(html).select("a[href]").eachAttr("href"))
        ).flatMap(
                link -> webClient.head().uri(link)
                        .retrieve()
                        .toBodilessEntity()
                        .map(response -> response.getStatusCode())
                        .onErrorResume(throwable -> Mono.just(HttpStatus.BAD_REQUEST))
                        .map(statusCode -> Map.of("url", link, "status", statusCode)
                )
        );
    }
}
