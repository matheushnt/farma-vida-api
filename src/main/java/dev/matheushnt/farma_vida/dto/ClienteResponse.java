package dev.matheushnt.farma_vida.dto;

import java.util.UUID;

public record ClienteResponse(
        UUID clienteId,
        String nome,
        String cpf,
        UUID planoSaudeId
) {}
