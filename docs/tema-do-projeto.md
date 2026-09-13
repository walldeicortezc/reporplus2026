# Tema do projeto — Repor+

## Identificação

- Nome do projeto: `Repor+`;
- identificador técnico: `reporplus2026`;
- tema: controle de estoque e reposição de peças;
- objetivo: cadastrar peças, organizá-las por categoria e controlar sua
  disponibilidade em oficinas, caminhões e máquinas agrícolas.

## Entidade de classificação

- Nome no singular: `CategoriaPeca`;
- nome no plural: categorias de peças;
- descrição: classificação funcional da peça;
- exemplos: Motor, Freios, Elétrica e Hidráulica;
- status: `ATIVO` ou `INATIVO`.

## Entidade principal

- Nome no singular: `Peca`;
- nome no plural: peças;
- código único: código interno ou SKU;
- descrição: nome comercial da peça;
- medida quantitativa: quantidade em estoque;
- valor monetário: custo unitário de reposição;
- valor calculado: quantidade em estoque multiplicada pelo custo unitário;
- data relevante: data de cadastro;
- status: `ATIVO` ou `INATIVO`.

## Relacionamento

Uma categoria pode classificar várias peças, enquanto cada peça pertence a, no
máximo, uma categoria.

```text
CategoriaPeca 1 -------- N Peca
```

## Regras de domínio

- toda peça deve possuir código e descrição;
- a quantidade em estoque não pode ser negativa;
- o custo unitário não pode ser negativo;
- entradas e retiradas devem ser maiores que zero;
- uma retirada não pode superar a quantidade disponível;
- o mesmo código não pode ser repetido dentro de uma categoria;
- uma peça já associada não pode ser transferida diretamente para outra
  categoria;
- a lista interna de peças não pode ser alterada externamente.

## Exemplos fictícios

| Categoria | Código | Peça | Estoque | Custo unitário |
|---|---|---|---:|---:|
| Motor | `MOT-FIL-001` | Filtro de óleo | 12 | 48,90 |
| Freios | `FRE-PAS-002` | Pastilha de freio | 8 | 189,50 |
| Hidráulica | `HID-MAN-003` | Mangueira hidráulica | 5 | 240,00 |

## Limite de escopo

Clientes, veículos, ordens de serviço, pagamentos e manutenções não fazem parte
do núcleo inicial. Novos conceitos só serão acrescentados nas etapas em que
forem solicitados.
