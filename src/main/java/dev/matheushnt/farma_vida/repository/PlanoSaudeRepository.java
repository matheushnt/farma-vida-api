package dev.matheushnt.farma_vida.repository;

import dev.matheushnt.farma_vida.model.PlanoSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PlanoSaudeRepository extends JpaRepository<PlanoSaude, UUID> {
}
