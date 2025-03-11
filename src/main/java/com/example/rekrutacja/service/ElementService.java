package com.example.rekrutacja.service;

import com.example.rekrutacja.model.Element;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Serwis odpowiedzialny za operacje na elementach.
 */
public interface ElementService {

    /**
     * Zapisuje elementy równolegle do bazy danych.
     * Wykorzystanie równoległego zapisania
     *
     * @param elements Lista elementów do zapisania
     * @return Mono zwraca jaka liczba elementów została zapisan do bazy danych.
     */
    Mono<Integer> saveElementsInParallel(List<Element> elements);
} 