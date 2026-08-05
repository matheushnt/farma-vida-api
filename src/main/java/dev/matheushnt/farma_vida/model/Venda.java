package dev.matheushnt.farma_vida.model;

import dev.matheushnt.farma_vida.enums.StatusVenda;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendas")
@Getter
@Setter
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreationTimestamp
    @Column(name = "data_venda", nullable = false, updatable = false)
    private LocalDate dataVenda;

    @Column(name = "usar_convenio", nullable = false)
    private Boolean usarConvenio = false;

    @Column(name = "valor_bruto", nullable = false)
    private BigDecimal valorBruto;

    @Column(name = "valor_pago_cliente", nullable = false)
    private BigDecimal valorPagoCliente;

    @Column(name = "valor_pago_convenio", nullable = false)
    private BigDecimal valorPagoConvenio;

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaItem> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusVenda status;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "fatura_id", nullable = true)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Fatura fatura;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    private Cliente cliente;

    @CreationTimestamp()
    @Column(name = "criado_em")
    private Instant criadoEm;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Venda venda)) return false;
        return Objects.equals(id, venda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
