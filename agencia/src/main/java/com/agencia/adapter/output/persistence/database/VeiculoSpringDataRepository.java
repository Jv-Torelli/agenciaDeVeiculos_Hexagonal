package com.agencia.adapter.output.persistence.database;

import com.agencia.adapter.output.persistence.entity.VeiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * REPOSITORY DO SPRING DATA JPA
 * Interface que estende JpaRepository do Spring Data.
 * O Spring cria automaticamente a implementação em tempo de execução.
 * Esta é uma interface técnica, específica do Spring Data.
 * NÃO é a mesma coisa que VeiculoRepositoryPort (porta de saída)
 */
@Repository
public interface VeiculoSpringDataRepository extends JpaRepository<VeiculoEntity, Long> {

    /**
     * Spring Data JPA gera automaticamente a query:
     * SELECT * FROM veiculos WHERE disponivel = true
     */
    List<VeiculoEntity> findByDisponivelTrue();

    /**
     * Spring Data JPA gera automaticamente:
     * SELECT EXISTS(SELECT 1 FROM veiculos WHERE placa = ?)
     */
    boolean existsByPlaca(String placa);
}
