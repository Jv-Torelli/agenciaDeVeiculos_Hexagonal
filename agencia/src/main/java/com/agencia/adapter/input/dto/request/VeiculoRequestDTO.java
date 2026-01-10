package com.agencia.adapter.input.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * DTO DE REQUEST (Data Transfer Object)
 * Objeto usado para receber dados da API REST.
 * Contém validações do Bean Validation (Jakarta Validation).
 * IMPORTANTE: Este DTO é diferente do modelo de domínio!
 * - VeiculoRequestDTO: específico para API REST, com anotações de validação
 * - Veiculo (domínio): independente de tecnologia
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoRequestDTO {

    @NotBlank(message = "Marca é obrigatória")
    @Size(max = 50, message = "Marca deve ter no máximo 50 caracteres")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
    private String modelo;

    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1950, message = "Ano deve ser maior ou igual a 1950")
    @Max(value = 2026, message = "Ano deve ser menor ou igual a 2026")
    private Integer ano;

    @NotBlank(message = "Placa é obrigatória")
    @Pattern(regexp = "[A-Z]{3}-?[0-9][A-Z0-9][0-9]{2}",
            message = "Placa inválida")
    private String placa;

    @Size(max = 30, message = "Cor deve ter no máximo 30 caracteres")
    private String cor;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;
}
