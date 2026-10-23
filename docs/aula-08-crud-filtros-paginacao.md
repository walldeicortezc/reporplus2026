# Aula 08 — CRUD completo, estoque e consultas paginadas

## Adaptação para o Repor+

O exemplo de grupo de produto e produto foi transferido para `CategoriaPeca` e
`Peca`. O estoque do Repor+ permanece inteiro porque representa unidades
físicas de peças. `valorUnitario` corresponde a `custoUnitario`.

Campos editáveis de uma peça:

- descrição;
- custo unitário;
- estoque mínimo;
- categoria;
- fornecedor, que pode ser removido com `null`.

Código, quantidade em estoque, data de cadastro e status são controlados por
operações específicas e não pertencem ao DTO geral de atualização.

## Contrato das rotas novas

| Operação | Método e URI | Entrada | Sucesso | Erros esperados |
|---|---|---|---|---|
| Alterar categoria | `PUT /api/categorias-pecas/{id}` | nome | `200` | `400`, `404`, `409` |
| Alterar status da categoria | `PUT /api/categorias-pecas/{id}/status` | status | `200` | `400`, `404` |
| Excluir categoria | `DELETE /api/categorias-pecas/{id}` | — | `204` | `404`, `409` |
| Pesquisar categorias | `GET /api/categorias-pecas` | filtros e página | `200` | `400` |
| Alterar peça | `PUT /api/pecas/{id}` | dados editáveis | `200` | `400`, `404` |
| Alterar status da peça | `PUT /api/pecas/{id}/status` | status | `200` | `400`, `404` |
| Entrada de estoque | `POST /api/pecas/{id}/estoque/entradas` | quantidade | `200` | `400`, `404` |
| Saída de estoque | `POST /api/pecas/{id}/estoque/saidas` | quantidade | `200` | `400`, `404` |
| Excluir peça | `DELETE /api/pecas/{id}` | — | `204` | `404` |
| Pesquisar peças | `GET /api/pecas` | filtros e página | `200` | `400` |

## Filtros, paginação e ordenação

Categorias aceitam `nome` e `status`. Peças aceitam `descricao`, `status`,
`categoriaId`, `fornecedorId` e `abaixoEstoqueMinimo`.

As consultas recebem `page`, `size` e `sort`. O tamanho padrão é 20 e o máximo
é 100. Campos de ordenação são validados por lista permitida antes da consulta.
As respostas utilizam `PaginaResponse`, com `conteudo`, `pagina`, `tamanho`,
`totalElementos`, `totalPaginas`, `primeira` e `ultima`.

## Decisões HTTP

- `PUT` atualiza a representação editável e é idempotente.
- `POST` registra entrada ou saída, eventos não idempotentes.
- `DELETE` remove fisicamente e retorna `204` sem corpo.
- Inativação preserva identificador e relacionamentos; não equivale à exclusão.
- Regras ou corpos inválidos retornam `400`.
- Recursos inexistentes retornam `404`.
- Duplicidade e categoria ainda utilizada retornam `409`.

## Persistência e integridade

A migração `004-unique-nome-categoria-peca.yaml` cria no PostgreSQL um índice
único sobre `LOWER(BTRIM(nome))`. Uma variação equivalente exclusiva para H2
mantém os testes automatizados compatíveis com o profile atual.

O repository de peças usa `JpaSpecificationExecutor` e `@EntityGraph` para
carregar categoria e fornecedor durante consultas paginadas, mantendo
`spring.jpa.open-in-view=false`.

## Evidências automatizadas

A classe `Aula08ApiTest` cobre atualização, status, movimentação, filtros,
paginação, ordenação inválida, exclusão e conflitos. Os testes de domínio
cobrem alteração de dados, troca de categoria nos dois lados da associação e
remoção de fornecedor. `PersistenciaJpaTest` comprova a unicidade normalizada.

Execute:

```powershell
.\mvnw.cmd test
```

A coleção `postman/ReporPlus-Aula08.postman_collection.json` contém o fluxo
completo e deve ser executada na ordem apresentada, deixando as exclusões por
último.
