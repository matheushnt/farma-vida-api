package dev.matheushnt.farma_vida.util;

public class CPF {
    public static boolean validar(String cpf) {
        cpf = CPF.limpar(cpf);

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.chars().distinct().count() == 1) {
            return false;
        }

        int primeiroDigito = calcularDigitoVerificador(cpf.substring(0, 9));
        int segundoDigito = calcularDigitoVerificador(cpf.substring(0, 9) + primeiroDigito);

        return cpf.equals(cpf.substring(0, 9) + primeiroDigito + segundoDigito);
    }

    public static String formatar(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return "";
        }

        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }

        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }

    public static String limpar(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return "";
        }

        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }

        return cpf.replaceAll("\\D", "");
    }

    public static String censurar(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return "";
        }

        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF deve ter 11 dígitos");
        }

        var cpfLimpo = CPF.limpar(cpf);

        return cpfLimpo.replaceAll("(\\d{3})\\d{5}(\\d{3})", "$1***$2");
    }

    private static int calcularDigitoVerificador(String base) {
        int peso = base.length() + 1;
        int soma = 0;

        for (char c : base.toCharArray()) {
            soma += Character.getNumericValue(c) * peso;
            peso--;
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : 11 - resto;
    }

}
