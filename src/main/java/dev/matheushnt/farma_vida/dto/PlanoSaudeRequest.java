package dev.matheushnt.farma_vida.dto;

import dev.matheushnt.farma_vida.enums.CategoriaMedicamento;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.Set;

public record PlanoSaudeRequest(

        @NotBlank(message = "O campo [nome] é obrigatório")
        String nome,

        @NotNull(message = "O campo [percentualDesconto] é obrigatório")
        @DecimalMin(value = "0.0", inclusive = true, message = "O campo [percentualDesconto] deve ser maior ou igual a 0.0")
        @DecimalMax(value = "1.0", inclusive = true, message = "O campo [percentualDesconto] deve ser menor ou igual a 1.0")
        BigDecimal percentualDesconto,

        @NotEmpty(message = "O campo [categoriasCobertas] é obrigatório e não pode ser uma lista vazia")
        Set<@NotNull(message = "A categoria não pode ser nula") CategoriaMedicamento> categoriasCobertas,

        @NotNull(message = "O campo [limiteMensal] é obrigatório")
        @Positive(message = "O campo [limiteMensal] deve ser maior do que 0")
        BigDecimal limiteMensal
) {}
