# REST API z równoległym zapisem do bazy danych

Aplikacja Spring Boot do równoległego zapisu elementów do bazy danych.

## Technologie
- Spring Boot 3.4.3
- Spring Data JPA
- Spring WebFlux
- H2 Database
- Lombok

## Funkcje
- Endpoint `/api/elements` (POST) do zapisu elementów
- Walidacja danych wejściowych
- Równoległy zapis do bazy danych
- Logowanie operacji
- Strona główna z dokumentacją API

## Uruchomienie
```bash
mvn spring-boot:run
```

Konsola H2: http://localhost:8080/h2-console

Dane do logowania H2:
- JDBC URL: `jdbc:h2:mem:elementdb`
- Username: `sa`
- Password: `password`

### POST /api/elements
Zapisuje listę elementów równolegle do bazy danych.


Przykład w curl:
```
curl -X POST http://localhost:8080/api/elements \
  -H "Content-Type: application/json" \
  -d '[
    { "id": 1, "name": "Element1", "value": 100 },
    { "id": 2, "name": "Element2", "value": 200 }
  ]'
```

Odpowiedź: `{"zapisano": 2}`
