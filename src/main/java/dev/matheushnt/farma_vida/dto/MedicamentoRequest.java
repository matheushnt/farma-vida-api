package dev.matheushnt.farma_vida.dto;

import dev.matheushnt.farma_vida.enums.CategoriaMedicamento;
import dev.matheushnt.farma_vida.model.Medicamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MedicamentoRequest(

        @NotBlank(message = "O campo [nome] é obrigatório")
        String nome,

        @NotNull(message = "O campo [categoria] é obrigatório")
        CategoriaMedicamento categoria,

        @NotNull(message = "O campo [preco] é obrigatório")
        @Positive(message = "O campo [preco] deve ser um valor maior do que zero (0)")
        BigDecimal preco

) {}
