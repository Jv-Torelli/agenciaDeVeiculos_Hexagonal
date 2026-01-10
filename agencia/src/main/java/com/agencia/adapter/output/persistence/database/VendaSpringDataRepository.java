package com.agencia.adapter.output.persistence.database;

import com.agencia.adapter.output.persistence.entity.VendaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendaSpringDataRepository extends JpaRepository<VendaEntity, Long> {
}
