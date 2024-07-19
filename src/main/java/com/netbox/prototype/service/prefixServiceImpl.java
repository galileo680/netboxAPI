package com.netbox.prototype.service;

import com.netbox.prototype.model.MultiplePrefixes;
import com.netbox.prototype.model.Prefix;
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

    public Mono<ResponseEntity<List<String>>> createMultiplePrefixes(MultiplePrefixes request) {
        if (request.getCount() <= 0 || request.getLength() <= 0) {
            return Mono.just(ResponseEntity.badRequest().build());
        }

        // Generowanie prefixów
        List<String> prefixes = generatePrefixes(request.getCount(), request.getLength(), request.getParentPrefix());

        System.out.println(Arrays.toString(prefixes.toArray()));

        // Wstawianie do NetBox
        return Mono.zip(
                prefixes.stream()
                        .map(prefix -> createPrefixInNetBox(prefix))
                        .collect(Collectors.toList()),
                results -> ResponseEntity.status(HttpStatus.CREATED).body(prefixes)
        ).onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()));
    }


    public List<String> generatePrefixes(int count, int length, String parentPrefix) {
        List<String> prefixes = new ArrayList<>();
        String basePrefix = parentPrefix.split("/")[0]; // Base IP
        int prefixLength = Integer.parseInt(parentPrefix.split("/")[1]); // Base Prefix Length

        // Generowanie prefixów
        for (int i = 0; i < count; i++) {
            String newPrefix = basePrefix + "/" + (prefixLength + length);
            prefixes.add(newPrefix);
            basePrefix = incrementIPAddress(basePrefix);
        }

        return prefixes;
    }

    // Helper function to increment IP address
    private String incrementIPAddress(String ip) {
        String[] parts = ip.split("\\.");
        int lastPart = Integer.parseInt(parts[3]) + 1;
        return parts[0] + "." + parts[1] + "." + parts[2] + "." + lastPart;
    }

    public Mono<String> createPrefixInNetBox(WebClient webClient, String prefix) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/").build())
                .bodyValue(Collections.singletonMap("prefix", prefix)) // Wysyłanie ciała żądania jako JSON
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    System.err.println("Error creating prefix in NetBox: " + e.getMessage());
                    return Mono.empty();
                });
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
