package dev.matheushnt.farma_vida.controller;

import dev.matheushnt.farma_vida.dto.ClienteRequest;
import dev.matheushnt.farma_vida.model.Cliente;
import dev.matheushnt.farma_vida.repository.ClienteRepository;
import dev.matheushnt.farma_vida.service.CreateClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private CreateClienteService createClienteService;

    @GetMapping()
    public ResponseEntity<List<Cliente>> listar() {
        List<Cliente> clientes = this.clienteRepository.findAll();
        return ResponseEntity.ok().body(clientes);
    }

    @PostMapping()
    public ResponseEntity<Map<String, UUID>> adicionar(@Valid @RequestBody ClienteRequest clienteRequest) {
        UUID clienteId = this.createClienteService.executar(clienteRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("clienteId", clienteId));
    }

}
