package com.agencia.adapter.input.controller;

import com.agencia.adapter.input.dto.mapper.VeiculoDTOMapper;
import com.agencia.adapter.input.dto.request.VeiculoRequestDTO;
import com.agencia.adapter.input.dto.response.VeiculoResponseDTO;
import com.agencia.domain.model.Veiculo;
import com.agencia.ports.input.VeiculoInputPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ADAPTADOR DE ENTRADA - REST Controller
 * Este é um ADAPTADOR que recebe requisições HTTP e traduz para chamadas
 * aos serviços de domínio.
 * Responsabilidades:
 * 1. Receber requisições HTTP (GET, POST, PUT, DELETE)
 * 2. Validar dados de entrada (@Valid)
 * 3. Converter DTOs -> Domain usando mapper
 * 4. Chamar o SERVICE através da PORTA (VeiculoInputPort)
 * 5. Converter Domain -> DTOs de resposta
 * 6. Retornar ResponseEntity com status HTTP apropriado
 * IMPORTANTE: O controller NÃO conhece a implementação do service.
 * Ele usa apenas a INTERFACE (VeiculoInputPort).
 * O Spring injeta a implementação automaticamente.
 */
@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {

    /**
     * Dependência da PORTA de entrada (interface)
     * O controller não conhece VeiculoService, apenas VeiculoInputPort
     */
    private final VeiculoInputPort veiculoInputPort;
    private final VeiculoDTOMapper mapper;

    /**
     * Injeção de dependência via construtor
     */
    public VeiculoController(VeiculoInputPort veiculoInputPort,
                             VeiculoDTOMapper mapper) {
        this.veiculoInputPort = veiculoInputPort;
        this.mapper = mapper;
    }

    /**
     * POST /api/veiculos - Cadastrar novo veículo
     *
     * @Valid ativa as validações do Bean Validation no DTO
     */
    @PostMapping
    public ResponseEntity<VeiculoResponseDTO> cadastrar(
            @Valid @RequestBody VeiculoRequestDTO requestDTO) {

        // 1. Converter DTO -> Domain
        Veiculo veiculo = mapper.toDomain(requestDTO);

        // 2. Chamar o serviço através da porta
        Veiculo veiculoCadastrado = veiculoInputPort.cadastrar(veiculo);

        // 3. Converter Domain -> DTO Response
        VeiculoResponseDTO responseDTO = mapper.toResponseDTO(veiculoCadastrado);

        // 4. Retornar resposta HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * GET /api/veiculos/{id} - Buscar veículo por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> buscarPorId(@PathVariable Long id) {
        Veiculo veiculo = veiculoInputPort.buscarPorId(id);
        VeiculoResponseDTO responseDTO = mapper.toResponseDTO(veiculo);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * GET /api/veiculos - Listar todos os veículos
     */
    @GetMapping
    public ResponseEntity<List<VeiculoResponseDTO>> listarTodos() {
        List<VeiculoResponseDTO> veiculos = veiculoInputPort.listarTodos()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(veiculos);
    }

    /**
     * GET /api/veiculos/disponiveis - Listar veículos disponíveis
     */
    @GetMapping("/disponiveis")
    public ResponseEntity<List<VeiculoResponseDTO>> listarDisponiveis() {
        List<VeiculoResponseDTO> veiculos = veiculoInputPort.listarDisponiveis()
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(veiculos);
    }

    /**
     * PUT /api/veiculos/{id} - Atualizar veículo
     */
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VeiculoRequestDTO requestDTO) {

        Veiculo veiculo = mapper.toDomain(requestDTO);
        Veiculo veiculoAtualizado = veiculoInputPort.atualizar(id, veiculo);
        VeiculoResponseDTO responseDTO = mapper.toResponseDTO(veiculoAtualizado);

        return ResponseEntity.ok(responseDTO);
    }

    /**
     * DELETE /api/veiculos/{id} - Deletar veículo
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        veiculoInputPort.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
