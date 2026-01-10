package com.agencia.adapter.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * ENTIDADE JPA (Infraestrutura)
 * Esta classe é específica da tecnologia de persistência (JPA).
 * Representa a tabela no banco de dados.
 * IMPORTANTE: Esta é DIFERENTE da entidade de domínio!
 * - VeiculoEntity: conhece JPA, anotações, banco de dados
 * - Veiculo (domínio): puro Java, sem dependências de frameworks
 * Um MAPPER faz a conversão entre elas
 */
@Entity
@Table(name = "veiculos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(length = 30)
    private String cor;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(nullable = false)
    private Boolean disponivel;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;
}
