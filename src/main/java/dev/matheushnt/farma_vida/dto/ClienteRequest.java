package dev.matheushnt.farma_vida.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
