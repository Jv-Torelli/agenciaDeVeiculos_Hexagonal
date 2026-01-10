package com.agencia.adapter.input.controller;

import com.agencia.adapter.input.dto.mapper.ClienteDTOMapper;
import com.agencia.adapter.input.dto.request.ClienteRequestDTO;
import com.agencia.adapter.input.dto.response.ClienteResponseDTO;
import com.agencia.domain.model.Cliente;
import com.agencia.ports.output.ClienteOutputPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ADAPTADOR DE ENTRADA - REST Controller para Clientes
 * Controller simples para gerenciar clientes
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteOutputPort clienteOutputPort;
    private final ClienteDTOMapper mapper;

    public ClienteController(ClienteOutputPort clienteOutputPort,
                             ClienteDTOMapper mapper) {
        this.clienteOutputPort = clienteOutputPort;
        this.mapper = mapper;
    }

    /**
     * POST /api/clientes - Cadastrar novo cliente
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrar(
            @Valid @RequestBody ClienteRequestDTO requestDTO) {

        // Converter DTO -> Domain
        Cliente cliente = mapper.toDomain(requestDTO);

        // Validar regras de negócio
        cliente.validar();

        // Verificar se CPF já existe
        if (clienteOutputPort.buscarPorCpf(cliente.getCpf()).isPresent()) {
            throw new IllegalArgumentException(
                    "Já existe cliente cadastrado com este CPF"
            );
        }

        // Salvar
        Cliente clienteSalvo = clienteOutputPort.salvar(cliente);

        // Converter para DTO e retornar
        ClienteResponseDTO responseDTO = mapper.toResponseDTO(clienteSalvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /**
     * GET /api/clientes/{id} - Buscar cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable Long id) {
        Cliente cliente = clienteOutputPort.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente não encontrado com ID: " + id
                ));

        ClienteResponseDTO responseDTO = mapper.toResponseDTO(cliente);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * GET /api/clientes/cpf/{cpf} - Buscar cliente por CPF
     */
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<ClienteResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        Cliente cliente = clienteOutputPort.buscarPorCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cliente não encontrado com CPF: " + cpf
                ));

        ClienteResponseDTO responseDTO = mapper.toResponseDTO(cliente);
        return ResponseEntity.ok(responseDTO);
    }
}
