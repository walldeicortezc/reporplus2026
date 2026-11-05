# Aulas 09 a 17 no Repor+

Este documento registra a adaptação dos requisitos do projeto do professor ao
sistema de controle de reposição e venda de peças do Repor+.

## Aula 09 — OpenAPI

- Springdoc publica `/v3/api-docs`, `/v3/api-docs.yaml` e o Swagger UI.
- `OpenApiConfig` descreve a API e o esquema Bearer JWT.
- O endpoint `/api/health` e os contratos principais estão documentados.
- O perfil `prod` desabilita Swagger UI e a especificação OpenAPI.

## Aula 10 — pessoas e vendas

- `Pessoa` centraliza nome, e-mail, CPF, cadastro e status.
- `Cliente` e `Colaborador` complementam a pessoa sem duplicar identidade.
- `Venda` relaciona cliente, vendedor e itens.
- `ItemVenda` referencia uma `Peca`, usa quantidade inteira e preserva o custo
  unitário praticado.
- A migration `005` cria as tabelas e restrições.

## Aula 11 — concorrência

- A finalização ordena os itens pelo ID da peça.
- Cada peça recebe bloqueio pessimista antes da baixa.
- Falta de estoque causa rollback de toda a venda.
- O teste concorrente confirma que apenas uma venda consome o saldo disputado.

## Aula 12 — autenticação

- `ContaAcesso` separa credenciais dos dados civis.
- Senhas são armazenadas com BCrypt.
- A migration `006` cria contas e perfis de acesso.

## Aula 13 — JWT

- O login devolve access token assinado em HMAC-SHA256.
- A API valida assinatura, emissor e expiração como Resource Server.
- A chave vem de `JWT_SECRET` e não deve ser publicada com valor real.

## Aula 14 — autorização

- Os perfis são `OPERADOR` e `ADMINISTRADOR`.
- Endpoints administrativos exigem o perfil correspondente.
- O administrador inicial é opcional e controlado por `INITIAL_ADMIN_*`.

## Aula 15 — testes e cobertura

- H2 em modo PostgreSQL executa testes sem alterar o banco de desenvolvimento.
- A suíte cobre domínio, serviços, migrations, API, segurança e concorrência.
- `mvn verify` exige 70% de linhas e 40% de desvios pelo JaCoCo.

## Aula 16 — refresh token

- Somente o hash SHA-256 do refresh token é persistido.
- A renovação rotaciona o token e impede reuso.
- Reutilização revoga a família; logout individual e global revogam sessões.
- A migration `007` cria as sessões e os índices.

## Aula 17 — proteção e auditoria

- Falhas consecutivas são controladas por hash do login.
- Limite, janela e duração do bloqueio são configuráveis.
- Login, refresh, logout e alterações administrativas geram auditoria.
- Senhas, tokens e login em texto aberto não são gravados na auditoria.
- A migration `008` cria as tabelas e índices de segurança.

## Como executar

1. Copie `.env.example` para `.env` e configure PostgreSQL e segredos locais.
2. Execute `.\mvnw.cmd verify` no Windows.
3. Inicie a aplicação com o perfil `dev`.
4. Acesse o Swagger UI, faça login e use o token Bearer nos endpoints protegidos.
