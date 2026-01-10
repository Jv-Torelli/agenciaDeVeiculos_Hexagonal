package com.agencia.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ENTIDADE DE DOMÍNIO - Veiculo
 * Esta classe representa o CORAÇÃO do negócio.
 * Contém apenas regras de negócio, sem dependências de frameworks.
 * Princípio: O domínio não conhece nada de fora (DB, REST, etc)
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {
    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String placa;
    private String cor;
    private BigDecimal preco;
    private Boolean disponivel;
    private LocalDateTime dataCadastro;

    /**
     * Regra de negócio: Marcar veículo como vendido
     */
    public void marcarComoVendido() {
        if (!this.disponivel) {
            throw new IllegalStateException("Veículo já foi vendido");
        }
        this.disponivel = false;
    }

    /**
     * Regra de negócio: Validações do domínio
     */
    public void validar() {
        int anoAtual = LocalDateTime.now().getYear();
        if (ano < 1950 || ano > anoAtual + 1) {
            throw new IllegalArgumentException("Ano inválido");
        }
        if (preco == null || preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("Marca é obrigatória");
        }
    }
}
