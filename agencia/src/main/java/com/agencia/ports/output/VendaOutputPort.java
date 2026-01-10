package com.agencia.ports.output;

import com.agencia.domain.model.Venda;
import java.util.List;

/**
 * PORTA DE SAÍDA - Repositório de Venda
 */
public interface VendaOutputPort {

    Venda salvar(Venda venda);

    List<Venda> listarTodas();
}
