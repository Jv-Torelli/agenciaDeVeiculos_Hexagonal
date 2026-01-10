package com.agencia.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ENTIDADE DE DOMÍNIO - Venda
 * Representa a transação de venda de um veículo para um cliente
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venda {
    private Long id;
    private Veiculo veiculo;
    private Cliente cliente;
    private BigDecimal valorVenda;
    private LocalDateTime dataVenda;

    /**
     * Regra de negócio: Validar venda
     */
    public void validar() {
        if (veiculo == null) {
            throw new IllegalArgumentException("Veículo é obrigatório");
        }
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        if (valorVenda == null || valorVenda.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de venda inválido");
        }
    }
}
