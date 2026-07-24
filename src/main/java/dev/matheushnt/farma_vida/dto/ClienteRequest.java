package dev.matheushnt.farma_vida.dto;

import dev.matheushnt.farma_vida.enums.CategoriaMedicamento;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.UUID;

public record ClienteRequest(

        @NotBlank(message = "O campo [nome] é obrigatório")
        String nome,

        @NotBlank(message = "O campo [cpf] é obrigatório")
        @Size(min = 11, max = 11, message = "O campo [cpf] deve ter exatamento 11 caracteres")
        String cpf,

        @Nullable()
        UUID planoSaudeId

) {
}
