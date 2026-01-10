package com.agencia.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ENTIDADE DE DOMÍNIO - Cliente
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;
    private String email;

    /**
     * Regra de negócio: Validar CPF básico
     */
    public void validar() {
        if (cpf == null || cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
    }
}
