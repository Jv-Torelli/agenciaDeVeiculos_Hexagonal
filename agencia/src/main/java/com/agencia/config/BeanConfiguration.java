package com.agencia.config;

import com.agencia.domain.service.VeiculoService;
import com.agencia.domain.service.VendaService;
import com.agencia.ports.output.VeiculoOutputPort;
import com.agencia.ports.output.VendaOutputPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * CONFIGURAÇÃO DE BEANS DO SPRING
 * Esta classe configura a injeção de dependências da aplicação.
 * PAPEL CRUCIAL NA ARQUITETURA HEXAGONAL:
 * - Define como as PORTAS são conectadas às IMPLEMENTAÇÕES
 * - Faz a "ligação" entre adaptadores e domínio
 * - Permite trocar implementações sem alterar o código
 * Exemplo: Se quisermos trocar JPA por MongoDB, só precisamos:
 * 1. Criar MongoVeiculoRepositoryAdapter
 * 2. Alterar este Bean para injetar a nova implementação
 * 3. O domínio e controllers continuam inalterados!
 */
@Configuration
public class BeanConfiguration {

    /**
     * Cria o bean do VeiculoService
     * O Spring vai:
     * 1. Criar uma instância de VeiculoService
     * 2. Injetar a implementação de VeiculoRepositoryPort (o Adapter JPA)
     * 3. Disponibilizar como VeiculoServicePort para os controllers
     * Isso permite que o controller dependa apenas da interface (Port),
     * não da implementação concreta (Service)
     */
    @Bean
    public VeiculoService vendaOutputPort(
            VeiculoOutputPort veiculoOutputPort) {
        return new VeiculoService(veiculoOutputPort);
    }

    /**
     * Cria o bean do VendaService
     */
    @Bean
    public VendaService vendaServicePort(
            VendaOutputPort vendaOutputPort,
            VeiculoOutputPort veiculoOutputPort) {
        return new VendaService(vendaOutputPort, veiculoOutputPort);
    }
}
