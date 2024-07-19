package com.netbox.prototype.service;

import com.netbox.prototype.model.Prefix;
import reactor.core.publisher.Mono;

public interface PrefixService {

    Mono<Prefix> createPrefix(Prefix prefix);
}
