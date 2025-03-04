package com.join.publisher.services;

import com.join.publisher.mapper.ClientMapper;
import com.join.publisher.models.Client;
import com.join.publisher.repositories.ClientRepository;
import com.join.publisher.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientPublisherService clientPublisherService;

    public List<Client> getAll() {

        log.info("Buscando todos os clientes");
        return clientRepository.findAll();
    }

    public List<Client> findClient(UUID id, String email, String cpf) {
        if (id != null) {
            return List.of(this.findById(id));
        } else if (email != null) {
            return List.of(this.findByEmail(email));
        } else if (cpf != null) {
            return List.of(this.findByCpf(cpf));
        } else {
            return this.getAll();
        }
    }

    private Client findById(UUID id) {
        log.info("Buscando cliente com id: {}", id);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado com id: " + id));
    }

    private Client findByEmail(String email) {
        log.info("Buscando cliente com e-mail: {}", email);
        if (!Utils.isEmail(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "E-mail invalido");
        }
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado com e-mail: " + email));
    }

    private Client findByCpf(String cpf) {
        log.info("Bsucando cliente com cpf: {}", cpf);
        if (!Utils.isCpf(cpf)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CPF invalido");
        }
        return clientRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente nao encontrado com cpf: " + cpf));
    }

    public void delete(UUID id) {
        var client = this.findById(id);
        log.info("Deletando cliente: {}", id);
        clientRepository.delete(client);
        log.info("Deletado cliente com sucesso: {}", id);
    }

    public Client update(UUID id, Client client) {
        if (!Utils.isEmail(client.getEmail())) {
            log.error("E-mail invalid: {}", client.getEmail());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "E-mail invalido");
        }
        
        String cpf = client.getCpf();
        if (cpf == null || cpf.replaceAll("\\D","").length() != 11) {
            log.error("CPF invalido: {}", cpf);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CPF deve ter 11 dígitos");
        }
        
        if (!Utils.isCpf(cpf)) {
            log.error("CPF invalido: {}", cpf);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CPF invalido");
        }
        
        var existingClient = this.findById(id);
        clientMapper.toUpdate(existingClient, client);
        log.info("Atualizando cliente: {}", existingClient);
        return clientRepository.save(existingClient);
    }

    private void clientExists(Client client) {
        var clientByEmail = clientRepository.findByEmail(client.getEmail());
        if (clientByEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "E-mail já cadastrado");
        }

        var clientByCpf = clientRepository.findByCpf(client.getCpf());
        if (clientByCpf.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CPF já cadastrado");
        }
    }

    public void publishClient(Client client) {
        if (!Utils.isEmail(client.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "E-mail invalido");
        }
        
        String cpf = client.getCpf();
        if (cpf == null || cpf.replaceAll("\\D","").length() != 11) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CPF deve ter 11 dígitos");
        }
        
        if (!Utils.isCpf(cpf)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "CPF invalido");
        }
        
        clientExists(client);
        log.info("Enviando cliente para criacao: {}", client);
        clientPublisherService.publishClient(client);
        log.info("Criado cliente: {}", client);
    }
}
