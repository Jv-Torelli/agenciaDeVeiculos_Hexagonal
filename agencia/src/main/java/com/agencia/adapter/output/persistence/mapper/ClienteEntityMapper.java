package com.agencia.adapter.output.persistence.mapper;

import com.agencia.adapter.output.persistence.entity.ClienteEntity;
import com.agencia.domain.model.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteEntityMapper {

    public Cliente toDomain(ClienteEntity entity) {
        if (entity == null) return null;
        return Cliente.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpf(entity.getCpf())
                .telefone(entity.getTelefone())
                .email(entity.getEmail())
                .build();
    }

    public ClienteEntity toEntity(Cliente domain) {
        if (domain == null) return null;
        return ClienteEntity.builder()
                .id(domain.getId())
                .nome(domain.getNome())
                .cpf(domain.getCpf())
                .telefone(domain.getTelefone())
                .email(domain.getEmail())
                .build();
    }
}
