# Tema do projeto — Repor+

## Descrição

API didática para controle de estoque e reposição de peças utilizadas em oficinas, caminhões e máquinas agrícolas.

## Entidades do domínio

### CategoriaPeca

Entidade de classificação responsável por organizar as peças em categorias, como Motor, Freios, Elétrica e Hidráulica.

### Peca

Entidade principal que representa uma peça armazenada no estoque.

## Relacionamento

Uma categoria pode classificar várias peças, enquanto cada peça pertence a uma categoria.

```text
CategoriaPeca 1 -------- N Peca