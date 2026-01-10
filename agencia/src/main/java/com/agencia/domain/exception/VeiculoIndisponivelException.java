package com.agencia.domain.exception;

public class VeiculoIndisponivelException extends RuntimeException {
    public VeiculoIndisponivelException(Long id) {
        super("Veículo indisponível para venda: " + id);
    }
}
