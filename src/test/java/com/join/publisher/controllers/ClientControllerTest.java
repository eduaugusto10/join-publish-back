package com.join.publisher.controllers;

import com.join.publisher.models.Client;
import com.join.publisher.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private Client testClient;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testClient = Client.builder()
                .id(testId)
                .name("Test User")
                .email("test@example.com")
                .cpf("123.456.789-00")
                .phone("(11) 99999-9999")
                .build();
    }

    @Test
    void createShouldReturnCreatedStatus() {
        doNothing().when(clientService).publishClient(any(Client.class));
        ResponseEntity<?> response = clientController.create(testClient);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(clientService).publishClient(testClient);
    }

    @Test
    void findClientShouldReturnListWhenSearchingById() {
        List<Client> expectedClients = Arrays.asList(testClient);
        when(clientService.findClient(testId, null, null)).thenReturn(expectedClients);
        ResponseEntity<List<Client>> response = clientController.findClient(testId, null, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClients, response.getBody());
        verify(clientService).findClient(testId, null, null);
    }

    @Test
    void findClientShouldReturnListWhenSearchingByEmail() {
        List<Client> expectedClients = Arrays.asList(testClient);
        String email = "test@example.com";
        when(clientService.findClient(null, email, null)).thenReturn(expectedClients);
        ResponseEntity<List<Client>> response = clientController.findClient(null, null,email);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClients, response.getBody());
        verify(clientService).findClient(null, email, null);
    }

    @Test
    void findClientShouldReturnListWhenSearchingByCpf() {
        List<Client> expectedClients = Arrays.asList(testClient);
        String cpf = "529.982.247-25";
        when(clientService.findClient(null, null, cpf)).thenReturn(expectedClients);
        ResponseEntity<List<Client>> response = clientController.findClient(null, cpf, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedClients, response.getBody());
        verify(clientService).findClient(null, null, cpf);
    }

    @Test
    void updateShouldReturnUpdatedClient() {
        Client updatedClient = Client.builder()
                .id(testId)
                .name("Updated Name")
                .email("updated@example.com")
                .cpf("987.654.321-00")
                .phone("(11) 88888-8888")
                .build();

        when(clientService.update(testId, testClient)).thenReturn(updatedClient);
        ResponseEntity<Client> response = clientController.update(testId, testClient);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedClient, response.getBody());
        verify(clientService).update(testId, testClient);
    }

    @Test
    void updateShouldHandleServiceException() {
        when(clientService.update(testId, testClient))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid data"));
        assertThrows(ResponseStatusException.class,
                () -> clientController.update(testId, testClient));
        verify(clientService).update(testId, testClient);
    }

    @Test
    void deleteShouldReturnNoContent() {
        doNothing().when(clientService).delete(testId);
        ResponseEntity<?> response = clientController.delete(testId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService).delete(testId);
    }

    @Test
    void deleteShouldHandleServiceException() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"))
                .when(clientService).delete(testId);
        assertThrows(ResponseStatusException.class,
                () -> clientController.delete(testId));
        verify(clientService).delete(testId);
    }
} 