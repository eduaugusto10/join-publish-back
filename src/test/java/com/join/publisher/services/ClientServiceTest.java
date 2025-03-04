package com.join.publisher.services;

import com.join.publisher.mapper.ClientMapper;
import com.join.publisher.models.Client;
import com.join.publisher.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ClientPublisherService clientPublisherService;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testClient = Client.builder()
                .id(testId)
                .name("Test User")
                .email("test@example.com")
                .cpf("529.982.247-25")
                .phone("(11) 99999-9999")
                .build();
    }

    @Test
    void getAllShouldReturnListOfClients() {
        List<Client> expectedClients = Arrays.asList(testClient);
        when(clientRepository.findAll()).thenReturn(expectedClients);
        List<Client> actualClients = clientService.getAll();
        assertEquals(expectedClients, actualClients);
        verify(clientRepository).findAll();
    }

    @Test
    void findByIdShouldReturnClientWhenExists() {
        when(clientRepository.findById(testId)).thenReturn(Optional.of(testClient));
        List<Client> result = clientService.findClient(testId, null, null);
        assertFalse(result.isEmpty());
        assertEquals(testClient, result.get(0));
        verify(clientRepository).findById(testId);
    }

    @Test
    void findByIdShouldThrowExceptionWhenNotFound() {
        when(clientRepository.findById(testId)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,
                () -> clientService.findClient(testId, null, null));
        verify(clientRepository).findById(testId);
    }

    @Test
    void findByEmailShouldReturnClientWhenExists() {
        String email = "test@example.com";
        when(clientRepository.findByEmail(email)).thenReturn(Optional.of(testClient));
        List<Client> result = clientService.findClient(null, email, null);
        assertFalse(result.isEmpty());
        assertEquals(testClient, result.get(0));
        verify(clientRepository).findByEmail(email);
    }

    @Test
    void findByEmailShouldThrowExceptionWhenInvalidEmail() {
        String invalidEmail = "invalid-email";
        assertThrows(ResponseStatusException.class,
                () -> clientService.findClient(null, invalidEmail, null));
        verify(clientRepository, never()).findByEmail(any());
    }

    @Test
    void findByCpfShouldReturnClientWhenExists() {
        String cpf = "529.982.247-25";
        when(clientRepository.findByCpf(cpf)).thenReturn(Optional.of(testClient));
        List<Client> result = clientService.findClient(null, null, cpf);
        assertFalse(result.isEmpty());
        assertEquals(testClient, result.get(0));
        verify(clientRepository).findByCpf(cpf);
    }

    @Test
    void findByCpfShouldThrowExceptionWhenInvalidCpf() {
        String invalidCpf = "123";
        assertThrows(ResponseStatusException.class,
                () -> clientService.findClient(null, null, invalidCpf));
        verify(clientRepository, never()).findByCpf(any());
    }

    @Test
    void deleteShouldSucceedWhenClientExists() {
        when(clientRepository.findById(testId)).thenReturn(Optional.of(testClient));
        doNothing().when(clientRepository).delete(testClient);
        assertDoesNotThrow(() -> clientService.delete(testId));
        verify(clientRepository).findById(testId);
        verify(clientRepository).delete(testClient);
    }

    @Test
    void updateShouldSucceedWhenValidData() {
        Client updatedClient = Client.builder()
                .name("Updated Name")
                .email("updated@example.com")
                .cpf("987.654.321-00")
                .phone("(11) 88888-8888")
                .build();

        when(clientRepository.findById(testId)).thenReturn(Optional.of(testClient));
        when(clientRepository.save(any(Client.class))).thenReturn(updatedClient);
        Client result = clientService.update(testId, updatedClient);
        assertNotNull(result);
        verify(clientRepository).findById(testId);
        verify(clientMapper).toUpdate(any(), any());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void updateShouldThrowExceptionWhenInvalidEmail() {
        Client invalidClient = Client.builder()
                .name("Test")
                .email("invalid-email")
                .cpf("987.654.321-00")
                .phone("(11) 88888-8888")
                .build();
        assertThrows(ResponseStatusException.class,
                () -> clientService.update(testId, invalidClient));
        verify(clientRepository, never()).save(any());
        verify(clientPublisherService, never()).publishClient(any());
    }

    @Test
    void publishClientShouldSucceedWhenValidData() {
        Client newClient = Client.builder()
                .name("New Client")
                .email("new@example.com")
                .cpf("987.654.321-00")
                .phone("(11) 77777-7777")
                .build();

        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(clientRepository.findByCpf(anyString())).thenReturn(Optional.empty());
        doNothing().when(clientPublisherService).publishClient(any(Client.class));
        assertDoesNotThrow(() -> clientService.publishClient(newClient));
        verify(clientRepository).findByEmail(newClient.getEmail());
        verify(clientRepository).findByCpf(newClient.getCpf());
        verify(clientPublisherService).publishClient(newClient);
    }

    @Test
    void publishClientShouldThrowExceptionWhenEmailExists() {
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.of(testClient));
        assertThrows(ResponseStatusException.class,
                () -> clientService.publishClient(testClient));
        verify(clientPublisherService, never()).publishClient(any());
    }

    @Test
    void publishClientShouldThrowExceptionWhenCpfExists() {
        when(clientRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(clientRepository.findByCpf(anyString())).thenReturn(Optional.of(testClient));
        
        assertThrows(ResponseStatusException.class,
                () -> clientService.publishClient(testClient));
        verify(clientPublisherService, never()).publishClient(any());
    }
} 