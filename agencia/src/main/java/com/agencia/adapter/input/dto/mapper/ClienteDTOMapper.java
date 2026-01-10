package com.agencia.adapter.input.dto.mapper;

import com.agencia.adapter.input.dto.request.ClienteRequestDTO;
import com.agencia.adapter.input.dto.response.ClienteResponseDTO;
import com.agencia.domain.model.Cliente;
import org.springframework.stereotype.Component;

/**
 * MAPPER DE DTOs - Cliente
 */
@Component
public class ClienteDTOMapper {

    /**
     * Converte Request DTO -> Domain
     */
    public Cliente toDomain(ClienteRequestDTO dto) {
        return Cliente.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .telefone(dto.getTelefone())
                .email(dto.getEmail())
                .build();
    }

    /**
     * Converte Domain -> Response DTO
     */
    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpf(cliente.getCpf())
                .telefone(cliente.getTelefone())
                .email(cliente.getEmail())
                .build();
    }
}
