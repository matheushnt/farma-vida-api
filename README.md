# Farma Vida

Sistema de gestão de vendas de medicamentos com aplicação de benefícios por convênio e fechamento periódico de faturas.

## Descrição
O Farma Vida é uma aplicação desenvolvida com Spring Boot para gerenciar medicamentos, clientes, convênios, vendas e faturas.

Durante o registro de uma venda, o cliente pode utilizar um convênio para obter descontos na compra. O sistema aplica as regras de elegibilidade do convênio item a item, validando, entre outros critérios, se o medicamento pertence às categorias cobertas pelo plano informado e se ainda há limite mensal disponível. Por isso uma mesma venda pode ter parte do valor coberta pelo convênio e parte paga integralmente pelo cliente.

As vendas vinculadas a um convênio são posteriormente incorporadas em faturas por período. O processo de fechamento é **idempotente**, garantindo que um mesmo convênio não possua mais de uma fatura para o mesmo intervalo de cobrança, mesmo diante de tentativas repetidas de processamento.

O projeto tem como foco a implementação de **regras de negócio**, a **integridade** das operações de venda e a **consistência** do processo de faturamento.

## Sumário
- [Pré-requisitos](#pré-requisitos)
- [Como executar a aplicação](#como-executar-a-aplicação)
- [Endpoints da API](#endpoints-da-api)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Decisões tomadas](#decisões-tomadas)
- [Observações](#observações)

## Pré-requisitos
Antes de começar, certifique-se de ter instalado:
- Docker Desktop (Windows/macOS) ou Docker Engine com Docker Compose v2 (Linux);
- Git (opcional, caso queira clonar o repositório).

## Como executar a aplicação
Na raiz do projeto, crie o arquivo `.env` a partir do exemplo.

No Linux/macOS:
```bash
cp .env.example .env
```

Abra `.env` e defina o valor de `POSTGRES_PASSWORD`. O Docker Compose carrega esse arquivo automaticamente.

Na raiz do projeto, construa a imagem e inicie a aplicação e o banco de dados:
```bash
docker compose up --build -d
```

Na primeira inicialização, a aplicação pode levar alguns instantes para ficar disponível. Acompanhe os logs:
```bash
docker compose logs -f application
```
Quando os logs indicarem que a aplicação iniciou, pressione `Ctrl+C` para encerrar o acompanhamento. Os contêineres continuarão em execução.

Verifique a API em `http://localhost:8080/cliente`. Em uma instalação nova, a lista de clientes estará vazia.

Para parar os serviços sem remover os dados do banco:
```bash
docker compose down
```

## Endpoints da API
### Medicamentos
#### POST `/medicamento`
Adiciona um novo medicamento.
##### Exemplo de requisição
```bash
curl -X POST http://localhost:8080/medicamento \
  -H "Content-Type: application/json" \
  -d '{
     "nome": "Dipirona 500mg",
     "categoria": "generico",
     "preco": 5.98
  }'
```
##### Respostas
- `201 Created` - Medicamento criado com sucesso;
- `422 Unprocessable Entity` - Erro de validação quando não atende às regras;
- `400 Bad Request` - Erro na requisição.
#### GET `/medicamento`
Retorna todos os medicamentos registrados.
##### Exemplo de requisição
```bash
curl http://localhost:8080/medicamento
```
##### Respostas
- `200 OK` - Todos os medicamentos.
### Clientes
#### POST `/cliente`
Adiciona um novo cliente.
##### Exemplo de requisição
```bash
curl -X POST http://localhost:8080/cliente \
  -H "Content-Type: application/json" \
  -d '{
     "nome": "John Doe",
     "cpf": "52005459003",
     "planoSaudeId": "f807f40c-07fa-42e9-9017-d8df919442d3"
  }'
```
A propriedade `planoSaudeId` é opcional, pois nem todo cliente possui um plano de saúde.
##### Respostas
- `201 Created` - Cliente criado com sucesso.
- `422 Unprocessable Entity`
  - Erro de validação quando não atende às regras;
  - CPF inválido informado;
- `404 Not Found` - Plano de saúde informado não encontrado;
- `409 Conflict` - Outro cliente já cadastrado com o mesmo CPF
- `400 Bad Request` - Erro na requisição.
#### GET `/cliente`
Retorna todos os clientes.
##### Exemplo de requisição
```bash
curl http://localhost:8080/cliente
```
##### Respostas
- `200 OK` - Todos os clientes.
### Planos de Saúde
#### POST `/plano-saude`
Adiciona um novo plano de saúde.
##### Exemplo de requisição
```bash
curl -X POST http://localhost:8080/plano-saude \
  -H "Content-Type: application/json" \
  -d '{
     "nome": "Plano Vida",
     "percentualDesconto": 0.25,
     "categoriasCobertas": ["GENERICO", "REFERENCIA", "SIMILAR"],
     "limiteMensal": 250
  }'
```
##### Respostas
- `201 Created` - Plano de saúde criado com sucesso.
- `422 Unprocessable Entity` - Erro de validação quando não atende às regras;
- `400 Bad Request` - Erro na requisição.
#### GET `/plano-saude`
Retorna todos os planos de saúde.
##### Exemplo de requisição
```bash
curl http://localhost:8080/plano-saude
```
##### Respostas
- `200 OK` - Todos os planos de saúde.
### Vendas
#### POST `/venda`
Registra uma venda.
##### Exemplo de requisição
```bash
curl -X POST http://localhost:8080/venda \
  -H "Content-Type: application/json" \
  -d '{
     "clienteId": "f7f1decd-26fa-4715-9c73-19f82d04827f",
     "usarConvenio": true,
     "itens": [
        { "medicamentoId": "6dcfb4fa-dc44-43dc-b36a-b6cb7e37f58a", "quantidade": 4 }
     ]
  }'
```
##### Respostas
- `201 Created` - Venda registrada com sucesso.
- `422 Unprocessable Entity` - Erro de validação quando não atende às regras;
- `404 Not Found`
  - Cliente não encontrado;
  - Um ou mais medicamentos informados não foram encontrados;
- `400 Bad Request` - Erro na requisição.
#### GET `/venda/{vendaId}`
Retorna os detalhes de uma venda.
##### Exemplo de requisição
```bash
curl http://localhost:8080/venda/c54d88c0-061e-4885-abe9-e7fd0a251153
```
##### Respostas
- `200 OK` - Detalhes da venda.
### Faturas
#### POST `/fatura/{planoSaudeId}`
Fecha a fatura de um plano de saúde em um período específico.
##### Exemplo de requisição
```bash
curl -X POST http://localhost:8080/fatura/f807f40c-07fa-42e9-9017-d8df919442d3 \
  -H "Content-Type: application/json" \
  -d '{
     "competencia": "2026-08"
  }'
```
##### Respostas
- `201 Created` - Fechamento de fatura realizado com sucesso.
- `200 OK` - Retorna fatura existente;
- `404 Not Found`
  - Plano de Saúde não encontrado;
  - Vendas pendentes não encontradas para faturamento para o plano e competência;
- `400 Bad Request`
  - Competência inválida. Ex.: 2026-13
  - Erro na requisição;
#### GET `/fatura/{faturaId}`
Retorna os detalhes de uma fatura.
##### Exemplo de requisição
```bash
curl http://localhost:8080/fatura/5ce5929e-ff7e-44d5-99eb-2fefee0be4ff
```
##### Respostas
- `200 OK` - Detalhes da fatura.

## Tecnologias utilizadas
- **Java 21** - Linguagem de programação;
- **Spring Boot 4.0.7** - Framework principal;
- **PostgreSQL 17** - Banco de dados relacional;
- **Spring Data JPA e Hibernate** - Persistência e mapeamento objeto-relacional;
- **Maven Wrapper** - Build e gerenciamento de dependências;
- **Docker e Docker Compose** - Build e execução dos serviços;
- **Lombok** - Redução de código boilerplate.

## Decisões tomadas
- Na modelagem original, a entidade `VendaItem` tinha um campo `valorPagoConvenio`, usado para saber quanto cada item representava do valor pago pelo convênio. Ao longo do projeto, percebi que essa granularidade era desnecessária e removi o campo, migrando o valor pago pelo convênio para a entidade `Venda`. Caso essa granularidade volte a ser necessária no futuro, duas abordagens seriam possíveis: **por ordem de aparição na requisição** ou **por distribuição igualitária**, cada uma com seus próprios _trade-offs_.
- O fechamento da fatura é **idempotente**. Para garantir isso, adicionei um **índice único** combinando as colunas `competencia` e `plano_saude_id` na entidade `Fatura`, além de adotar um **Lock Pessimista** ao buscar todas as vendas pendentes de faturamento. Essa abordagem foi mais simples do que o **Lock Otimista**, pois não preciso tratar a exceção `DataIntegrityViolationException`. Por outro lado, o Lock Pessimista aumenta o tempo de espera para acessar registros que estão em uso por outra transação. Neste projeto, creio que em um fluxo real de produção seria pequena a chance de duas transações acessarem o mesmo recurso ao mesmo tempo, pois penso isso com base na natureza da operação (fechar uma fatura ocorre uma vez por mês, e apenas por pessoas com papéis permitidos).
- Para o registro de uma venda e o fechamento de fatura, usei **transações** para garantir a **consistência** e a **integridade** das informações, salvando tudo ou revertendo a operação por completo em caso de falha.

## Observações
- O Docker Compose persiste os dados do PostgreSQL no volume `postgres-data`, inclusive após a parada ou recriação dos contêineres;
- O arquivo `.env` contém credenciais locais e não deve ser versionado; use `.env.example` como referência;
- Os termos **plano de saúde** e **convênio** são usados de maneira intercambiável durante este arquivo;
- Na raiz do projeto, há a pasta `postman` que inclui a coleção de requisições. Você pode importar no Postman para fazer as requisições de maneira mais fácil.
