package com.example.rekrutacja.service;

import com.example.rekrutacja.model.Element;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ElementService {
    
    /**
     * Zapisuje elementy równolegle
     */
    Mono<Integer> saveElementsInParallel(List<Element> elements);
} 