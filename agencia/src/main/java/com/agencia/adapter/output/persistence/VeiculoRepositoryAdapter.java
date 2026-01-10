package com.agencia.adapter.output.persistence;

import com.agencia.adapter.output.persistence.database.VeiculoSpringDataRepository;
import com.agencia.adapter.output.persistence.entity.VeiculoEntity;
import com.agencia.adapter.output.persistence.mapper.VeiculoEntityMapper;
import com.agencia.domain.model.Veiculo;
import com.agencia.ports.output.VeiculoOutputPort;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * ADAPTADOR DE SAÍDA - Persistência com JPA
 * Esta classe IMPLEMENTA a porta de saída (VeiculoRepositoryPort).
 * É a implementação CONCRETA da interface definida pelo domínio.
 * Responsabilidades:
 * 1. Implementar VeiculoRepositoryPort
 * 2. Usar o Spring Data Repository (VeiculoSpringDataRepository)
 * 3. Converter entre Domain e Entity usando o Mapper
 * O domínio NÃO conhece esta classe, apenas a interface (Port).
 * O Spring injeta esta implementação automaticamente.
 */
@Component
public class VeiculoRepositoryAdapter implements VeiculoOutputPort {

    private final VeiculoSpringDataRepository springDataRepository;
    private final VeiculoEntityMapper mapper;

    /**
     * Injeção das dependências de infraestrutura
     */
    public VeiculoRepositoryAdapter(VeiculoSpringDataRepository springDataRepository,
                                    VeiculoEntityMapper mapper) {
        this.springDataRepository = springDataRepository;
        this.mapper = mapper;
    }

    @Override
    public Veiculo salvar(Veiculo veiculo) {
        // 1. Converter Domain -> Entity
        VeiculoEntity entity = mapper.toEntity(veiculo);

        // 2. Salvar no banco (JPA)
        VeiculoEntity entitySalva = springDataRepository.save(entity);

        // 3. Converter Entity -> Domain e retornar
        return mapper.toDomain(entitySalva);
    }

    @Override
    public Optional<Veiculo> buscarPorId(Long id) {
        return springDataRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Veiculo> listarTodos() {
        return springDataRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Veiculo> listarDisponiveis() {
        return springDataRepository.findByDisponivelTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public boolean existePorPlaca(String placa) {
        return springDataRepository.existsByPlaca(placa);
    }
}
