package dev.matheushnt.farma_vida.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "venda_itens")
@Getter
@Setter
public class VendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false)
    private BigDecimal precoUnitario;

    @Column(name = "elegivel_convenio", nullable = false)
    private Boolean elegivelConvenio;

    @Column(name = "valor_bruto")
    private BigDecimal valorBruto;

    @ManyToOne()
    @JoinColumn(name = "venda_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Venda venda;

    @ManyToOne()
    @JoinColumn(name = "medicamento_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Medicamento medicamento;

    @CreationTimestamp()
    @Column(name = "criado_em")
    private Instant criadoEm;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VendaItem vendaItem)) return false;
        return Objects.equals(id, vendaItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
