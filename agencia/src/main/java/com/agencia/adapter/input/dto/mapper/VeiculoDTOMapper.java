package com.agencia.adapter.input.dto.mapper;

import com.agencia.adapter.input.dto.request.VeiculoRequestDTO;
import com.agencia.adapter.input.dto.response.VeiculoResponseDTO;
import com.agencia.domain.model.Veiculo;
import org.springframework.stereotype.Component;

/**
 * MAPPER DE DTOs
 * Converte entre DTOs (API REST) e objetos de Domínio.
 * Mantém a separação entre a camada de apresentação e o domínio.
 */
@Component
public class VeiculoDTOMapper {

    /**
     * Converte Request DTO -> Domain
     */
    public Veiculo toDomain(VeiculoRequestDTO dto) {
        return Veiculo.builder()
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .ano(dto.getAno())
                .placa(dto.getPlaca())
                .cor(dto.getCor())
                .preco(dto.getPreco())
                .build();
    }

    /**
     * Converte Domain -> Response DTO
     */
    public VeiculoResponseDTO toResponseDTO(Veiculo veiculo) {
        return VeiculoResponseDTO.builder()
                .id(veiculo.getId())
                .marca(veiculo.getMarca())
                .modelo(veiculo.getModelo())
                .ano(veiculo.getAno())
                .placa(veiculo.getPlaca())
                .cor(veiculo.getCor())
                .preco(veiculo.getPreco())
                .disponivel(veiculo.getDisponivel())
                .dataCadastro(veiculo.getDataCadastro())
                .build();
    }
}
