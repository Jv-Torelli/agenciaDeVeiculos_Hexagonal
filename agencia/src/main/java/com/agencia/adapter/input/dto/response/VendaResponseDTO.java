package com.agencia.adapter.input.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para retornar dados de uma venda
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendaResponseDTO {
    private Long id;
    private VeiculoResponseDTO veiculo;
    private ClienteResponseDTO cliente;
    private BigDecimal valorVenda;
    private LocalDateTime dataVenda;
}
