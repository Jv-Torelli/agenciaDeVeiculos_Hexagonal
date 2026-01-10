package com.agencia.adapter.output.persistence;

import com.agencia.adapter.output.persistence.database.VendaSpringDataRepository;
import com.agencia.adapter.output.persistence.entity.VendaEntity;
import com.agencia.adapter.output.persistence.mapper.VendaEntityMapper;
import com.agencia.domain.model.Venda;
import com.agencia.ports.output.VendaOutputPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ADAPTADOR DE SAÍDA - Implementação da persistência de Vendas com JPA
 * Implementa VendaRepositoryPort usando Spring Data JPA
 */
@Component
public class VendaRepositoryAdapter implements VendaOutputPort {

    private final VendaSpringDataRepository springDataRepository;
    private final VendaEntityMapper mapper;

    public VendaRepositoryAdapter(VendaSpringDataRepository springDataRepository,
                                  VendaEntityMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Venda salvar(Venda venda) {
        // 1. Converter Domain -> Entity
        VendaEntity entity = mapper.toEntity(venda);

        // 2. Salvar no banco
        VendaEntity entitySalva = springDataRepository.save(entity);

        // 3. Converter Entity -> Domain e retornar
        return mapper.toDomain(entitySalva);
    }

    @Override
    public List<Venda> listarTodas() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
