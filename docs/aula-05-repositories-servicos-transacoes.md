# Aula 05 — repositories, serviços e transações

## Tradução do projeto de referência

O exemplo `GrupoProduto` e `Produto` do `suporteos2026` foi traduzido para
`CategoriaPeca` e `Peca`. A estrutura técnica é a mesma, mas os nomes e regras
continuam pertencendo ao tema individual Repor+.

| Projeto de referência | Repor+ |
| --- | --- |
| `GrupoProduto` | `CategoriaPeca` |
| `Produto` | `Peca` |
| código de barras | código da peça |
| receber quantidade | receber estoque |

## Responsabilidades

- a entidade mantém seu estado válido e executa regras de domínio;
- o repository recupera e armazena entidades;
- o serviço coordena o caso de uso e define sua transação;
- o banco preserva os dados e suas restrições.

O acesso ao banco não é espalhado pelas entidades ou pelos serviços. As
interfaces que estendem `JpaRepository` são implementadas pelo Spring durante
a inicialização da aplicação.

## Consultas derivadas

`CategoriaPecaRepository` consulta e verifica o nome ignorando diferenças entre
maiúsculas e minúsculas. `PecaRepository` consulta pelo código, categoria e
status. O Spring Data interpreta esses nomes e produz as consultas.

## Serviços de aplicação

`CategoriaPecaService` cadastra, busca e lista categorias. Ele rejeita um nome
já utilizado com `RecursoDuplicadoException`.

`PecaService` cadastra uma peça em uma categoria existente, consulta peças por
categoria ou status e recebe estoque. Código repetido gera
`RecursoDuplicadoException`; identificador inexistente gera
`RecursoNaoEncontradoException`.

As exceções representam falhas da aplicação e não conhecem códigos HTTP. Essa
responsabilidade pertence à camada de API, que será estudada posteriormente.

## Transações e dirty checking

Métodos de escrita usam `@Transactional`. Consultas usam
`@Transactional(readOnly = true)` para comunicar que não pretendem alterar os
dados.

Ao receber estoque, a peça recuperada está no estado `managed`. O método de
domínio altera a quantidade e o Hibernate detecta a mudança no final da
transação, realizando o `UPDATE` sem um segundo `save`. Isso é dirty checking.

Quando uma categoria não existe, o cadastro lança uma exceção e nenhuma peça é
gravada. O teste automatizado confirma esse comportamento de rollback.

## Evidências automatizadas

Os testes verificam:

- cadastro e listagem de categorias;
- nome de categoria duplicado;
- cadastro da peça com sua categoria;
- código de peça duplicado;
- consulta pelo relacionamento e pelo status;
- atualização de estoque por dirty checking;
- falha com categoria inexistente sem gravação parcial.

Esta aula não adiciona controllers nem DTOs.
