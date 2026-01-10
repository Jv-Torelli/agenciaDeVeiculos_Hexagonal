<div align="center">

# 🚗 Agência de Veículos - Arquitetura Hexagonal

[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![H2](https://img.shields.io/badge/H2-59666C?style=for-the-badge&logo=h2&logoColor=white)](https://www.h2database.com/)

</div>

---

## 📚 O que é Arquitetura Hexagonal?

A **Arquitetura Hexagonal** (também chamada de **Ports and Adapters**) foi criada por **Alistair Cockburn**. O objetivo é criar aplicações onde:

1. O domínio (negócio) é o centro e não depende de nada externo
2. Portas definem interfaces (contratos) de comunicação
3. Adaptadores implementam essas interfaces usando tecnologias específicas

---

## 🎯 Princípios Fundamentais

```
┌─────────────────────────┐
│    ADAPTADORES DE       │
│       ENTRADA           │
│  (REST, CLI, WebSocket) │
└───────────┬─────────────┘
            │
    ┌───────▼────────┐
    │  PORTAS DE     │
    │    ENTRADA     │
    │  (Interfaces)  │
    └───────┬────────┘
            │
┌───────────▼──────────────┐
│                          │
│    DOMÍNIO (NÚCLEO)      │
│   - Entidades            │
│   - Regras de Negócio    │
│   - Services             │
│                          │
└───────────┬──────────────┘
            │
    ┌───────▼────────┐
    │  PORTAS DE     │
    │     SAÍDA      │
    │  (Interfaces)  │
    └───────┬────────┘
            │
┌───────────▼─────────────┐
│   ADAPTADORES DE        │
│       SAÍDA             │
│ (JPA, MongoDB, APIs)    │
└─────────────────────────┘
```

---

## 🏗️ Estrutura do Projeto

### 1️⃣ DOMAIN (Núcleo - O Hexágono)

**Localização:** `com.agencia.domain`

**O que contém:**
- **model/**: Entidades de negócio (Veiculo, Cliente, Venda)
- **service/**: Lógica de negócio (VeiculoService, VendaService)
- **exception/**: Exceções de domínio

**Características:**
- ✅ Independente de frameworks
- ✅ Sem anotações JPA, Spring, etc
- ✅ Contém APENAS regras de negócio
- ✅ Não conhece banco de dados, REST, ou qualquer tecnologia externa

**Exemplo:**

```java
// Veiculo.java - Entidade pura de domínio
public class Veiculo {
    private Long id;
    private String marca;
    
    // Regra de negócio no domínio
    public void marcarComoVendido() {
        if (!this.disponivel) {
            throw new IllegalStateException("Veículo já vendido");
        }
        this.disponivel = false;
    }
}
```

---

### 2️⃣ PORTS (Portas - Interfaces)

**Localização:** `com.agencia.ports`

**O que contém:**
- **input/**: Portas de entrada (o que a aplicação FAZ)
  - VeiculoInputPort.java
  - VendaInputPort.java
  
- **output/**: Portas de saída (o que a aplicação PRECISA)
  - VeiculoOutputPort.java
  - ClienteOutputPort.java

**Características:**
- ✅ São apenas INTERFACES (contratos)
- ✅ Definem o "O QUE" fazer, não o "COMO"
- ✅ Separam o domínio das implementações

**Exemplo:**

```java
// VeiculoOutputPort.java - Porta de Saída
public interface VeiculoOutputPort {
    Veiculo salvar(Veiculo veiculo);
    Optional<Veiculo> buscarPorId(Long id);
    List<Veiculo> listarTodos();
}

// VeiculoInputPort.java - Porta de Entrada
public interface VeiculoInputPort {
    Veiculo cadastrar(Veiculo veiculo);
    Veiculo buscarPorId(Long id);
}
```

---

### 3️⃣ ADAPTERS (Adaptadores - Implementações)

**Localização:** `com.agencia.veiculos.adapters`

#### 3.1 Adapter de ENTRADA (input/)

**Localização:** `adapters/input/rest`

**O que contém:**
- **Controllers REST**: Recebem requisições HTTP
- **DTOs**: Objetos de transferência de dados
- **Mappers**: Convertem DTO ↔ Domain

**Responsabilidades:**
- ✅ Receber requisições HTTP
- ✅ Validar dados de entrada
- ✅ Converter DTOs para objetos de domínio
- ✅ Chamar serviços através das PORTAS
- ✅ Retornar respostas HTTP

**Exemplo:**

```java
@RestController
@RequestMapping("/api/veiculos")
public class VeiculoController {
    // Depende da PORTA, não da implementação
    private final VeiculoInputPort veiculoInputPort;
    
    @PostMapping
    public ResponseEntity<VeiculoResponseDTO> cadastrar(
            @Valid @RequestBody VeiculoRequestDTO dto) {
        
        Veiculo veiculo = mapper.toDomain(dto);
        Veiculo salvo = veiculoInputPort.cadastrar(veiculo);
        return ResponseEntity.ok(mapper.toResponseDTO(salvo));
    }
}
```

#### 3.2 Adaptadores de SAÍDA (output/)

**Localização:** `adapter/output/persistence`

**O que contém:**
- **Entities JPA**: Entidades do banco de dados
- **Repositories Spring Data**: Interfaces JPA
- **Adapters**: Implementam as portas de saída
- **Mappers**: Convertem Domain ↔ Entity

**Responsabilidades:**
- ✅ Implementar as portas de saída
- ✅ Fazer persistência no banco
- ✅ Converter objetos de domínio para entidades JPA
- ✅ Isolar o domínio da tecnologia de banco

**Exemplo:**

```java
@Component
public class VeiculoRepositoryAdapter implements VeiculoOutputPort {
    private final VeiculoSpringDataRepository springDataRepo;
    private final VeiculoEntityMapper mapper;
    
    @Override
    public Veiculo salvar(Veiculo veiculo) {
        VeiculoEntity entity = mapper.toEntity(veiculo);
        VeiculoEntity saved = springDataRepo.save(entity);
        return mapper.toDomain(saved);
    }
}
```

---

### 4️⃣ CONFIGURATION

**Localização:** `com.agencia.configuration`

**O que contém:**
- **BeanConfiguration.java**: Configuração de injeção de dependências

**Responsabilidade:**
- ✅ Conectar portas às implementações
- ✅ Configurar beans do Spring

```java
@Configuration
public class BeanConfiguration {
    @Bean
    public VeiculoInputPort veiculoInputPort(
            VeiculoOutputPort veiculoOutputPort) {
        return new VeiculoInputPort(veiculoOutputPort);
    }
}
```

---

## 🔄 Fluxo de uma Requisição

Vamos seguir o fluxo de **POST /api/veiculos** (cadastrar veículo):

```
1. HTTP Request chega no VeiculoController (Adapter de Entrada)
                   ↓
2. Controller valida o VeiculoRequestDTO (@Valid)
                   ↓
3. Controller converte DTO → Veiculo (domínio) usando mapper
                   ↓
4. Controller chama veiculoOutputPort.cadastrar() através da PORTA
                   ↓
5. VeiculoService (domínio) executa regras de negócio
   - Valida dados
   - Verifica se placa já existe
   - Define veículo como disponível
                   ↓
6. VeiculoService chama repositório através da PORTA
                   ↓
7. VeiculoRepositoryAdapter (Adapter de Saída) recebe a chamada
                   ↓
8. Adapter converte Veiculo → VeiculoEntity usando mapper
                   ↓
9. Adapter salva no banco via Spring Data JPA
                   ↓
10. Adapter converte VeiculoEntity → Veiculo e retorna
                   ↓
11. VeiculoService retorna para o Controller
                   ↓
12. Controller converte Veiculo → VeiculoResponseDTO
                   ↓
13. HTTP Response é enviado ao cliente
```

---

## ✨ Vantagens da Arquitetura Hexagonal

### 1. Independência de Tecnologia

```java
// O domínio não conhece JPA
public class Veiculo {
    private Long id;  // SEM @Id
    private String marca;  // SEM @Column
}

// Apenas o adapter conhece JPA
@Entity
public class VeiculoEntity {
    @Id
    private Long id;
}
```

### 2. Facilidade para Testes

```java
// Testar o domínio sem banco de dados
class VeiculoServiceTest {
    @Test
    void deveCadastrarVeiculo() {
        // Mock da porta
        VeiculoRepositoryPort mockRepo = mock(VeiculoRepositoryPort.class);
        VeiculoService service = new VeiculoService(mockRepo);
        
        // Testar apenas lógica de negócio
        Veiculo veiculo = new Veiculo(...);
        service.cadastrar(veiculo);
    }
}
```

### 3. Fácil Troca de Implementações

```java
// Usar JPA hoje
@Bean
public VeiculoOutputPort veiculoOutputPort() {
    return new VeiculoJpaAdapter(...);
}

// Trocar para MongoDB amanhã - apenas mudar o Bean!
@Bean
public VeiculoOutputPort veiculoOutputPort() {
    return new VeiculoMongoAdapter(...);
}

// O DOMÍNIO e CONTROLLERS permanecem IGUAIS!
```

### 4. Regras de Negócio Centralizadas

```java
// Todas as regras ficam no domínio
public class Veiculo {
    public void marcarComoVendido() {
        if (!this.disponivel) {
            throw new IllegalStateException("Já vendido");
        }
        this.disponivel = false;
    }
}

// Controllers apenas chamam o domínio
veiculoService.realizarVenda(venda);
```

---

## 🚀 Como Executar

### 1. Criar projeto no Spring Initializr

Acesse: https://start.spring.io/

**Dependências:**
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Validation

### 2. Estrutura de pastas

Crie a estrutura conforme mostrado no início deste documento.

### 3. Executar a aplicação

```bash
mvn spring-boot:run
```

### 4. Testar os endpoints

**Cadastrar veículo:**

```bash
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Corolla",
    "ano": 2023,
    "placa": "ABC-1234",
    "cor": "Prata",
    "preco": 85000.00
  }'
```

**Listar veículos:**

```bash
curl http://localhost:8080/api/veiculos
```

**Buscar por ID:**

```bash
curl http://localhost:8080/api/veiculos/1
```

**Listar disponíveis:**

```bash
curl http://localhost:8080/api/veiculos/disponiveis
```

### 5. Acessar console H2

**URL:** http://localhost:8080/h2-console

- **JDBC URL:** `jdbc:h2:mem:agencia_veiculos`
- **Username:** `sa`
- **Password:** (deixe em branco)

---

## 📖 Conceitos Importantes

### Ports vs Adapters

| Conceito | Descrição | Localização |
|----------|-----------|-------------|
| **Port (Porta)** | Interface que define um contrato | `ports/` |
| **Adapter (Adaptador)** | Implementação concreta de uma porta | `adapters/` |

### Driving vs Driven

| Tipo | Também chamado | Descrição | Exemplo |
|------|----------------|-----------|---------|
| **Driving Port** | Input Port / Porta de Entrada | Quem DIRIGE a aplicação | REST Controller |
| **Driven Port** | Output Port / Porta de Saída | Quem é DIRIGIDO pela aplicação | Database Repository |

### Domain vs Infrastructure

| Camada | Contém | Depende de |
|--------|--------|------------|
| **Domain** | Regras de negócio puras | Nada! |
| **Infrastructure** | Tecnologias (JPA, REST, etc) | Domain (através de portas) |

---

## 🎓 Aprendizados Principais

1. **Domínio no Centro**: O negócio não depende de tecnologia
2. **Inversão de Dependência**: Domínio define interfaces, infra implementa
3. **Separação de Responsabilidades**: Cada camada tem um papel claro
4. **Testabilidade**: Fácil testar o domínio isoladamente
5. **Flexibilidade**: Trocar tecnologias sem impactar o negócio

---

## 📡 Exemplos de Uso da API - Agência de Veículos

### 🚗 ENDPOINTS DE VEÍCULOS

#### 1. Cadastrar Veículo

```bash
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Corolla",
    "ano": 2023,
    "placa": "ABC-1234",
    "cor": "Prata",
    "preco": 85000.00
  }'
```

**Resposta esperada (201 Created):**

```json
{
  "id": 1,
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2023,
  "placa": "ABC-1234",
  "cor": "Prata",
  "preco": 85000.00,
  "disponivel": true,
  "dataCadastro": "2024-01-09T10:30:00"
}
```

#### 2. Listar Todos os Veículos

```bash
curl http://localhost:8080/api/veiculos
```

#### 3. Buscar Veículo por ID

```bash
curl http://localhost:8080/api/veiculos/1
```

#### 4. Listar Apenas Veículos Disponíveis

```bash
curl http://localhost:8080/api/veiculos/disponiveis
```

#### 5. Atualizar Veículo

```bash
curl -X PUT http://localhost:8080/api/veiculos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Toyota",
    "modelo": "Corolla XEI",
    "ano": 2023,
    "placa": "ABC-1234",
    "cor": "Prata Metálico",
    "preco": 88000.00
  }'
```

#### 6. Deletar Veículo

```bash
curl -X DELETE http://localhost:8080/api/veiculos/1
```

---

### 👤 ENDPOINTS DE CLIENTES

#### 1. Cadastrar Cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João da Silva",
    "cpf": "12345678901",
    "telefone": "11987654321",
    "email": "joao@email.com"
  }'
```

**Resposta esperada (201 Created):**

```json
{
  "id": 1,
  "nome": "João da Silva",
  "cpf": "12345678901",
  "telefone": "11987654321",
  "email": "joao@email.com"
}
```

#### 2. Buscar Cliente por ID

```bash
curl http://localhost:8080/api/clientes/1
```

#### 3. Buscar Cliente por CPF

```bash
curl http://localhost:8080/api/clientes/cpf/12345678901
```

---

### 💰 ENDPOINTS DE VENDAS

#### 1. Realizar Venda

```bash
curl -X POST http://localhost:8080/api/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "veiculoId": 1,
    "clienteId": 1,
    "valorVenda": 85000.00
  }'
```

**Resposta esperada (201 Created):**

```json
{
  "id": 1,
  "veiculo": {
    "id": 1,
    "marca": "Toyota",
    "modelo": "Corolla",
    "ano": 2023,
    "placa": "ABC-1234",
    "cor": "Prata",
    "preco": 85000.00,
    "disponivel": false,
    "dataCadastro": "2024-01-09T10:30:00"
  },
  "cliente": {
    "id": 1,
    "nome": "João da Silva",
    "cpf": "12345678901",
    "telefone": "11987654321",
    "email": "joao@email.com"
  },
  "valorVenda": 85000.00,
  "dataVenda": "2024-01-09T14:45:00"
}
```

#### 2. Listar Todas as Vendas

```bash
curl http://localhost:8080/api/vendas
```

#### 3. Buscar Venda por Veículo

```bash
curl http://localhost:8080/api/vendas/veiculo/1
```

---

## 🔄 FLUXO COMPLETO - Passo a Passo

### Passo 1: Cadastrar alguns veículos

```bash
# Veículo 1
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Honda",
    "modelo": "Civic",
    "ano": 2022,
    "placa": "XYZ-9876",
    "cor": "Preto",
    "preco": 95000.00
  }'

# Veículo 2
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Volkswagen",
    "modelo": "Gol",
    "ano": 2023,
    "placa": "DEF-5678",
    "cor": "Branco",
    "preco": 55000.00
  }'

# Veículo 3
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Fiat",
    "modelo": "Uno",
    "ano": 2021,
    "placa": "GHI-1234",
    "cor": "Vermelho",
    "preco": 45000.00
  }'
```

### Passo 2: Cadastrar clientes

```bash
# Cliente 1
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "cpf": "98765432100",
    "telefone": "11999887766",
    "email": "maria@email.com"
  }'

# Cliente 2
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Pedro Oliveira",
    "cpf": "11122233344",
    "telefone": "11988776655",
    "email": "pedro@email.com"
  }'
```

### Passo 3: Listar veículos disponíveis

```bash
curl http://localhost:8080/api/veiculos/disponiveis
```

### Passo 4: Realizar uma venda

```bash
curl -X POST http://localhost:8080/api/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "veiculoId": 1,
    "clienteId": 1,
    "valorVenda": 95000.00
  }'
```

### Passo 5: Verificar que o veículo não está mais disponível

```bash
curl http://localhost:8080/api/veiculos/disponiveis
```

### Passo 6: Tentar vender o mesmo veículo novamente (deve dar erro)

```bash
curl -X POST http://localhost:8080/api/vendas \
  -H "Content-Type: application/json" \
  -d '{
    "veiculoId": 1,
    "clienteId": 2,
    "valorVenda": 95000.00
  }'
```

**Resposta esperada (409 Conflict):**

```json
{
  "timestamp": "2024-01-09T15:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Veículo indisponível para venda: 1"
}
```

### Passo 7: Listar todas as vendas

```bash
curl http://localhost:8080/api/vendas
```

---

## ❌ EXEMPLOS DE VALIDAÇÃO (Erros esperados)

### 1. Cadastrar veículo com dados inválidos

```bash
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "",
    "modelo": "Teste",
    "ano": 1900,
    "placa": "INVALIDO",
    "preco": -1000
  }'
```

**Resposta esperada (400 Bad Request):**

```json
{
  "marca": "Marca é obrigatória",
  "ano": "Ano deve ser maior ou igual a 1950",
  "placa": "Placa inválida",
  "preco": "Preço deve ser maior que zero"
}
```

### 2. Cadastrar cliente com CPF inválido

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Teste",
    "cpf": "123",
    "telefone": "abc",
    "email": "email-invalido"
  }'
```

**Resposta esperada (400 Bad Request):**

```json
{
  "cpf": "CPF deve conter 11 dígitos",
  "telefone": "Telefone inválido",
  "email": "Email inválido"
}
```

### 3. Buscar veículo que não existe

```bash
curl http://localhost:8080/api/veiculos/99999
```

**Resposta esperada (404 Not Found):**

```json
{
  "timestamp": "2024-01-09T15:10:00",
  "status": 404,
  "error": "Not Found",
  "message": "Veículo não encontrado com ID: 99999"
}
```

### 4. Cadastrar veículo com placa duplicada

```bash
# Primeiro veículo
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Ford",
    "modelo": "Ka",
    "ano": 2023,
    "placa": "JKL-9999",
    "cor": "Azul",
    "preco": 50000.00
  }'

# Tentar cadastrar outro com a mesma placa
curl -X POST http://localhost:8080/api/veiculos \
  -H "Content-Type: application/json" \
  -d '{
    "marca": "Chevrolet",
    "modelo": "Onix",
    "ano": 2023,
    "placa": "JKL-9999",
    "cor": "Prata",
    "preco": 60000.00
  }'
```

**Resposta esperada (400 Bad Request):**

```json
{
  "timestamp": "2024-01-09T15:15:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Já existe veículo com esta placa"
}
```

---

## 🧪 TESTANDO COM BRUNO/POSTMAN/INSOMNIA

### Importar Collection

Crie uma Collection no BRUNO/POSTMAN/INSOMNIA com estas requests:

#### 1. Cadastrar Veículo
- **Method:** POST
- **URL:** http://localhost:8080/api/veiculos
- **Headers:** Content-Type: application/json
- **Body (raw JSON):**

```json
{
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2023,
  "placa": "ABC-1234",
  "cor": "Prata",
  "preco": 85000.00
}
```

#### 2. Listar Veículos
- **Method:** GET
- **URL:** http://localhost:8080/api/veiculos

#### 3. Cadastrar Cliente
- **Method:** POST
- **URL:** http://localhost:8080/api/clientes
- **Headers:** Content-Type: application/json
- **Body (raw JSON):**

```json
{
  "nome": "João da Silva",
  "cpf": "12345678901",
  "telefone": "11987654321",
  "email": "joao@email.com"
}
```

#### 4. Realizar Venda
- **Method:** POST
- **URL:** http://localhost:8080/api/vendas
- **Headers:** Content-Type: application/json
- **Body (raw JSON):**

```json
{
  "veiculoId": 1,
  "clienteId": 1,
  "valorVenda": 85000.00
}
```

---

## 🗄️ ACESSAR CONSOLE H2

1. Abra o navegador em: http://localhost:8080/h2-console
2. Configure:
   - **JDBC URL:** `jdbc:h2:mem:agencia_veiculos`
   - **Username:** `sa`
   - **Password:** (deixe em branco)
3. Clique em **Connect**

### Queries úteis:

```sql
-- Ver todos os veículos
SELECT * FROM VEICULOS;

-- Ver veículos disponíveis
SELECT * FROM VEICULOS WHERE DISPONIVEL = TRUE;

-- Ver todos os clientes
SELECT * FROM CLIENTES;

-- Ver todas as vendas com dados completos
SELECT 
    v.ID AS VENDA_ID,
    v.VALOR_VENDA,
    v.DATA_VENDA,
    ve.MARCA,
    ve.MODELO,
    ve.PLACA,
    c.NOME AS CLIENTE_NOME,
    c.CPF
FROM VENDAS v
JOIN VEICULOS ve ON v.VEICULO_ID = ve.ID
JOIN CLIENTES c ON v.CLIENTE_ID = c.ID;
```

---

## 🎯 Resumo dos Status HTTP Retornados

| Código | Situação | Exemplo |
|--------|----------|---------|
| **200** | OK | Operação bem-sucedida (GET, PUT) | Buscar veículo, listar vendas |
| **201** | Created | Recurso criado (POST) | Cadastrar veículo, realizar venda |
| **204** | No Content | Operação bem-sucedida sem retorno (DELETE) | Deletar veículo |
| **400** | Bad Request | Dados inválidos | CPF inválido, placa duplicada |
| **404** | Not Found | Recurso não encontrado | Buscar veículo inexistente |
| **409** | Conflict | Conflito de estado | Tentar vender veículo já vendido |

---

## 🎯 Endpoints disponíveis:

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/veiculos` | Cadastrar veículo |
| `GET` | `/api/veiculos` | Listar todos |
| `GET` | `/api/veiculos/{id}` | Buscar por ID |
| `GET` | `/api/veiculos/disponiveis` | Listar disponíveis |
| `PUT` | `/api/veiculos/{id}` | Atualizar |
| `DELETE` | `/api/veiculos/{id}` | Deletar |
| `POST` | `/api/clientes` | Cadastrar cliente |
| `GET` | `/api/clientes/{id}` | Buscar por ID |
| `GET` | `/api/clientes/cpf/{cpf}` | Buscar por CPF |
| `POST` | `/api/vendas` | Realizar venda |
| `GET` | `/api/vendas` | Listar vendas |
| `GET` | `/api/vendas/veiculo/{id}` | Buscar por veículo |

---

<div align="center">

**Agora você tem todos os endpoints implementados e pode testar a API completa!** 🚀

</div></parameter>
