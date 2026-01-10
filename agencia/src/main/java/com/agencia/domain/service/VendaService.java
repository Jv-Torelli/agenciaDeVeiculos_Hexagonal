package com.agencia.domain.service;

import com.agencia.domain.exception.VeiculoIndisponivelException;
import com.agencia.domain.model.Veiculo;
import com.agencia.domain.model.Venda;
import com.agencia.ports.input.VendaInputPort;
import com.agencia.ports.output.VeiculoOutputPort;
import com.agencia.ports.output.VendaOutputPort;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVIÇO DE DOMÍNIO - Venda
 * Orquestra o processo de venda de um veículo
 */
public class VendaService implements VendaInputPort {

    private final VendaOutputPort vendaOutputPort;
    private final VeiculoOutputPort veiculoOutputPort;

    public VendaService(VendaOutputPort vendaOutputPort,
                        VeiculoOutputPort veiculoOutputPort) {
        this.vendaOutputPort = vendaOutputPort;
        this.veiculoOutputPort = veiculoOutputPort;
    }

    @Override
    public Venda realizar(Venda venda) {
        // Validar dados da venda
        venda.validar();

        // Verificar se veículo está disponível
        if (!venda.getVeiculo().getDisponivel()) {
            throw new VeiculoIndisponivelException(venda.getVeiculo().getId());
        }

        // Marcar veículo como vendido (regra de negócio do domínio)
        Veiculo veiculo = venda.getVeiculo();
        veiculo.marcarComoVendido();
        veiculoOutputPort.salvar(veiculo);

        // Registrar venda
        Venda novaVenda = Venda.builder()
                .veiculo(venda.getVeiculo())
                .cliente(venda.getCliente())
                .valorVenda(venda.getValorVenda())
                .dataVenda(LocalDateTime.now())
                .build();

        return vendaOutputPort.salvar(novaVenda);
    }

    @Override
    public List<Venda> listarTodas() {
        return vendaOutputPort.listarTodas();
    }
}
