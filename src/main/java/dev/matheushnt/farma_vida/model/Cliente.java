package dev.matheushnt.farma_vida.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(unique = true, length = 11, columnDefinition = "CHAR(11)")
    private String cpf;

    @ManyToOne()
    @JoinColumn(name = "plano_saude_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private PlanoSaude planoSaude;

    @OneToMany(mappedBy = "cliente")
    private List<Venda> vendas = new ArrayList<>();

    @CreationTimestamp()
    @Column(name = "criado_em")
    private Instant criadoEm;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cliente cliente)) return false;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Cliente{");
        sb.append("id=").append(id);
        sb.append(", nome='").append(nome).append('\'');
        sb.append(", cpf='").append(cpf).append('\'');
        sb.append(", planoSaude='").append(planoSaude).append('\'');
        sb.append(", criadoEm=").append(criadoEm);
        sb.append('}');
        return sb.toString();
    }

}
