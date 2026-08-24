# Repor+

## Identificação

- Nome do projeto: reporplus2026
- Tema: controle e reposição de peças para caminão oficina de máquinas agrícolas
- Objetivo em uma frase: controlar o estoque de peças e identificar quais peças precisam ser repostas

## Entidade de classificação

- Nome no singular: CategoriaPeca
- Nome no plural: CategoriasPeca
- Exemplo 1: Hidráulica
- Exemplo 2: Elétrica

## Entidade principal

- Nome no singular: Peca
- Nome no plural: Pecas
- Código único: código da peça
- Descrição: descrição da peça
- Medida quantitativa: quantidade de peças
- Valor monetário: valor de reposição
- Data relevante: data de cadastro
- Status: ativo ou inativo

## Relacionamento

- Uma categoria pode possuir várias peças.
- Cada peça pertence a uma categoria.

## Três exemplos de registros

1. Código: 001 - Mangueira hidráulica - Categoria: Hidráulica
2. Código: 002 - Filtro hidráulico - Categoria: Hidráulica
3. Código: 003 - Sensor de rotação - Categoria: Elétrica