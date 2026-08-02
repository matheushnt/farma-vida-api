package dev.matheushnt.farma_vida.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record ItemRequest(
        @NotNull(message = "O campo [medicamentoId] é obrigatório")
        UUID medicamentoId,

        @NotNull(message = "O campo [quantidade] é obrigatório")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade
) {}
