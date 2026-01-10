package com.agencia.domain.exception;

/**
 * EXCEÇÃO DE DOMÍNIO
 * Exceções específicas do negócio devem estar no domínio
 */
public class VeiculoNaoEncontradoException extends RuntimeException {
    public VeiculoNaoEncontradoException(Long id) {
        super("Veículo não encontrado com ID: " + id);
    }
}
