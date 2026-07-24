package dev.matheushnt.farma_vida.controller;

import dev.matheushnt.farma_vida.dto.PlanoSaudeRequest;
import dev.matheushnt.farma_vida.model.PlanoSaude;
import dev.matheushnt.farma_vida.repository.PlanoSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/plano-saude")
public class PlanoSaudeController {

    @Autowired
    private PlanoSaudeRepository planoSaudeRepository;

    @GetMapping()
    public ResponseEntity<List<PlanoSaude>> listar() {
        List<PlanoSaude> planoSaudeList = this.planoSaudeRepository.findAll();
        return ResponseEntity.ok().body(planoSaudeList);
    }

    @PostMapping()
    public ResponseEntity<Map<String, UUID>> adicionar(@Valid @RequestBody PlanoSaudeRequest planoSaudeRequest) {
        PlanoSaude planoSaude = new PlanoSaude();
        planoSaude.setNome(planoSaudeRequest.nome());
        planoSaude.setPercentualDesconto(planoSaudeRequest.percentualDesconto());
        planoSaude.setCategoriasCobertas(planoSaudeRequest.categoriasCobertas());
        planoSaude.setLimiteMensal(planoSaudeRequest.limiteMensal());
        this.planoSaudeRepository.save(planoSaude);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("planoSaudeId", planoSaude.getId()));
    }

}
