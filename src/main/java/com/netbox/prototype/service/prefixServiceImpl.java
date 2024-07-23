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
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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


    @Override
    public Mono<ResponseEntity<List<Prefix>>> createPrefixesParrent(MultiplePrefixes request) {
        if (request.getCount() <= 0 || request.getLength() <= 0) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        getAllPrefixes()
                .subscribe(prefixes -> {
                    System.out.println(prefixes);
                }, error -> {
                    System.err.println("Error retrieving prefixes: " + error.getMessage());
                });

        // Losowy parent prefix
        return //getRandomParentPrefix()
                //.flatMap(parentPrefix ->
                        getAvailablePrefixes("1507", request.getLength())
                                .flatMap(availablePrefixes -> {

                                    List<Prefix> childPrefixes = availablePrefixes.stream()
                                            .limit(request.getCount())
                                            .map(prefix -> new Prefix(prefix.getPrefix(), "Generated child prefix"))
                                            .collect(Collectors.toList());

                                    List<Mono<Prefix>> prefixMonos = childPrefixes.stream()
                                            .map(this::createPrefix)
                                            .collect(Collectors.toList());

                                    return Mono.zip(prefixMonos, results -> {
                                        List<Prefix> createdPrefixes = new ArrayList<>();
                                        for (Object result : results) {
                                            if (result instanceof Prefix) {
                                                createdPrefixes.add((Prefix) result);
                                            }
                                        }
                                        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrefixes);
                                    }).onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
                                });
                //);
    }

    /*private Mono<String> getRandomParentPrefix() {
        return webClient.get()
                .uri("/prefixes/")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {

                    return response.split(",")[0];
                });
    }*/

    public Mono<List<Prefix>> getAllPrefixes() {
        return this.webClient.get()
                .uri("/prefixes/?family=4&limit=10000")
                .retrieve()
                .bodyToFlux(PrefixResponse.class)
                .flatMapIterable(PrefixResponse::getResults)
                .map(prefixResult -> {
                    Prefix prefix = new Prefix();
                    prefix.setId(prefixResult.getId());
                    prefix.setPrefix(prefixResult.getPrefix());
                    prefix.setDepth(prefixResult.getDepth());
                    prefix.setChildren(prefixResult.getChildren());

                    return prefix;
                })
                .collectList();
    }

    private Mono<List<Prefix>> getAvailablePrefixes(String parentPrefix, int prefixLength){

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/prefixes/{parentPrefix}/available-prefixes/")
                        .queryParam("prefix_length", prefixLength)
                        .build(parentPrefix)
                )
                .retrieve()
                .bodyToFlux(Prefix.class)
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






































