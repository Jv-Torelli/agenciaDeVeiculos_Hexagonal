package com.agencia.ports.input;

import com.agencia.domain.model.Veiculo;
import java.util.List;

/**
 * PORTA DE ENTRADA (Input Port / Driving Port)
 * Esta interface define os CASOS DE USO da aplicação.
 * Define O QUE a aplicação faz, sem dizer COMO.
 * Será implementada pelo SERVIÇO DE DOMÍNIO (VeiculoService)
 * Os ADAPTADORES DE ENTRADA (Controllers) usam esta interface
 * Princípio: Separação entre interface e implementação
 */
public interface VeiculoInputPort {

    /**
     * Cadastrar um novo veículo
     */
    Veiculo cadastrar(Veiculo veiculo);

    /**
     * Buscar veículo por ID
     */
    Veiculo buscarPorId(Long id);

    /**
     * Listar todos os veículos
     */
    List<Veiculo> listarTodos();

    /**
     * Listar apenas veículos disponíveis
     */
    List<Veiculo> listarDisponiveis();

    /**
     * Atualizar veículo
     */
    Veiculo atualizar(Long id, Veiculo veiculo);

    /**
     * Deletar veículo
     */
    void deletar(Long id);
}
