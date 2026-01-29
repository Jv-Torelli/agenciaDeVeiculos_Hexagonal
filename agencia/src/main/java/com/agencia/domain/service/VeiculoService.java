package com.agencia.domain.service;

import com.agencia.domain.exception.VeiculoNaoEncontradoException;
import com.agencia.domain.model.Veiculo;
import com.agencia.ports.input.VeiculoInputPort;
import com.agencia.ports.output.VeiculoOutputPort;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SERVIÇO DE DOMÍNIO
 * Esta classe contém a LÓGICA DE NEGÓCIO da aplicação.
 * Características importantes:
 * 1. IMPLEMENTA a porta de entrada (VeiculoInputPort)
 * 2. USA a porta de saída (VeiculoOutputPort) via construtor
 * 3. NÃO conhece detalhes de implementação (JPA, REST, etc)
 * 4. Contém as regras de negócio e orquestra as entidades
 * O Spring vai injetar automaticamente a implementação do repositório
 */
public class VeiculoService implements VeiculoInputPort {

    /**
     * Dependência da PORTA de saída (interface)
     * O service não sabe qual é a implementação concreta (JPA, MongoDB, etc)
     */
    private final VeiculoOutputPort veiculoOutputPort;

    /**
     * Injeção de dependência via construtor
     * Spring vai fornecer a implementação em tempo de execução
     */
    public VeiculoService(VeiculoOutputPort veiculoOutputPort) {
        this.veiculoOutputPort = veiculoOutputPort;
    }

    @Override
    public Veiculo cadastrar(Veiculo veiculo) {
        // Regra de negócio: validar dados
        veiculo.validar();

        // Regra de negócio: não pode cadastrar placa duplicada
        if (veiculoOutputPort.existePorPlaca(veiculo.getPlaca())) {
            throw new IllegalArgumentException("Já existe veículo com esta placa");
        }

        // Regra de negócio: novos veículos são disponíveis
        Veiculo novoVeiculo = Veiculo.builder()
                .marca(veiculo.getMarca())
                .modelo(veiculo.getModelo())
                .ano(veiculo.getAno())
                .placa(veiculo.getPlaca())
                .cor(veiculo.getCor())
                .preco(veiculo.getPreco())
                .disponivel(true)
                .dataCadastro(LocalDateTime.now())
                .build();

        return veiculoOutputPort.salvar(novoVeiculo);
    }

    @Override
    public Veiculo buscarPorId(Long id) {
        return veiculoOutputPort.buscarPorId(id)
                .orElseThrow(() -> new VeiculoNaoEncontradoException(id));
    }

    @Override
    public List<Veiculo> listarTodos() {
        return veiculoOutputPort.listarTodos();
    }

    @Override
    public List<Veiculo> listarDisponiveis() {
        return veiculoOutputPort.listarDisponiveis();
    }

    @Override
    public Veiculo atualizar(Long id, Veiculo veiculo) {
        // Verificar se existe
        Veiculo veiculoExistente = buscarPorId(id);

        // Validar novos dados
        veiculo.validar();

        // Atualizar campos
        Veiculo veiculoAtualizado = Veiculo.builder()
                .id(id)
                .marca(veiculo.getMarca())
                .modelo(veiculo.getModelo())
                .ano(veiculo.getAno())
                .placa(veiculo.getPlaca())
                .cor(veiculo.getCor())
                .preco(veiculo.getPreco())
                .disponivel(veiculoExistente.getDisponivel())
                .dataCadastro(veiculoExistente.getDataCadastro())
                .build();

        return veiculoOutputPort.salvar(veiculoAtualizado);
    }

    @Override
    public void deletar(Long id) {
        buscarPorId(id); // Verifica se existe
        veiculoOutputPort.deletar(id);
    }
}
