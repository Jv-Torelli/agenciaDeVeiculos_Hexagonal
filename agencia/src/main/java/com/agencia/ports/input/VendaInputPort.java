package com.agencia.ports.input;

import com.agencia.domain.model.Venda;
import java.util.List;

/**
 * PORTA DE ENTRADA - Serviço de Venda
 */
public interface VendaInputPort {

    Venda realizar(Venda venda);

    List<Venda> listarTodas();

}
