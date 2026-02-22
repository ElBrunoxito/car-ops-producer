package com.yobi.caropsproducer.repository;

import com.yobi.caropsproducer.dto.CardReplacementRequestDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.json.JsonParseException;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Component
public class RedisRepository {
    private final ReactiveRedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper; // Para convertir DTO a JSON String
    private static final String PREFIX = "card:event:";

    public RedisRepository(
            @Qualifier("reactiveRedisTemplate")ReactiveRedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public Mono<String> getSnapshot(String requestId) {
        return redisTemplate.opsForValue().get(PREFIX + requestId);
    }

    // GUARDAR: El primer intento se queda en Redis por 24 horas
    public Mono<Boolean> saveSnapshot(String requestId, CardReplacementRequestDTO dto) {
        try {
            String json = objectMapper.writeValueAsString(dto);
            return redisTemplate.opsForValue()
                    .set(PREFIX + requestId, json, Duration.ofSeconds(10));
        } catch (JsonParseException e) {
            return Mono.error(new RuntimeException("Error serializando DTO para Redis", e));
        }
    }
}
