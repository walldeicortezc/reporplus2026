# Aula 07 – API REST, DTOs e mapeadores

## Tradução para o Repor+

Os três recursos da API são `CategoriaPeca`, `Fornecedor` e `Peca`. Cada um
possui um contrato de entrada e outro de saída. As entidades JPA permanecem na
camada de domínio e não são serializadas diretamente.

```text
Cliente -> Controller -> Service -> Repository -> Banco
             |              |
            DTO          Entidade
             \-- Mapper --/
```

- DTO define o formato do JSON;
- mapper converte DTO e entidade sem consultar o banco;
- controller interpreta HTTP;
- service executa regras e transações;
- repository acessa os dados.

## Rotas e escolhas de status

| Situação | Status | Motivo |
|---|---:|---|
| cadastro concluído | `201 Created` | um novo recurso foi criado e o cabeçalho `Location` indica sua URL |
| consulta ou listagem | `200 OK` | a requisição foi executada com sucesso |
| validação ou JSON inválido | `400 Bad Request` | o cliente enviou dados que não formam uma entrada válida |
| recurso ou relacionamento ausente | `404 Not Found` | o identificador solicitado não existe |
| nome, CNPJ ou código repetido | `409 Conflict` | a entrada conflita com um recurso já cadastrado |

O contrato `ApiError` sempre informa instante, status, tipo, mensagem, caminho
e erros por campo. Stack trace, SQL e credenciais nunca são devolvidos.

## Validações de entrada

- nomes, código e descrição não podem estar vazios;
- os tamanhos respeitam as colunas do banco;
- quantidades, custo e estoque mínimo não podem ser negativos;
- categoria é obrigatória e fornecedor é opcional;
- CNPJ deve conter exatamente 14 dígitos.

Os DTOs protegem a fronteira HTTP, mas não substituem as regras das entidades.
O domínio continua se protegendo quando for chamado por outro adaptador.

## Testes automatizados

`ApiRestTest` usa MockMvc e verifica mais do que o mínimo de cinco cenários:

1. cadastro de categoria com `201`;
2. cadastro de fornecedor com `201`;
3. cadastro de peça relacionada com `201` e `Location`;
4. listagem com `200`;
5. entrada inválida com `400` e erros por campo;
6. peça inexistente com `404`;
7. categoria duplicada com `409`;
8. JSON malformado com `400`.

## Passo a passo do Postman

1. Inicie a aplicação.
2. No Postman, escolha **Import**.
3. Importe `postman/ReporPlus-Aula07.postman_collection.json`.
4. Abra a coleção `Repor+ - Aula 07`.
5. Execute as requisições na ordem `00` até `08`.

Os IDs criados são salvos automaticamente nas variáveis `categoriaId`,
`fornecedorId` e `pecaId`. A coleção inclui o caminho feliz e os erros `400`,
`404` e `409`. Ela não possui senhas, tokens, cookies nem URLs privadas.

Se a coleção for executada novamente no mesmo banco, altere os valores únicos
do nome da categoria, CNPJ e código da peça.
