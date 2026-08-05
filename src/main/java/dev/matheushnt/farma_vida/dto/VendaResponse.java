package dev.matheushnt.farma_vida.dto;

import dev.matheushnt.farma_vida.enums.StatusVenda;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record VendaResponse(
        UUID vendaId,
        UUID clienteId,
        StatusVenda status,
        BigDecimal valorBruto,
        BigDecimal valorPagoCliente,
        BigDecimal valorPagoConvenio,
        List<ItemResponse> itens
) {}
