package com.agencia.adapter.input.controller;

import com.agencia.adapter.input.dto.mapper.VendaDTOMapper;
import com.agencia.adapter.input.dto.request.VendaRequestDTO;
import com.agencia.adapter.input.dto.response.VendaResponseDTO;
import com.agencia.domain.model.Cliente;
import com.agencia.domain.model.Veiculo;
import com.agencia.domain.model.Venda;
import com.agencia.ports.input.VeiculoInputPort;
import com.agencia.ports.input.VendaInputPort;
import com.agencia.ports.output.ClienteOutputPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ADAPTADOR DE ENTRADA - REST Controller para Vendas
 * Responsabilidades:
 * 1. Receber requisições HTTP para operações de venda
 * 2. Validar dados de entrada
 * 3. Buscar veículo e cliente necessários
 * 4. Montar objeto de Venda do domínio
 * 5. Chamar o service através da porta
 * 6. Converter resultado para DTO e retornar
 */
@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    /**
     * Dependências das PORTAS (interfaces)
     */
    private final VendaInputPort vendaInputPort;
    private final VeiculoInputPort veiculoInputPort;
    private final ClienteOutputPort clienteOutputPort;
    private final VendaDTOMapper mapper;

    /**
     * Injeção de dependências via construtor
     */
    public VendaController(VendaInputPort vendaInputPort,
                           VeiculoInputPort veiculoInputPort,
                           ClienteOutputPort clienteOutputPort,
                           VendaDTOMapper mapper) {
        this.vendaInputPort = vendaInputPort;
        this.veiculoInputPort = veiculoInputPort;
        this.clienteOutputPort = clienteOutputPort;
        this.mapper = mapper;
    }

    /**
     * POST /api/vendas - Realizar uma venda
     * Processo:
     * 1. Recebe VendaRequestDTO com IDs do veículo e cliente
     * 2. Busca o veículo pelo ID
     * 3. Busca o cliente pelo ID
     * 4. Monta objeto Venda do domínio
     * 5. Chama o service para realizar a venda
     * 6. Retorna VendaResponseDTO com todos os dados
     *
     * @param requestDTO DTO com dados da venda
     * @return VendaResponseDTO com dados completos da venda realizada
     */
    @PostMapping
    public ResponseEntity<VendaResponseDTO> realizarVenda(
            @Valid @RequestBody VendaRequestDTO requestDTO) {

        // 1. Buscar veículo
        Veiculo veiculo = veiculoInputPort.buscarPorId(requestDTO.getVeiculoId());

        // 2. Buscar cliente
        Cliente cliente = clienteOutputPort.buscarPorId(requestDTO.getClienteId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente não encontrado com ID: " + requestDTO.getClienteId()
                ));

        // 3. Montar objeto de Venda do domínio
        Venda venda = Venda.builder()
                .veiculo(veiculo)
                .cliente(cliente)
                .valorVenda(requestDTO.getValorVenda())
                .build();

        // 4. Realizar a venda através do service
        Venda vendaRealizada = vendaInputPort.realizar(venda);

        // 5. Converter para DTO e retornar
        VendaResponseDTO responseDTO = mapper.toResponseDTO(vendaRealizada);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * GET /api/vendas - Listar todas as vendas
     *
     * @return Lista de todas as vendas realizadas
     */
    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarTodas() {
        List<VendaResponseDTO> vendas = vendaInputPort.listarTodas()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vendas);
    }

    /**
     * GET /api/vendas/veiculo/{veiculoId} - Buscar venda de um veículo específico
     * Endpoint útil para verificar se um veículo foi vendido
     * @param veiculoId ID do veículo
     * @return Dados da venda se encontrada
     */
    @GetMapping("/veiculo/{veiculoId}")
    public ResponseEntity<VendaResponseDTO> buscarPorVeiculo(
            @PathVariable Long veiculoId) {

        List<Venda> vendas = vendaInputPort.listarTodas();

        Venda venda = vendas.stream()
                .filter(v -> v.getVeiculo().getId().equals(veiculoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Nenhuma venda encontrada para o veículo ID: " + veiculoId
                ));

        return ResponseEntity.ok(mapper.toResponseDTO(venda));
    }
}
