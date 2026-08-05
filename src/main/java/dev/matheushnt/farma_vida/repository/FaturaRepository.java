package dev.matheushnt.farma_vida.repository;

import dev.matheushnt.farma_vida.model.Fatura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FaturaRepository extends JpaRepository<Fatura, UUID> {

    Optional<Fatura> findByPlanoSaudeIdAndCompetencia(UUID planoSaudeId, String competencia);

}
