package com.example.rekrutacja.service;

import com.example.rekrutacja.model.Element;
import com.example.rekrutacja.repository.ElementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElementServiceImplTest {

    @Mock
    private ElementRepository repository;

    @InjectMocks
    private ElementServiceImpl service;

    private List<Element> elements;

    @BeforeEach
    void setUp() {
        elements = Arrays.asList(
                Element.builder().id(1L).name("Element1").element(100).build(),
                Element.builder().id(2L).name("Element2").element(200).build(),
                Element.builder().id(3L).name("Element3").element(300).build()
        );
    }

    @Test
    void testSaveElements() {
        when(repository.save(any(Element.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Mono<Integer> result = service.saveElementsInParallel(elements);

        StepVerifier.create(result)
                .expectNext(3)
                .verifyComplete();

        verify(repository, times(3)).save(any(Element.class));
    }

    @Test
    void testSaveEmptyList() {
        List<Element> emptyList = List.of();

        Mono<Integer> result = service.saveElementsInParallel(emptyList);

        StepVerifier.create(result)
                .expectNext(0)
                .verifyComplete();

        verify(repository, never()).save(any(Element.class));
    }

    @Test
    void testSaveWithException() {
        when(repository.save(any(Element.class)))
                .thenThrow(new RuntimeException("Test exception"));

        Mono<Integer> result = service.saveElementsInParallel(elements);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(repository, atLeastOnce()).save(any(Element.class));
    }
} 