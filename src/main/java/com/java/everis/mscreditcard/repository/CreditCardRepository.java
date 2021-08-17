package com.java.everis.mscreditcard.repository;

import com.java.everis.mscreditcard.entity.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {

    Flux<CreditCard> findByCustomerId(String id);
}
