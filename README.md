# Repor+

## Tema

API didática para controle de estoque e reposição de peças utilizadas em
oficinas, caminhões e máquinas agrícolas.

## Objetivo

Cadastrar peças, organizá-las por categoria e aplicar regras de domínio para
entrada, retirada e cálculo do valor armazenado em estoque.

## Modelo de domínio

```text
CategoriaPeca 1 -------- N Peca N -------- 0..1 Fornecedor
```

- `CategoriaPeca`: classifica as peças por função, como Motor, Freios, Elétrica
  e Hidráulica.
- `Peca`: representa o item controlado no estoque por código único, descrição,
  quantidade, estoque mínimo, custo unitário, data de cadastro e status.
- `Fornecedor`: identifica quem fornece uma peça, por razão social e CNPJ.
- `Status`: informa se uma categoria, peça ou fornecedor está `ATIVO` ou
  `INATIVO`.

Uma categoria classifica várias peças. Cada peça exige uma categoria e pode ter
um fornecedor. O fornecedor é opcional para preservar os cadastros antigos.

## Regras implementadas

- nomes, códigos e descrições são obrigatórios;
- quantidade e custo não podem ser negativos;
- entradas e retiradas precisam ser maiores que zero;
- uma retirada não pode superar o estoque disponível;
- códigos de peças não podem se repetir dentro da mesma categoria;
- uma peça não pode ser transferida diretamente para outra categoria;
- a coleção interna de peças da categoria é protegida contra alteração externa;
- o valor do estoque é calculado pela quantidade multiplicada pelo custo
  unitário.
- categorias com o mesmo nome não podem ser cadastradas, sem diferenciar
  letras maiúsculas e minúsculas;
- códigos de peças são únicos em todo o estoque;
- o cadastro de uma peça exige uma categoria existente;
- os casos de uso de escrita são executados dentro de transações.
- estoque mínimo não pode ser negativo;
- o sistema identifica quando a quantidade está abaixo do estoque mínimo;
- CNPJ possui 14 dígitos e não pode se repetir;
- uma peça pode ser cadastrada com ou sem fornecedor.

## Tecnologias

- Java 21;
- Spring Boot 4.0.7;
- Maven Wrapper;
- Spring Web MVC;
- Validation;
- Spring Data JPA;
- PostgreSQL;
- H2 para testes automatizados;
- Liquibase;
- JUnit 5.

O Liquibase cria e versiona o esquema; o Hibernate apenas valida se os
mapeamentos JPA correspondem às tabelas.

## Configuração local do banco

Copie `.env.example` para `.env` e informe somente as senhas locais. O arquivo
`.env` é ignorado pelo Git e nunca deve ser publicado.

## Executando os testes

Os testes usam um banco H2 temporário em memória. Portanto, não precisam da
senha do PostgreSQL nem alteram o banco de desenvolvimento.

No Windows:

```powershell
.\mvnw.cmd test
```

## Executando a aplicação

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

Com a aplicação iniciada, acesse `http://localhost:8080/api/health`. A resposta
esperada é `OK`, com status HTTP 200.

## API REST

| Recurso | Cadastro | Listagem | Consulta por ID |
|---|---|---|---|
| Categorias | `POST /api/categorias-pecas` | `GET /api/categorias-pecas` | `GET /api/categorias-pecas/{id}` |
| Fornecedores | `POST /api/fornecedores` | `GET /api/fornecedores` | `GET /api/fornecedores/{id}` |
| Peças | `POST /api/pecas` | `GET /api/pecas` | `GET /api/pecas/{id}` |

Cadastros devolvem `201 Created`. Consultas devolvem `200 OK`. Dados inválidos
devolvem `400`, recursos inexistentes devolvem `404` e valores únicos repetidos
devolvem `409`. As entidades JPA não são expostas: a API utiliza DTOs e
mapeadores manuais.

## Documentação

- `docs/tema-do-projeto.md`: ficha e limites do tema individual;
- `docs/aula-03-dominio.md`: tradução do exemplo do professor para o domínio do
  Repor+ e regras testadas;
- `docs/aula-04-persistencia.md`: mapeamento JPA, PostgreSQL, repositories e
  migrations do Liquibase.
- `docs/aula-05-repositories-servicos-transacoes.md`: consultas derivadas,
  serviços transacionais, dirty checking e rollback.
- `docs/aula-06-evolucao-modelo-liquibase-diff.md`: fornecedor, estoque mínimo,
  comparação de esquemas e migração segura.
- `docs/aula-07-api-rest-dtos-mappers.md`: contratos JSON, validação,
  controllers, erros HTTP, MockMvc e roteiro do Postman.
