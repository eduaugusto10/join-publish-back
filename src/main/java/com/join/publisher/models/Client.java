package com.join.publisher.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "clients")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotBlank(message = "Nome � obrigat�rio")
    private String name;

    @Email(message = "E-mail inv�lido")
    @NotBlank(message = "E-mail � obrigat�rio")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "CPF � obrigat�rio")
    @Column(unique = true)
    private String cpf;

    @NotBlank(message = "Telefone � obrigat�rio")
    private String phone;

}
