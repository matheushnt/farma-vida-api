package dev.matheushnt.farma_vida.controller;

import dev.matheushnt.farma_vida.dto.ItemResponse;
import dev.matheushnt.farma_vida.dto.VendaRequest;
import dev.matheushnt.farma_vida.dto.VendaResponse;
import dev.matheushnt.farma_vida.model.Venda;
import dev.matheushnt.farma_vida.service.RegisterVendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private RegisterVendaService registerVendaService;

    @PostMapping()
    public ResponseEntity<VendaResponse> adicionar(@Valid @RequestBody VendaRequest vendaRequest) {
        Venda venda = this.registerVendaService.registrar(vendaRequest);

        List<ItemResponse> vendaItens = venda.getItens().stream()
                .map((item) -> new ItemResponse(
                        item.getMedicamento().getId(),
                        item.getQuantidade(),
                        item.getElegivelConvenio(),
                        item.getValorBruto(),
                        item.getPrecoUnitario()
                ))
                .toList();

        VendaResponse vendaResponse = new VendaResponse(
                venda.getId(),
                venda.getCliente().getId(),
                venda.getStatus(),
                venda.getValorBruto(),
                venda.getValorPagoCliente(),
                venda.getValorPagoConvenio(),
                vendaItens
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(vendaResponse);
    }

}
