package com.netbox.prototype.controller;

import com.netbox.prototype.model.MultiplePrefixes;
import com.netbox.prototype.model.Prefix;
import com.netbox.prototype.service.PrefixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/prefixes")
public class PrefixController {

    private final PrefixService prefixService;

    @Autowired
    public PrefixController(PrefixService prefixService) {
        this.prefixService = prefixService;
    }

    // GET Prefix



    // POST Create Prefix
    @PostMapping
    public Mono<ResponseEntity<Prefix>> createPrefix(@RequestBody Prefix prefix) {
        return prefixService.createPrefix(prefix)
                .map(createdPrefix -> ResponseEntity.status(HttpStatus.CREATED).body(createdPrefix));
    }


    // POST Create multiple prefixes based on parrent prefix
    @PostMapping("create-multiple-parrent")
    public Mono<ResponseEntity<List<Prefix>>> createMultiplePrefixesParrent(@RequestBody MultiplePrefixes multiplePrefixes){
        return prefixService.createPrefixesParrent(multiplePrefixes);
    }

}


























