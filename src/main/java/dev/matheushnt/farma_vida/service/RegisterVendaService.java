package dev.matheushnt.farma_vida.service;

import dev.matheushnt.farma_vida.dto.ItemRequest;
import dev.matheushnt.farma_vida.dto.VendaRequest;
import dev.matheushnt.farma_vida.enums.StatusVenda;
import dev.matheushnt.farma_vida.exception.RecursoNaoEncontradoException;
import dev.matheushnt.farma_vida.model.*;
import dev.matheushnt.farma_vida.repository.ClienteRepository;
import dev.matheushnt.farma_vida.repository.MedicamentoRepository;
import dev.matheushnt.farma_vida.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegisterVendaService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Transactional
    public Venda registrar(VendaRequest vendaRequest) {
        Venda venda = new Venda();

        Cliente cliente = this.clienteRepository.findById(vendaRequest.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado"));

        venda.setCliente(cliente);
        venda.setUsarConvenio(vendaRequest.usarConvenio());
        PlanoSaude planoSaude = cliente.getPlanoSaude();

        boolean convenioIndisponivel = vendaRequest.usarConvenio() && (planoSaude == null || !planoSaude.getAtivo());

        if (convenioIndisponivel) {
            venda.setValorBruto(BigDecimal.ZERO);
            venda.setValorPagoCliente(BigDecimal.ZERO);
            venda.setValorPagoConvenio(BigDecimal.ZERO);
            venda.setStatus(StatusVenda.RECUSADA);
            this.vendaRepository.save(venda);

            return venda;
        }

        List<VendaItem> vendaItems = this.criarVendaItems(vendaRequest, planoSaude, venda, this.buscarMedicamentos(vendaRequest));

        BigDecimal valorBrutoTotal = vendaItems.stream()
                .map(VendaItem::getValorBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal valorPagoConvenio = vendaRequest.usarConvenio()
                ? this.calcularValorEfetivamenteCobertoConvenio(
                        this.calcularSaldoRestante(cliente, planoSaude),
                        this.calcularValorCobertoConvenio(planoSaude, vendaItems))
                : BigDecimal.ZERO;

        venda.setValorBruto(valorBrutoTotal);
        venda.setValorPagoConvenio(valorPagoConvenio);
        venda.setValorPagoCliente(venda.getValorBruto().subtract(venda.getValorPagoConvenio()));
        venda.setStatus(StatusVenda.CONCLUIDA);
        venda.setItens(vendaItems);
        this.vendaRepository.save(venda);

        return venda;
    }

    private Map<UUID, Medicamento> buscarMedicamentos(VendaRequest vendaRequest) {
        List<UUID> medicamentoIds = vendaRequest.itens().stream()
                .map(ItemRequest::medicamentoId)
                .toList();
        List<Medicamento> medicamentos = this.medicamentoRepository.findAllById(medicamentoIds);

        if (medicamentos.size() != medicamentoIds.size()) {
            throw new RecursoNaoEncontradoException("Um ou mais medicamentos informados não foram encontrados");
        }

        return medicamentos.stream().collect(Collectors.toMap(Medicamento::getId, m -> m));
    }

    private List<VendaItem> criarVendaItems(VendaRequest vendaRequest, PlanoSaude planoSaude, Venda venda, Map<UUID, Medicamento> medicamentoMap) {
        boolean usarConvenio = vendaRequest.usarConvenio();

        return vendaRequest.itens().stream()
                .map((item) -> {
                    VendaItem vendaItem = new VendaItem();
                    Medicamento medicamento = medicamentoMap.get(item.medicamentoId());
                    vendaItem.setVenda(venda);
                    vendaItem.setMedicamento(medicamento);

                    BigDecimal valorBrutoPorItem = medicamento.getPreco().multiply(BigDecimal.valueOf(item.quantidade()));

                    vendaItem.setQuantidade(item.quantidade());
                    vendaItem.setValorBruto(valorBrutoPorItem);
                    vendaItem.setPrecoUnitario(medicamento.getPreco());

                    boolean elegivel = usarConvenio && planoSaude != null && planoSaude.getCategoriasCobertas().contains(medicamento.getCategoria());
                    vendaItem.setElegivelConvenio(elegivel);

                    return vendaItem;
                })
                .toList();
    }

    private BigDecimal calcularValorEfetivamenteCobertoConvenio(BigDecimal saldoRestante, BigDecimal valorTotalCobertoConvenio) {
        return valorTotalCobertoConvenio.compareTo(saldoRestante) <= 0
                ? valorTotalCobertoConvenio
                : saldoRestante;
    }

    private BigDecimal calcularValorCobertoConvenio(PlanoSaude planoSaude, List<VendaItem> vendaItems) {
        return vendaItems.stream()
                .filter(VendaItem::getElegivelConvenio)
                .map(VendaItem::getValorBruto)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .multiply(planoSaude.getPercentualDesconto());
    }

    private BigDecimal calcularSaldoRestante(Cliente cliente, PlanoSaude planoSaude) {
        Instant agora = Instant.now();
        Instant inicioMes = YearMonth.now().atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
        BigDecimal limiteConsumido = vendaRepository.calcularLimiteMensalConsumido(cliente.getId(), planoSaude.getId(), inicioMes, agora);

        return planoSaude.getLimiteMensal().subtract(limiteConsumido);
    }

}
