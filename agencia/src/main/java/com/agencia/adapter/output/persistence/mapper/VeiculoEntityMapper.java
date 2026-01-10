package com.agencia.adapter.output.persistence.mapper;

import com.agencia.adapter.output.persistence.entity.VeiculoEntity;
import com.agencia.domain.model.Veiculo;
import org.springframework.stereotype.Component;

/**
 * MAPPER
 * Classe responsável por converter entre:
 * - Veiculo (domínio) <-> VeiculoEntity (JPA)
 * Isso mantém o domínio independente da tecnologia de persistência.
 * Se mudarmos de JPA para MongoDB, só precisamos mudar o mapper e o adapter.
 */
@Component
public class VeiculoEntityMapper {

    /**
     * Converte de Entity (JPA) para Domain (negócio)
     */
    public Veiculo toDomain(VeiculoEntity entity) {
        if (entity == null) return null;

        return Veiculo.builder()
                .id(entity.getId())
                .marca(entity.getMarca())
                .modelo(entity.getModelo())
                .ano(entity.getAno())
                .placa(entity.getPlaca())
                .cor(entity.getCor())
                .preco(entity.getPreco())
                .disponivel(entity.getDisponivel())
                .dataCadastro(entity.getDataCadastro())
                .build();
    }

    /**
     * Converte de Domain (negócio) para Entity (JPA)
     */
    public VeiculoEntity toEntity(Veiculo domain) {
        if (domain == null) return null;

        return VeiculoEntity.builder()
                .id(domain.getId())
                .marca(domain.getMarca())
                .modelo(domain.getModelo())
                .ano(domain.getAno())
                .placa(domain.getPlaca())
                .cor(domain.getCor())
                .preco(domain.getPreco())
                .disponivel(domain.getDisponivel())
                .dataCadastro(domain.getDataCadastro())
                .build();
    }
}
