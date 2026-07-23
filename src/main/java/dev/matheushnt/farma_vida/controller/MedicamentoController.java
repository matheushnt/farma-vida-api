package dev.matheushnt.farma_vida.controller;

import dev.matheushnt.farma_vida.dto.MedicamentoRequest;
import dev.matheushnt.farma_vida.model.Medicamento;
import dev.matheushnt.farma_vida.repository.MedicamentoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/medicamento")
public class MedicamentoController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping()
    public ResponseEntity<List<Medicamento>> listar() {
        List<Medicamento> medicamentos = this.medicamentoRepository.findAll();
        return ResponseEntity.ok().body(medicamentos);
    }

    @PostMapping()
    public ResponseEntity<UUID> adicionar(@Valid @RequestBody MedicamentoRequest medicamentoRequest) {
        Medicamento medicamento = new Medicamento();
        medicamento.setNome(medicamentoRequest.nome());
        medicamento.setCategoria(medicamentoRequest.categoria());
        medicamento.setPreco(medicamentoRequest.preco());
        this.medicamentoRepository.save(medicamento);

        return ResponseEntity.status(HttpStatus.CREATED).body(medicamento.getId());
    }

}
