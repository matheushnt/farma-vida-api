package dev.matheushnt.farma_vida.controller;

import dev.matheushnt.farma_vida.dto.CompetenciaRequest;
import dev.matheushnt.farma_vida.dto.FechamentoFaturaResultado;
import dev.matheushnt.farma_vida.service.FecharFaturaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/fatura")
public class FaturaController {

    @Autowired
    private FecharFaturaService fecharFaturaService;

    @PostMapping("/{planoSaudeId}")
    public ResponseEntity<Map<String, UUID>> fechar(@PathVariable UUID planoSaudeId, @Valid @RequestBody CompetenciaRequest competenciaRequest) {
        FechamentoFaturaResultado resultado = this.fecharFaturaService.fechar(planoSaudeId, competenciaRequest);

        return resultado.criadaAgora()
            ? ResponseEntity.status(HttpStatus.CREATED).body(Map.of("faturaId", resultado.fatura().getId()))
            : ResponseEntity.status(HttpStatus.OK).body(Map.of("faturaId", resultado.fatura().getId()));
    }

}
