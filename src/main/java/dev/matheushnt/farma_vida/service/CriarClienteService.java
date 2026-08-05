package dev.matheushnt.farma_vida.service;

import dev.matheushnt.farma_vida.dto.ClienteRequest;
import dev.matheushnt.farma_vida.exception.CpfInvalidoException;
import dev.matheushnt.farma_vida.exception.RecursoEncontradoException;
import dev.matheushnt.farma_vida.exception.RecursoNaoEncontradoException;
import dev.matheushnt.farma_vida.model.Cliente;
import dev.matheushnt.farma_vida.model.PlanoSaude;
import dev.matheushnt.farma_vida.repository.ClienteRepository;
import dev.matheushnt.farma_vida.repository.PlanoSaudeRepository;
import dev.matheushnt.farma_vida.util.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CriarClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PlanoSaudeRepository planoSaudeRepository;

    public UUID executar(ClienteRequest clienteRequest) {
        if (!CPF.validar(clienteRequest.cpf())) {
            throw new CpfInvalidoException("CPF inválido");
        }

        Optional<Cliente> clienteExiste = this.clienteRepository.findByCpf(clienteRequest.cpf());

        if (clienteExiste.isPresent()) {
            throw new RecursoEncontradoException("Já existe um cliente cadastrado com o CPF " + CPF.censurar(clienteRequest.cpf()));
        }

        Cliente cliente = new Cliente();
        cliente.setNome(clienteRequest.nome());
        cliente.setCpf(clienteRequest.cpf());

        if (clienteRequest.planoSaudeId() != null) {
            Optional<PlanoSaude> planoSaudeExiste = this.planoSaudeRepository.findById(clienteRequest.planoSaudeId());

            if (planoSaudeExiste.isEmpty()) {
                throw new RecursoNaoEncontradoException("Plano de Saúde não encontrado");
            }

            PlanoSaude planoSaude = planoSaudeExiste.get();
            cliente.setPlanoSaude(planoSaude);
        }

        this.clienteRepository.save(cliente);

        return cliente.getId();
    }

}
