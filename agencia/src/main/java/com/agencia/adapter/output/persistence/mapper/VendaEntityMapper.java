package com.agencia.adapter.output.persistence.mapper;

import com.agencia.adapter.output.persistence.entity.VendaEntity;
import com.agencia.domain.model.Venda;
import org.springframework.stereotype.Component;

/**
 * MAPPER - Converte entre VendaEntity (JPA) e Venda (Domain)
 */
@Component
public class VendaEntityMapper {

    private final VeiculoEntityMapper veiculoMapper;
    private final ClienteEntityMapper clienteMapper;

    public VendaEntityMapper(VeiculoEntityMapper veiculoMapper,
                             ClienteEntityMapper clienteMapper) {
        this.veiculoMapper = veiculoMapper;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Converte Entity (JPA) -> Domain
     */
    public Venda toDomain(VendaEntity entity) {
        if (entity == null) return null;

        return Venda.builder()
                .id(entity.getId())
                .veiculo(veiculoMapper.toDomain(entity.getVeiculo()))
                .cliente(clienteMapper.toDomain(entity.getCliente()))
                .valorVenda(entity.getValorVenda())
                .dataVenda(entity.getDataVenda())
                .build();
    }

    /**
     * Converte Domain -> Entity (JPA)
     */
    public VendaEntity toEntity(Venda domain) {
        if (domain == null) return null;

        return VendaEntity.builder()
                .id(domain.getId())
                .veiculo(veiculoMapper.toEntity(domain.getVeiculo()))
                .cliente(clienteMapper.toEntity(domain.getCliente()))
                .valorVenda(domain.getValorVenda())
                .dataVenda(domain.getDataVenda())
                .build();
    }
}
