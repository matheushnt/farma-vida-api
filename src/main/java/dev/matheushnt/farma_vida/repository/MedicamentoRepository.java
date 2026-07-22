package dev.matheushnt.farma_vida.repository;

import dev.matheushnt.farma_vida.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicamentoRepository extends JpaRepository<Medicamento, UUID> {
}
