package dev.matheushnt.farma_vida.model;

import dev.matheushnt.farma_vida.enums.CategoriaMedicamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medicamentos")
@Getter
@Setter
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private CategoriaMedicamento categoria;

    private BigDecimal preco;

    @CreationTimestamp()
    @Column(name = "criado_em")
    private Instant criadoEm;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Medicamento that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Medicamento{");
        sb.append("nome='").append(nome).append('\'');
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }

}
