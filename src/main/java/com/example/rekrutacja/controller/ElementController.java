package com.example.rekrutacja.controller;

import com.example.rekrutacja.model.Element;
import com.example.rekrutacja.service.ElementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * Kontroler REST obsługujący operacje na elementach.
 * Zapewnia endpoint do równoległego zapisu elementów do bazy danych.
 */
@RestController
@RequestMapping("/api/elements")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ElementController {

    private final ElementService service;

    /**
     * @param elements Lista elementów do zapisania (nie może być pusta)
     * @return Liczba pomyślnie zapisanych elementów
     */
    @PostMapping
    public Mono<ResponseEntity<Map<String, Integer>>> saveElements(
            @RequestBody @NotEmpty(message = "Lista nie może być pusta") List<@Valid Element> elements) {
        log.info("Otrzymano {} elementów", elements.size());
        
        return service.saveElementsInParallel(elements)
                .map(count -> ResponseEntity.ok(Map.of("zapisano", count)));
    }
} 