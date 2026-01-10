package com.agencia.adapter.input.dto.mapper;


import com.agencia.adapter.input.dto.response.VendaResponseDTO;
import com.agencia.domain.model.Venda;
import org.springframework.stereotype.Component;

/**
 * MAPPER DE DTOs - Venda
 * Converte entre objetos de domínio e DTOs da API REST
 */
@Component
public class VendaDTOMapper {

    private final VeiculoDTOMapper veiculoMapper;
    private final ClienteDTOMapper clienteMapper;

    /**
     * Injeta os outros mappers necessários
     */
    public VendaDTOMapper(VeiculoDTOMapper veiculoMapper,
                          ClienteDTOMapper clienteMapper) {
        this.veiculoMapper = veiculoMapper;
        this.clienteMapper = clienteMapper;
    }

    /**
     * Converte Domain -> Response DTO
     * Inclui os dados completos do veículo e cliente
     */
    public VendaResponseDTO toResponseDTO(Venda venda) {
        return VendaResponseDTO.builder()
                .id(venda.getId())
                .veiculo(veiculoMapper.toResponseDTO(venda.getVeiculo()))
                .cliente(clienteMapper.toResponseDTO(venda.getCliente()))
                .valorVenda(venda.getValorVenda())
                .dataVenda(venda.getDataVenda())
                .build();
    }
}
