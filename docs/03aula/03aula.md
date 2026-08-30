# Aula 03 — Modelo de Domínio

## Objetivo

Criar o modelo de domínio inicial do projeto Repor+ e aplicar as primeiras regras de negócio.

## Classes criadas

* `Produto.java`
* `GrupoProduto.java`
* `Status.java`

## Regras implementadas

### Produto

* Código de barras obrigatório.
* Descrição obrigatória.
* Saldo de estoque não pode ser negativo.
* Valor unitário não pode ser negativo.
* Entrada de estoque.
* Saída de estoque.
* Não permite retirar mais do que o saldo disponível.
* Pode ser ativado ou inativado.
* Calcula o valor total do estoque.

### GrupoProduto

* Possui nome e status.
* Permite adicionar produtos.
* Não permite produtos com código de barras repetido.
* Um produto não pode pertencer a dois grupos.
* A lista de produtos não pode ser alterada diretamente.

## Testes

Foram criados testes unitários utilizando JUnit:

* `ProdutoTest.java`
* `GrupoProdutoTest.java`

## Conclusão

A Aula 03 foi concluída com o modelo de domínio do Repor+ e seus respectivos testes unitários.
