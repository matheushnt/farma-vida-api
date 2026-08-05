package dev.matheushnt.farma_vida.controller;

import dev.matheushnt.farma_vida.dto.ItemResponse;
import dev.matheushnt.farma_vida.dto.VendaRequest;
import dev.matheushnt.farma_vida.dto.VendaResponse;
import dev.matheushnt.farma_vida.exception.RecursoNaoEncontradoException;
import dev.matheushnt.farma_vida.model.Venda;
import dev.matheushnt.farma_vida.repository.VendaRepository;
import dev.matheushnt.farma_vida.service.RegistrarVendaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private RegistrarVendaService registrarVendaService;

    @PostMapping()
    public ResponseEntity<VendaResponse> adicionar(@Valid @RequestBody VendaRequest vendaRequest) {
        Venda venda = this.registrarVendaService.registrar(vendaRequest);
        VendaResponse vendaResponse = this.criarVendaResponse(venda);

        return ResponseEntity.status(HttpStatus.CREATED).body(vendaResponse);
    }

    @GetMapping("/{vendaId}")
    public ResponseEntity<VendaResponse> obterDetalhesVenda(@PathVariable UUID vendaId) {
        Venda venda = this.vendaRepository.findById(vendaId).orElseThrow(() -> new RecursoNaoEncontradoException("Venda não encontrada"));
        VendaResponse vendaResponse = this.criarVendaResponse(venda);

        return ResponseEntity.ok().body(vendaResponse);
    }

    private VendaResponse criarVendaResponse(Venda venda) {
        List<ItemResponse> vendaItens = venda.getItens().stream()
                .map((item) -> new ItemResponse(
                        item.getMedicamento().getId(),
                        item.getQuantidade(),
                        item.getElegivelConvenio(),
                        item.getValorBruto(),
                        item.getPrecoUnitario()
                ))
                .toList();

        return new VendaResponse(
                venda.getId(),
                venda.getCliente().getId(),
                venda.getStatus(),
                venda.getValorBruto(),
                venda.getValorPagoCliente(),
                venda.getValorPagoConvenio(),
                vendaItens
        );
    }

}
