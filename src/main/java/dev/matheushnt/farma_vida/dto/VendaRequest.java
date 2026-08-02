package dev.matheushnt.farma_vida.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record VendaRequest(

        @NotNull(message = "O campo [usarConvenio] é obrigatório")
        Boolean usarConvenio,

        @NotNull(message = "O campo [clienteId] é obrigatório")
        UUID clienteId,

        @NotEmpty(message = "A venda precisa ter ao menos um item")
        @Valid
        List<ItemRequest> itens

) {}
