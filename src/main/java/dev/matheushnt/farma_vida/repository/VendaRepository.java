package dev.matheushnt.farma_vida.repository;

import dev.matheushnt.farma_vida.model.Venda;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface VendaRepository extends JpaRepository<Venda, UUID> {

    @Query("""
                SELECT COALESCE(SUM(v.valorPagoConvenio), 0)
                FROM Venda v
                WHERE v.cliente.id = :clienteId
                    AND v.cliente.planoSaude.id = :planoSaudeId
                    AND v.status = dev.matheushnt.farma_vida.enums.StatusVenda.CONCLUIDA
                    AND v.dataVenda BETWEEN :inicio AND :fim
            """)
    BigDecimal calcularLimiteMensalConsumido(
            @Param("clienteId") UUID clienteId,
            @Param("planoSaudeId") UUID planoSaudeId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
                SELECT v
                FROM Venda v
                WHERE v.cliente.planoSaude.id = :planoSaudeId
                    AND v.fatura.id IS NULL
                    AND v.status = dev.matheushnt.farma_vida.enums.StatusVenda.CONCLUIDA
                    AND v.dataVenda BETWEEN :inicio AND :fim
            """)
    List<Venda> buscarVendasPendentesDeFaturamento(
            @Param("planoSaudeId") UUID planoSaudeId,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

}
