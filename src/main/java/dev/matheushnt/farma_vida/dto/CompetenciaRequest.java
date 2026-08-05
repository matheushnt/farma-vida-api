package dev.matheushnt.farma_vida.dto;

import jakarta.validation.constraints.NotNull;

import java.time.YearMonth;

public record CompetenciaRequest(
        @NotNull(message = "O campo [competencia] é obrigatório")
        YearMonth competencia
) {}
