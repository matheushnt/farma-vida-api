package dev.matheushnt.farma_vida.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faturas")
@Getter
@Setter
public class Fatura {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "CHAR(6)")
    private String competencia;

    @Column(name = "valor_total", nullable = false)
    private BigDecimal valorTotal;

    @Column(name = "quantidade_vendas", nullable = false)
    private Integer quantidadeVendas;

    @ManyToOne()
    @JoinColumn(name = "plano_saude_id", nullable = false)
    private PlanoSaude planoSaude;

    @OneToMany(mappedBy = "fatura")
    private List<Venda> vendas = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Fatura fatura)) return false;
        return Objects.equals(id, fatura.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
