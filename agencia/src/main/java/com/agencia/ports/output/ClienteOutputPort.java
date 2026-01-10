package com.agencia.ports.output;

import com.agencia.domain.model.Cliente;

import java.util.Optional;

/**
 * PORTA DE SAÍDA - Repositório de Cliente
 */
public interface ClienteOutputPort {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    Optional<Cliente> buscarPorCpf(String cpf);

}
