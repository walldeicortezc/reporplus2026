# Aula 04 — persistência com JPA e Liquibase

## Tradução do projeto de referência

No `suporteos2026`, o professor demonstra como mapear `GrupoProduto` e
`Produto`. No Repor+, o mesmo conceito foi traduzido para `CategoriaPeca` e
`Peca`, preservando os nomes e as regras próprias do tema.

## Responsabilidades

- JPA relaciona as classes Java às tabelas;
- PostgreSQL armazena os dados no ambiente de desenvolvimento;
- H2 fornece um banco temporário e isolado para os testes automatizados;
- repositories oferecem operações de persistência;
- Liquibase cria e versiona o esquema de forma reproduzível;
- Hibernate usa `ddl-auto=validate` e não cria tabelas automaticamente.

## Mapeamento

```text
categoria_peca 1 -------- N peca
```

- `CategoriaPeca` usa `@Entity`, chave primária gerada e relação `@OneToMany`;
- `Peca` usa `@Entity`, código único e relação obrigatória `@ManyToOne`;
- o enum `Status` é armazenado como texto com `EnumType.STRING`;
- `BigDecimal` é persistido como `NUMERIC(18,2)`;
- a data de cadastro é persistida como `DATE`.

O construtor sem argumentos é `protected` para uso do JPA. Os construtores
públicos e as regras da Aula 03 foram mantidos.

## Repositories

- `CategoriaPecaRepository` estende `JpaRepository<CategoriaPeca, Long>`;
- `PecaRepository` estende `JpaRepository<Peca, Long>`;
- consultas derivadas verificam nomes, códigos e peças por categoria.

## Migrations

- `001-create-categoria-peca.yaml` cria a tabela de classificação;
- `002-create-peca.yaml` cria a entidade principal, chave estrangeira,
  unicidade e restrições de domínio;
- `db.changelog-master.yaml` inclui as migrations na ordem correta.

As restrições do banco impedem estoque e custo negativos e limitam o status aos
valores `ATIVO` e `INATIVO`, mesmo quando uma escrita não passa pelas entidades.

## Ambientes

- `reporplus2026_dev`: execução local da aplicação;
- `reporplus2026_test`: banco H2 em memória, criado e descartado pelos testes;
- `.env`: credenciais locais ignoradas pelo Git;
- `.env.example`: variáveis do PostgreSQL de desenvolvimento sem credenciais reais.

Essa separação permite executar `mvnw.cmd test` sem senha local e sem gravar
dados no banco usado pela aplicação.
