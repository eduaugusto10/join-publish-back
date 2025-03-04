package com.join.publisher.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user.name@domain.com",
        "user+tag@domain.co.uk",
        "first.last@subdomain.domain.org"
    })
    void isEmailShouldReturnTrueForValidEmails(String email) {
        assertTrue(Utils.isEmail(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "user@",
        "@domain.com",
        "user@domain",
        "user.domain.com",
        ""
    })
    void isEmailShouldReturnFalseForInvalidEmails(String email) {
        assertFalse(Utils.isEmail(email));
    }

    @Test
    void isEmailShouldReturnFalseForNull() {
        assertFalse(Utils.isEmail(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "529.982.247-25",
        "111.222.333-96",
        "123.456.789-09"
    })
    void isCpfShouldReturnTrueForValidCpfs(String cpf) {
        assertTrue(Utils.isCpf(cpf));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "111.111.111-11",
        "123.456.789-00",
        "000.000.000-00",
        "999.999.999-99",
        "123.456.789",
        "12345678900",
        "abc.def.ghi-jk",
        ""
    })
    void isCpfShouldReturnFalseForInvalidCpfs(String cpf) {
        assertFalse(Utils.isCpf(cpf));
    }

    @Test
    void isCpfShouldReturnFalseForNull() {
        assertFalse(Utils.isCpf(null));
    }

    @Test
    void isCpfShouldReturnFalseForCpfWithWrongFormat() {
        assertFalse(Utils.isCpf("12345678901"));
    }

    @Test
    void isCpfShouldReturnFalseForCpfWithInvalidCharacters() {
        assertFalse(Utils.isCpf("abc.def.ghi-jk"));
    }
} 