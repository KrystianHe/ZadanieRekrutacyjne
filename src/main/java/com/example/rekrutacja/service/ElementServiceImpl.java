package com.example.rekrutacja.service;

import com.example.rekrutacja.model.Element;
import com.example.rekrutacja.repository.ElementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElementServiceImpl implements ElementService {

    private final ElementRepository repository;

    @Override
    public Mono<Integer> saveElementsInParallel(List<Element> elements) {
        log.info("Zapisuję {} elementów", elements.size());
        
        return Flux.fromIterable(elements)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .doOnNext(element -> log.debug("Zapisuję: {}", element))
                .flatMap(element -> {
                    try {
                        Element saved = repository.save(element);
                        log.debug("Zapisano: {}", saved);
                        return Mono.just(saved);
                    } catch (Exception e) {
                        log.error("Błąd zapisu: {}", e.getMessage());
                        return Mono.error(e);
                    }
                })
                .sequential()
                .count()
                .map(Long::intValue)
                .doOnSuccess(count -> log.info("Zapisano {} elementów", count));
    }
} 