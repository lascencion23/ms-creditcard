package com.java.everis.mscreditcard.service.impl;

import com.java.everis.mscreditcard.entity.CreditCard;
import com.java.everis.mscreditcard.entity.Customer;
import com.java.everis.mscreditcard.repository.CreditCardRepository;
import com.java.everis.mscreditcard.service.CreditCardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditCardServiceImpl implements CreditCardService {

	private final WebClient webClient;
	private final ReactiveCircuitBreaker reactiveCircuitBreaker;
	
	String uri = "http://localhost:8090/api/ms-customer/customer/find/{id}";
	
	public CreditCardServiceImpl(ReactiveResilience4JCircuitBreakerFactory circuitBreakerFactory) {
		this.webClient = WebClient.builder().baseUrl(this.uri).build();
		this.reactiveCircuitBreaker = circuitBreakerFactory.create("customer");
	}
	
    @Autowired
    CreditCardRepository creditCardRepository;

    // Plan A
    @Override
    public Mono<Customer> findCustomerById(String id) {
		return reactiveCircuitBreaker.run(webClient.get().uri(this.uri,id).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Customer.class),
				throwable -> {
					return this.getDefaultCustomer();
				});
    }
    
    // Plan B
 	public Mono<Customer> getDefaultCustomer() {
 		Mono<Customer> customer = Mono.just(new Customer("0", null, null,null,null,null,null,null));
 		return customer;
 	}

    
    @Override
    public Mono<CreditCard> create(CreditCard t) {
        return creditCardRepository.save(t);
    }

    @Override
    public Flux<CreditCard> findAll() {
        return creditCardRepository.findAll();
    }

    @Override
    public Mono<CreditCard> findById(String id) {
        return creditCardRepository.findById(id);
    }

    @Override
    public Mono<CreditCard> update(CreditCard t) {
        return creditCardRepository.save(t);
    }

    @Override
    public Mono<Boolean> delete(String t) {
        return creditCardRepository.findById(t)
                .flatMap(credit -> creditCardRepository.delete(credit).then(Mono.just(Boolean.TRUE)))
                .defaultIfEmpty(Boolean.FALSE);
    }
    

    @Override
    public Flux<CreditCard> findCreditCardByCustomer(String id) {
        return creditCardRepository.findByCustomerId(id);
    }
}
