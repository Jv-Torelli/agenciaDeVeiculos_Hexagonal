package com.agencia.ports.output;

import com.agencia.domain.model.Veiculo;
import java.util.List;
import java.util.Optional;

/**
 * PORTA DE SAÍDA (Output Port / Driven Port)
 * Esta é uma INTERFACE que define o CONTRATO de persistência.
 * O DOMÍNIO define o que precisa, mas não sabe COMO é implementado.
 * Será implementada por um ADAPTADOR (ex: JPA, MongoDB, arquivo, etc)
 * Princípio: Inversão de Dependência - o domínio define, adapters implementam
 */
public interface VeiculoOutputPort {

    /**
     * Salvar ou atualizar um veículo
     */
    Veiculo salvar(Veiculo veiculo);

    /**
     * Buscar veículo por ID
     */
    Optional<Veiculo> buscarPorId(Long id);

    /**
     * Listar todos os veículos
     */
    List<Veiculo> listarTodos();

    /**
     * Listar apenas veículos disponíveis
     */
    List<Veiculo> listarDisponiveis();

    /**
     * Deletar veículo
     */
    void deletar(Long id);

    /**
     * Verificar se existe por placa
     */
    boolean existePorPlaca(String placa);
}
