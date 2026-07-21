package dev.matheushnt.farma_vida.model;

import dev.matheushnt.farma_vida.enums.CategoriaMedicamento;
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
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "planos_saude")
@Getter
@Setter
public class PlanoSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(name = "percentual_desconto")
    private BigDecimal percentualDesconto;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "plano_saude_categorias_cobertas",
            joinColumns = @JoinColumn(name = "plano_saude_id")
    )
    @Column(name = "categoria_medicamento")
    private Set<CategoriaMedicamento> categoriasCobertas;

    @Column(name = "limite_mensal")
    private BigDecimal limiteMensal;

    @Column(columnDefinition = "BOOLEAN NOT NULL DEFAULT TRUE")
    private Boolean ativo = true;

    @CreationTimestamp()
    @Column(name = "criado_em")
    private Instant criadoEm;

    @OneToMany(mappedBy = "planoSaude")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private List<Cliente> clientes = new ArrayList<>();

    @OneToMany(mappedBy = "planoSaude")
    private List<Fatura> faturas = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlanoSaude that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlanoSaude{");
        sb.append("id=").append(id);
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", ativo=").append(ativo);
        sb.append('}');
        return sb.toString();
    }

}
