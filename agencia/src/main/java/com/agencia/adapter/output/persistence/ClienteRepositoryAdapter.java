package com.agencia.adapter.output.persistence;

import com.agencia.adapter.output.persistence.database.ClienteSpringDataRepository;
import com.agencia.adapter.output.persistence.mapper.ClienteEntityMapper;
import com.agencia.domain.model.Cliente;
import com.agencia.ports.output.ClienteOutputPort;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ClienteRepositoryAdapter implements ClienteOutputPort {

    private final ClienteSpringDataRepository springDataRepository;
    private final ClienteEntityMapper mapper;

    public ClienteRepositoryAdapter(ClienteSpringDataRepository springDataRepository,
                                    ClienteEntityMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Cliente salvar(Cliente cliente) {
        return mapper.toDomain(
                springDataRepository.save(mapper.toEntity(cliente))
        );
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return springDataRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return springDataRepository.findByCpf(cpf)
                .map(mapper::toDomain);
    }
}
