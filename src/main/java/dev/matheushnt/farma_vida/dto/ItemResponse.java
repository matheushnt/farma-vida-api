package dev.matheushnt.farma_vida.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemResponse(
        UUID medicamentoId,
        int quantidade,
        boolean elegivelConvenio,
        BigDecimal valorBruto,
        BigDecimal precoUnitario
) {}
