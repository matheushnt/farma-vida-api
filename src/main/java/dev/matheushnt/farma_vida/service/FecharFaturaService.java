package dev.matheushnt.farma_vida.service;

import dev.matheushnt.farma_vida.dto.CompetenciaRequest;
import dev.matheushnt.farma_vida.dto.FechamentoFaturaResultado;
import dev.matheushnt.farma_vida.exception.NenhumaVendaParaFaturarException;
import dev.matheushnt.farma_vida.exception.RecursoNaoEncontradoException;
import dev.matheushnt.farma_vida.model.Fatura;
import dev.matheushnt.farma_vida.model.PlanoSaude;
import dev.matheushnt.farma_vida.model.Venda;
import dev.matheushnt.farma_vida.repository.FaturaRepository;
import dev.matheushnt.farma_vida.repository.PlanoSaudeRepository;
import dev.matheushnt.farma_vida.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class FecharFaturaService {

    @Autowired
    private PlanoSaudeRepository planoSaudeRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private FaturaRepository faturaRepository;

    @Transactional
    public FechamentoFaturaResultado fechar(UUID planoSaudeId, CompetenciaRequest competenciaRequest) {
        YearMonth competencia = competenciaRequest.competencia();
        String competenciaStr = competencia.format(DateTimeFormatter.ofPattern("yyyyMM"));
        LocalDate inicioMes = competencia.atDay(1).atStartOfDay().toLocalDate();
        LocalDate fimMes = competencia.atEndOfMonth();

        List<Venda> vendas = this.vendaRepository.buscarVendasPendentesDeFaturamento(planoSaudeId, inicioMes, fimMes);

        if (vendas.isEmpty()) {
            Fatura faturaExistente = this.faturaRepository.findByPlanoSaudeIdAndCompetencia(planoSaudeId, competenciaStr)
                    .orElseThrow(() -> new NenhumaVendaParaFaturarException(
                            "Não há vendas pendentes de faturamento para este plano nesta competência")
                    );

            return new FechamentoFaturaResultado(faturaExistente, false);
        }

        PlanoSaude planoSaude = this.planoSaudeRepository.findById(planoSaudeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano de Saúde não encontrado"));

        Fatura fatura = new Fatura();
        fatura.setPlanoSaude(planoSaude);
        fatura.setVendas(vendas);
        fatura.setValorTotal(vendas.stream().map(Venda::getValorPagoConvenio).reduce(BigDecimal.ZERO, BigDecimal::add));
        fatura.setQuantidadeVendas(vendas.size());
        fatura.setCompetencia(competenciaStr);
        this.faturaRepository.save(fatura);

        return new FechamentoFaturaResultado(fatura, true);
    }

}
