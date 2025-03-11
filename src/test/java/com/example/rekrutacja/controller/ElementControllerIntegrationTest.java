package com.example.rekrutacja.controller;

import com.example.rekrutacja.model.Element;
import com.example.rekrutacja.repository.ElementRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ElementControllerIntegrationTest {

    @Autowired
    private WebTestClient webClient;

    @Autowired
    private ElementRepository repository;

    @Autowired
    private ObjectMapper mapper;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void testSaveElements() throws JsonProcessingException {
        var elements = Arrays.asList(
                Element.builder().id(1L).name("Element1").element(100).build(),
                Element.builder().id(2L).name("Element2").element(200).build()
        );

        webClient.post()
                .uri("/api/elements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(elements))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.zapisano").isEqualTo(2);
    }

    @Test
    void testInvalidData() throws JsonProcessingException {
        var invalidElements = Arrays.asList(
                Element.builder().id(1L).name("").element(100).build(),
                Element.builder().id(2L).name("Element2").element(-5).build()
        );
        webClient.post()
                .uri("/api/elements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(invalidElements))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("BŁĄD WALIDACJI")
                .jsonPath("$.szczegóły.name").isEqualTo("Nazwa wymagana");
    }
    
    @Test
    void testEmptyList() throws JsonProcessingException {
        var emptyList = Collections.emptyList();
        webClient.post()
                .uri("/api/elements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(mapper.writeValueAsString(emptyList))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("BŁĄD WALIDACJI")
                .jsonPath("$.szczegóły.elements").isEqualTo("Lista nie może być pusta");
    }
    
    @Test
    void testInvalidJson() {
        var invalidJson = "{invalid-json}";
        webClient.post()
                .uri("/api/elements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(invalidJson)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("BŁĄD FORMATU");
    }

    @Test
    void testUnknownProperties() {
        var jsonWithExtraFields = """
            [
              { "id": 1, "name": "Element1", "value": 100, "extraField": "extra" },
              { "id": 2, "name": "Element2", "value": 200 }
            ]
            """;

        webClient.post()
                .uri("/api/elements")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonWithExtraFields)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo("BŁĄD FORMATU");
    }
} 