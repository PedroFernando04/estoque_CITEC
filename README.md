# Estoque CITEC

Sistema web de controle de estoque para o CITEC (CESMAC). Ele registra os itens do almoxarifado, quanto há de cada um, em qual projeto cada item está e todo o histórico de movimentações, além de pedidos de material, ordens de serviço e transportes.

Projeto de portfólio feito em **Java + Spring Boot + Thymeleaf**. O back-end foi desenvolvido por mim; o redesenho do front-end foi feito em parceria com o **Claude** (IA da Anthropic). A seção [Como usei IA neste projeto](#como-usei-ia-neste-projeto) explica o que foi feito em cada parte.

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0-6DB33F?style=flat&logo=springboot&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=flat&logo=thymeleaf&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white)

---

## O que o sistema faz

- **Projetos (locais de estoque):** cada projeto, aula ou setor do CITEC tem o próprio estoque, com solicitante, responsáveis, categoria e status.
- **Itens:** cadastro com nome, nome fantasia, código RM (formato `00.00.000`), foto e quantidade. O sistema calcula o total, o que está em uso e o que está disponível, e permite marcar um item como "em falta".
- **Movimentação entre projetos:** itens saem do estoque geral, vão para um projeto, voltam ao estoque ou passam de um projeto para outro. Cada movimento vira uma linha no histórico.
- **Histórico:** registro de entradas, saídas e transferências, com origem, destino, quantidade, data e usuário. Pode ser filtrado.
- **Pedidos:** requisição de material (com lista de itens e quantidades), ordem de serviço e transporte, com fluxo de status (solicitado → recebido, ou cancelado).
- **Funcionários e acesso:** cadastro de funcionários com cargo, login e senha, e consulta dos projetos de cada um.
- **Aviso de itens em falta:** um alerta aparece nas telas principais quando há itens marcados como faltando.

---

## Tecnologias

| Camada | Tecnologia |
| --- | --- |
| Linguagem | Java 25 |
| Framework | Spring Boot 4.0 (Web MVC, Data JPA, Validation) |
| Segurança | Spring Security 6 com senhas em BCrypt |
| Banco de dados | PostgreSQL (via Hibernate/JPA) |
| Views | Thymeleaf, `thymeleaf-extras-springsecurity6`, HTML e CSS puro |
| Build | Maven (com wrapper `mvnw`) |
| Outros | Lombok |

---

## Conceitos de Java e Spring que este projeto pratica

- **Arquitetura em camadas:** `controllers` → `services` (interface + implementação) → `repositorys` → `entities`, cada camada com uma responsabilidade.
- **Modelagem com JPA:** entidades principais (`Item`, `Estoque`, `Pedido`, `Funcionario`, `Projeto`) e tabelas associativas com `@ManyToOne` (`ItemEstoque`, `ItemPedido`, `FuncionarioProjeto`, `Movimentacao`).
- **Enums com descrição:** cargo, categoria e status de projeto e de pedido, tipo de OS, tipo de transporte e tipo de movimentação. O texto exibido vem do próprio enum, e o back-end entrega à view a classe CSS de cada status.
- **Filtros dinâmicos com `Specification`:** pesquisas combinando nome, status, categoria, origem, destino e item, sem escrever uma query para cada combinação.
- **Paginação:** `PageRequest` e `Page` nas listagens, com navegação entre páginas na tela.
- **DTOs:** `ItensComQuantidadeDTO` e `PedidoComItensDTO` para levar à view só os dados de que ela precisa.
- **Regras de negócio nos services:** validações como nome duplicado, quantidade negativa, quantidade removida maior que a estocada e item ausente na origem informada.
- **Tratamento global de erros:** `@ControllerAdvice` e `@ExceptionHandler` levam as mensagens de erro para uma tela própria.
- **Segurança:** `SecurityFilterChain`, login customizado, `BCryptPasswordEncoder` e controle de rotas públicas e protegidas.
- **Upload de arquivos:** fotos dos itens gravadas em disco com nome único (`UUID`) e servidas por um `ResourceHandler` configurado em `WebConfig`.
- **`@ModelAttribute` global:** `@ControllerAdvice` que injeta em todas as telas a quantidade de itens em falta.
- **Thymeleaf:** fragmentos reutilizáveis (menu e aviso de itens em falta), `th:each`, `th:if`, `th:classappend`, formatação de datas e `sec:authorize`.

---

## Modelo de dados

```text
Funcionario ──< FuncionarioProjeto >── Estoque (projeto/local)
                                          │
Item ──< ItemEstoque >────────────────────┘
 │
 ├──< ItemPedido >── Pedido ── Estoque (destino)
 │
 └──< Movimentacao (origem, destino, quantidade, data, usuário)
```

---

## Principais rotas

| Rota | O que faz |
| --- | --- |
| `/locais`, `/local/{id}` | Lista de projetos, detalhe, itens do projeto, cadastro e edição |
| `/itens`, `/itens/projetos/{id}` | Estoque de itens, onde cada item está, cadastro e edição |
| `/pedidos`, `/pedidos/{id}` | Pedidos, itens do pedido, mudança de status, cadastro e edição |
| `/historico` | Histórico de movimentações com filtros |
| `/funcionarios`, `/funcionarios/projetos/{id}` | Funcionários e os projetos de cada um |
| `/entrar` | Login |

---

## Como executar

**Requisitos:** JDK 25, Maven (ou o `mvnw` do projeto) e PostgreSQL.

1. Crie o banco de dados:

   ```sql
   CREATE DATABASE "Estoque_CITEC";
   ```

2. Ajuste usuário e senha em `estoque/src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/Estoque_CITEC
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA
   ```

   > Em um ambiente real, não deixe a senha no arquivo. Use variáveis de ambiente, por exemplo `SPRING_DATASOURCE_PASSWORD`.

3. Rode a aplicação:

   ```bash
   cd estoque
   ./mvnw spring-boot:run
   ```

4. Abra `http://localhost:8080`. Cadastre o primeiro funcionário pela tela de login ("Cadastre-se") e entre com ele.

As tabelas são criadas automaticamente pelo Hibernate (`ddl-auto=update`). As fotos enviadas ficam na pasta `uploads/`, na raiz do projeto.

---

## Estrutura do projeto

```text
estoque_CITEC/
├── uploads/                         # fotos dos itens
└── estoque/
    ├── pom.xml
    └── src/main/
        ├── java/com/citec/estoque/
        │   ├── config/              # segurança e arquivos estáticos
        │   ├── controllers/         # rotas web
        │   ├── dtos/
        │   ├── entities/            # entidades JPA e enums
        │   ├── exception/           # tratamento global de erros
        │   ├── repositorys/         # Spring Data JPA
        │   ├── services/            # regras de negócio
        │   └── specification/       # filtros dinâmicos
        └── resources/
            ├── static/css/style.css # estilos
            ├── static/imgs/
            └── templates/           # páginas Thymeleaf
```

---

## Como usei IA neste projeto

Usei o **Claude (Anthropic)** como ferramenta de trabalho, não como substituto de entender o código.

**O que pedi:** refazer todo o front-end (que eu tinha feito em Thymeleaf, HTML e CSS) deixando-o moderno e bonito, **sem mexer nas funcionalidades do back-end**.

**Como foi o trabalho com a IA:**

1. **Leitura antes de alterar.** O Claude abriu os 18 templates e o CSS e conferiu como cada tela se liga ao back-end (controllers, nomes de campos, rotas, classes CSS devolvidas pelos enums).
2. **Restrição clara.** Todos os `th:*`, `name` de campos, rotas e funções JavaScript existentes foram preservados. O objetivo era mudar só a aparência e a organização do HTML.
3. **Identidade visual a partir do contexto.** O menu lateral usa o azul-marinho do logo do CESMAC, e as etiquetas de status têm cores consistentes em todo o sistema.
4. **Verificação.** Os nomes de campos de formulário dos templates novos foram comparados com os dos originais, e as telas foram renderizadas em um navegador, em desktop e celular, para conferir o resultado.
5. **Revisão minha.** Testei as telas rodando o sistema e decidi o que manter.

**O que a IA mudou (somente front-end):**

- CSS reescrito do zero, com variáveis de cor e tipografia, responsivo e com estados de foco para teclado.
- Menu lateral com página ativa, tabelas e formulários refeitos, janelas de confirmação e estados vazios.
- Pequenas correções que ela encontrou nos templates: link de paginação sem barra, `th.selected` e `th:placholder` com erro de digitação, e campo de senha como texto visível.

**O que aprendi sobre trabalhar com IA:**

- Dar uma restrição explícita ("não altere o back") evita retrabalho.
- Pedir para o resultado ser conferido contra o original (e não só "parecer certo") pega erros cedo.
- A IA acelera o trabalho repetitivo, como refazer 18 telas. As decisões de regra de negócio e de modelagem continuam sendo minhas.

<!-- AJUSTE: se você também usou IA em outras partes (dúvidas de Spring, revisão de código, testes), acrescente aqui com suas palavras. -->

---

## Melhorias futuras

- Testes automatizados (hoje só existe o teste de contexto gerado pelo Spring Initializr).
- Mover credenciais do banco para variáveis de ambiente.
- Perfis de usuário com permissões diferentes (hoje todo usuário autenticado tem o mesmo acesso).
- Proteção CSRF (está desabilitada) e validação mais forte dos formulários.
- Painel inicial com indicadores (itens em falta, pedidos pendentes).
- Docker Compose com a aplicação e o PostgreSQL.
- API REST para integração com outros sistemas.

---

## Autor

**Pedro Fernando**
GitHub: [PedroFernando04](https://github.com/PedroFernando04)

---

## Licença

Projeto acadêmico e de portfólio, livre para estudo.
