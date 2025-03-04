package com.join.publisher.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.join.publisher.models.Client;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientPublisherService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publishClient(Client client) {
        String messageKey = UUID.randomUUID().toString();
        try {
            String clientJson = objectMapper.writeValueAsString(client);
            log.info("Enviando cliente para criacao: {}", clientJson);
            kafkaTemplate.send("client-topic", messageKey, clientJson);
            log.info("Criado cliente: {}", clientJson);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter cliente para JSON", e);
            throw new RuntimeException("Erro ao converter cliente para JSON", e);
        }
    }
}
