package com.join.publisher.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.join.publisher.models.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientPublisherServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ClientPublisherService clientPublisherService;

    private Client testClient;
    private String testClientJson;

    @BeforeEach
    void setUp() {
        testClient = Client.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .cpf("123.456.789-00")
                .phone("(11) 99999-9999")
                .build();

        testClientJson = "{\"id\":\"" + testClient.getId() + "\",\"name\":\"Test User\",\"email\":\"test@example.com\",\"cpf\":\"123.456.789-00\",\"telefone\":\"(11) 99999-9999\"}";
    }

    @Test
    void publishClientShouldSucceedWhenValidData() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(testClient)).thenReturn(testClientJson);
        when(kafkaTemplate.send(anyString(), anyString(), anyString())).thenReturn(null);
        clientPublisherService.publishClient(testClient);
        verify(objectMapper).writeValueAsString(testClient);
        verify(kafkaTemplate).send(eq("client-topic"), anyString(), eq(testClientJson));
    }

    @Test
    void publishClientShouldThrowExceptionWhenJsonProcessingFails() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Error converting to JSON") {});
        assertThrows(RuntimeException.class, () -> clientPublisherService.publishClient(testClient));
        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void publishClientShouldHandleKafkaError() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(testClient)).thenReturn(testClientJson);
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Kafka error"));
        assertThrows(RuntimeException.class, () -> clientPublisherService.publishClient(testClient));
        verify(objectMapper).writeValueAsString(testClient);
    }
} 