package com.netbox.prototype.service;

import com.netbox.prototype.model.MultiplePrefixes;
import com.netbox.prototype.model.Prefix;
import com.netbox.prototype.model.PrefixResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.pow;

@Service
public class prefixServiceImpl implements PrefixService {
    private final WebClient webClient;

    @Autowired
    public prefixServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000/api/ipam")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Token d47a1cb460415d2acd9ae20c9708b105e38c0f7b")
                .build();
    }

    public Mono<Prefix> getPrefix(String id) {
        return this.webClient.get()
                .uri("/prefixes/{id}/", id)
                .retrieve()
                .bodyToMono(Prefix.class);
    }

    public Mono<Prefix> createPrefix(Prefix prefix) {
        return this.webClient.post()
                .uri("/prefixes/")
                .bodyValue(prefix)
                .retrieve()
                .bodyToMono(Prefix.class);
    }

    public Mono<Void> createChildPrefixes() {
        return getAllPrefixes()
                .flatMapMany(Flux::fromIterable)
                .filter(prefix -> prefix.getDepth() > 0)
                .flatMap(prefix -> Flux.range(1, 100) // 100 oznacza liczbę powtórzeń
                        .flatMap(i -> createAvailablePrefixes(prefix.getId(), 32))
                        .then() // Kiedy wszystkie operacje zakończą się to kontynuuj
                )
                .then();
    }



    private Mono<List<Prefix>> createAvailablePrefixes(int parentId, int prefixLength) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prefix_length", prefixLength);
        requestBody.put("description", "Generated child prefix");

        return this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/prefixes/{id}/available-prefixes/")
                        .build(parentId)
                )
                .bodyValue(requestBody)
                .retrieve()
                .bodyToFlux(Prefix.class)
                .collectList()
                .onErrorResume(e -> {
                    System.err.println("Error creating child prefixes for parent prefix ID " + parentId + ": " + e.getMessage());
                    return Mono.empty();
                });
    }


    public Mono<List<Prefix>> getAllPrefixes() {
            return this.webClient.get()
                    .uri("/prefixes/?limit=10000")  // family=4&
                    .retrieve()
                    .bodyToFlux(PrefixResponse.class)
                    .flatMapIterable(PrefixResponse::getResults)
                    .map(prefixResult -> {
                        Prefix prefix = new Prefix();
                        prefix.setId(prefixResult.getId());
                        prefix.setPrefix(prefixResult.getPrefix());
                        prefix.setDepth(prefixResult.getDepth());
                        prefix.setChildren(prefixResult.getChildren());
                        prefix.setDescription(prefixResult.getDescription());
                        return prefix;
                    })
                    .collectList();
    }


    public Mono<Prefix> updatePrefix(String id, Prefix prefix) {
        return this.webClient.put()
                .uri("/prefixes/{id}/", id)
                .bodyValue(prefix)
                .retrieve()
                .bodyToMono(Prefix.class);
    }

    public Mono<Void> deletePrefix(String id) {
        return this.webClient.delete()
                .uri("/prefixes/{id}/", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
}






































