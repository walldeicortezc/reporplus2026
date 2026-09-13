# Repor+

## Tema

API didática para controle de estoque e reposição de peças utilizadas em
oficinas, caminhões e máquinas agrícolas.

## Objetivo

Cadastrar peças, organizá-las por categoria e aplicar regras de domínio para
entrada, retirada e cálculo do valor armazenado em estoque.

## Modelo de domínio

```text
CategoriaPeca 1 -------- N Peca
```

- `CategoriaPeca`: classifica as peças por função, como Motor, Freios, Elétrica
  e Hidráulica.
- `Peca`: representa o item controlado no estoque por código único, descrição,
  quantidade, custo unitário, data de cadastro e status.
- `Status`: informa se uma categoria ou peça está `ATIVO` ou `INATIVO`.

Uma categoria pode classificar várias peças, enquanto cada peça pertence a, no
máximo, uma categoria.

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

## Tecnologias

- Java 21;
- Spring Boot 4.0.7;
- Maven Wrapper;
- Spring Web MVC;
- Validation;
- JUnit 5.

Nesta etapa, o projeto ainda não utiliza JPA, banco de dados ou Liquibase.

## Executando os testes

No Windows:

```powershell
.\mvnw.cmd test
```

## Executando a aplicação

```powershell
.\mvnw.cmd spring-boot:run
```

Com a aplicação iniciada, acesse `http://localhost:8080/api/health`. A resposta
esperada é `OK`, com status HTTP 200.

## Documentação

- `docs/tema-do-projeto.md`: ficha e limites do tema individual;
- `docs/aula-03-dominio.md`: tradução do exemplo do professor para o domínio do
  Repor+ e regras testadas.
