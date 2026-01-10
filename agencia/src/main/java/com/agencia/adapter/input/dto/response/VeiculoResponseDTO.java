package com.agencia.adapter.input.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO DE RESPONSE
 * Objeto usado para enviar dados pela API REST.
 * Representa como os dados serão serializados em JSON.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoResponseDTO {
    private Long id;
    private String marca;
    private String modelo;
    private Integer ano;
    private String placa;
    private String cor;
    private BigDecimal preco;
    private Boolean disponivel;
    private LocalDateTime dataCadastro;
}
