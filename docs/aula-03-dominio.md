# Aula 03 — modelo de domínio

## Tradução do projeto de referência

O código do `suporteos2026` é usado apenas para demonstrar os conceitos. No
projeto individual Repor+, os nomes e as regras foram traduzidos para o tema de
reposição de peças.

| Projeto de referência | Repor+ | Significado no tema |
|---|---|---|
| `GrupoProduto` | `CategoriaPeca` | classifica peças por função |
| `Produto` | `Peca` | item controlado no estoque |
| saldo | quantidade em estoque | número inteiro de unidades |
| valor unitário | custo unitário | custo monetário de reposição |

## Classes criadas

- `Status`: representa os estados `ATIVO` e `INATIVO`;
- `CategoriaPeca`: mantém o nome, o status e sua coleção protegida de peças;
- `Peca`: mantém código, descrição, quantidade, custo, data, status e categoria.

As classes são objetos Java puros. Anotações de JPA, repositories e configurações
de banco não foram antecipadas porque pertencem à Aula 04.

## Invariantes implementadas

1. Textos obrigatórios não aceitam valor nulo ou vazio.
2. Quantidade e custo não aceitam valores negativos.
3. Movimentações de estoque precisam ser positivas.
4. Uma retirada não pode superar a quantidade disponível.
5. Uma peça pertence a, no máximo, uma categoria.
6. Um código não pode ser repetido dentro da mesma categoria.
7. A coleção interna da categoria não pode ser modificada externamente.

As quantidades utilizam `int` porque o domínio controla unidades discretas. O
custo utiliza `BigDecimal` para evitar perda de precisão em valores monetários.

## Testes automatizados

- `PecaTest` verifica criação, status inicial, cálculo do valor do estoque,
  entrada, retirada e rejeição de valores inválidos;
- `CategoriaPecaTest` verifica a associação, a proteção da coleção e a rejeição
  de código duplicado;
- o teste de contexto confirma que a aplicação Spring continua iniciando.



