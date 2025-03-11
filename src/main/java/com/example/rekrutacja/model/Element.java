package com.example.rekrutacja.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "elements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Element {

    @Id
    private Long id;

    @NotBlank(message = "Nazwa wymagana")
    private String name;

    @NotNull(message = "Wartość jest wymagana")
    @Min(value = 1, message = "Wartość musi być > 0")
    @Column(name = "element_value")
    @JsonProperty("value")
    private Integer element;
} 