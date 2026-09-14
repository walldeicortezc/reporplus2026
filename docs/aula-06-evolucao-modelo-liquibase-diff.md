# Aula 06 – Evolução do modelo e Liquibase Diff

## Tradução para o tema Repor+

O exemplo da aula do professor foi traduzido para o controle de reposição de
peças. O modelo ganhou a entidade `Fornecedor`, com razão social, CNPJ único e
status. A entidade `Peca` ganhou `estoqueMinimo` e um fornecedor opcional.

Foi usado `int` no estoque mínimo porque o Repor+ controla peças em unidades
inteiras. Essa escolha diferencia as regras do projeto individual do exemplo
apresentado em aula.

## Estratégia expand–migrate–contract

A migration `003-fornecedor-e-estoque-minimo.yaml` não altera as migrations
`001` e `002`, que já representam o histórico aplicado.

1. **Expand:** cria `estoque_minimo` aceitando nulo.
2. **Migrate:** preenche com zero todas as peças antigas.
3. **Contract:** torna a coluna obrigatória e adiciona a regra `>= 0`.
4. Cria `fornecedor`, protege CNPJ e status, e adiciona a relação opcional.

Assim, os dados anteriores continuam válidos durante a evolução do banco.

## Geração assistida e revisão do rascunho

O Liquibase Maven Plugin foi configurado para comparar um banco `diff`, criado
pelas migrations atuais, com um banco `reference`, criado pelo Hibernate. O
arquivo gerado fica em `target/liquibase-diff` e serve apenas como rascunho.

Antes de escrever a migration final, o rascunho gerado precisa ser revisado.
Problemas que uma geração automática pode apresentar:

1. adicionar `estoque_minimo` como `NOT NULL` imediatamente, quebrando linhas
   antigas que ainda não possuem valor;
2. tentar remover e recriar chaves estrangeiras existentes sem necessidade;
3. criar nomes automáticos pouco claros para constraints e changeSets;
4. deixar de criar as regras de domínio `estoque_minimo >= 0`, CNPJ único e
   status permitido;
5. gerar rollback incompleto ou perigoso para dados existentes.

Por isso o rascunho não foi copiado diretamente. A migration final foi escrita
e revisada com nomes estáveis, ordem segura e rollback estrutural.

## Bancos descartáveis

As variáveis `DB_DIFF_*` e `DB_REFERENCE_*` estão documentadas em
`.env.example`. Esses bancos são temporários e nunca devem apontar para produção.
Senhas reais permanecem apenas no `.env`, que é ignorado pelo Git.

O perfil `schema-reference` desliga o Liquibase e permite que o Hibernate crie
somente o esquema de referência. No ambiente normal, o Liquibase continua sendo
o responsável pelas tabelas e o Hibernate apenas valida o resultado.

## Evidências automatizadas

- peças cadastradas antes da Aula 06 são preservadas com estoque mínimo zero;
- a relação com fornecedor permanece opcional;
- CNPJ duplicado é recusado pelo serviço e pelo banco;
- estoque mínimo negativo é recusado pelo domínio e pelo banco;
- o cadastro com fornecedor inexistente faz rollback;
- o Hibernate valida se as entidades correspondem ao esquema das migrations.
