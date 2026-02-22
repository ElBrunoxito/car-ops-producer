package com.yobi.caropsproducer.serviceImpl;

import com.yobi.caropsproducer.dto.CardReplacementRequestDTO;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tools.jackson.databind.ObjectMapper;

@Component
@Slf4j
public class CardReplacementPublisher {
    // Cambiamos a String, String para que no haya duda
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic;

    public CardReplacementPublisher(
            KafkaTemplate<String, String> kafkaTemplate,
            ObjectMapper objectMapper,
            @Value("${spring.kafka.topic.name}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public Mono<Void> publish(CardReplacementRequestDTO dto) {
        return Mono.fromCallable(() -> objectMapper.writeValueAsString(dto))
                .flatMap(json -> Mono.fromFuture(kafkaTemplate.send(topic, dto.getRequestId(), json)))
                .doOnSuccess(r -> log.info("Enviado a Kafka correctamente"))
                .doOnError(e -> log.error("Error REAL al construir o enviar: ", e))
                .then();
    }

}
