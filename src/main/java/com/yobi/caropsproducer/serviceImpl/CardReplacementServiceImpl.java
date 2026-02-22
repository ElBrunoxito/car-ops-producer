package com.yobi.caropsproducer.serviceImpl;

import com.yobi.caropsproducer.dto.CardReplacementRequestDTO;
import com.yobi.caropsproducer.repository.RedisRepository;
import com.yobi.caropsproducer.service.CardReplacementService;
import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.aot.KafkaAvroBeanRegistrationAotProcessor;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
@Slf4j
public class CardReplacementServiceImpl implements CardReplacementService {

    private final RedisRepository redisRepository;
    private final CardReplacementPublisher kafkaPublisher;

    public Single<String> process(CardReplacementRequestDTO dto) {
        var requestId = dto.getRequestId();
        return redisRepository.getSnapshot(requestId)
                .flatMap(existingSnapshot -> {
                    // CASO: SEGUNDO INTENTO (Ya existía en Redis)
                    log.info("Segundo intento detectado para ID: {}", requestId);
                    return kafkaPublisher.publish(dto)
                            .thenReturn("Procesado como segundo intento");
                }).switchIfEmpty(
                        // CASO: PRIMER INTENTO (No estaba en Redis)
                        Mono.defer(() -> {
                            log.info("Primer intento. Guardando en Redis y enviando a Kafka: {}", requestId);
                            // Guardamos en Redis y, al terminar, publicamos en Kafka
                            return redisRepository.saveSnapshot(requestId, dto)
                                    .then(kafkaPublisher.publish(dto))
                                    .thenReturn("Procesado como primer intento");
                        })
                )

                .onErrorResume(e -> {
                    log.error("Error en el flujo: {}", e.getMessage());
                    return Mono.error(new RuntimeException("Error al procesar reemplazo de tarjeta"));
                })
                .as(Single::fromPublisher);
    }






}
