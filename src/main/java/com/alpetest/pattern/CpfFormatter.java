package com.alpetest.pattern;

import org.springframework.stereotype.Component;

@Component
public class CpfFormatter {

    public String CpfFormater(String cpf) {
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
