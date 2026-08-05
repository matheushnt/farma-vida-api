package dev.matheushnt.farma_vida.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record FaturaResponse(
        UUID faturaId,
        String competencia,
        BigDecimal valorTotal,
        int quantidadeVendas,
        UUID planoSaudeId
) {
}
