package com.join.publisher.utils;

import java.util.regex.Pattern;

public class Utils {

    public static Boolean isEmail(String email) {
        var regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (email == null) {
            return false;
        }

        return Pattern.matches(regex, email);
    }

    public static Boolean isCpf(String cpf) {
        if(cpf == null || cpf.isEmpty()){
            return false;
        }
        cpf = cpf.replaceAll("\\D","");
        if (cpf.length() != 11) {
            return false;
        }
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += (cpf.charAt(i) - '0') * (10 - i);
        }
        int digito1 = 11 - (soma % 11);
        if (digito1 > 9) digito1 = 0;
        
        if (digito1 != (cpf.charAt(9) - '0')) {
            return false;
        }
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += (cpf.charAt(i) - '0') * (11 - i);
        }
        int digito2 = 11 - (soma % 11);
        if (digito2 > 9) digito2 = 0;
        return digito2 == (cpf.charAt(10) - '0');
    }
}
