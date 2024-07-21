package com.netbox.prototype.service;

import com.netbox.prototype.model.MultiplePrefixes;
import com.netbox.prototype.model.Prefix;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PrefixService {

    Mono<Prefix> createPrefix(Prefix prefix);
    Mono<ResponseEntity<List<Prefix>>> createPrefixes(MultiplePrefixes request);
}
