# 📦 Sistema de Controle de Estoque - CITEC

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge\&logo=java\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge\&logo=springboot\&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge)
![Status](https://img.shields.io/badge/status-completo-green?style=for-the-badge)
![License](https://img.shields.io/badge/license-Acadêmico-blue?style=for-the-badge)

---

## 📌 Sobre o Projeto

O **estoque_CITEC** é uma aplicação web desenvolvida com **Spring Boot** e **Thymeleaf**, voltada para o gerenciamento de estoque de produtos.

O sistema permite controlar entradas e saídas de itens, mantendo o estoque atualizado de forma simples, organizada e eficiente.

Este projeto foi desenvolvido com foco acadêmico, aplicando conceitos modernos de desenvolvimento backend com Java e integração com interface web dinâmica.

---

## 🚀 Funcionalidades

* 📦 Cadastro de produtos
* ➕ Registro de entrada de itens
* ➖ Registro de saída de itens
* 📊 Visualização do estoque atualizado
* 🔍 Consulta e busca de produtos
* 📁 Upload de arquivos (armazenados na pasta `uploads`)

---

## 🧠 Como o sistema funciona

O fluxo principal do sistema é:

1. O usuário cadastra os produtos no sistema
2. Realiza movimentações:

   * Entrada → aumenta a quantidade em estoque
   * Saída → reduz a quantidade disponível
3. O sistema atualiza automaticamente os dados
4. O usuário pode consultar o estoque a qualquer momento

---

## 🛠️ Tecnologias Utilizadas

* ☕ Java
* 🚀 Spring Boot
* 🌐 Thymeleaf
* 🗄️ JPA / Hibernate
* 📦 Maven
* 🎨 HTML & CSS

---

## 📁 Estrutura do Projeto

```bash
estoque_CITEC/
│-- src/
│   ├── controllers/     # Controladores (requisições web)
│   ├── services/        # Regras de negócio
│   ├── repositories/    # Acesso a dados
│   ├── models/          # Entidades do sistema
│   └── resources/
│       ├── templates/   # Páginas Thymeleaf
│       └── static/      # CSS, JS, imagens
│-- uploads/             # Arquivos enviados
│-- pom.xml              # Configuração Maven
```

---

## ⚙️ Como Executar o Projeto

### 🔽 1. Clonar o repositório

```bash
git clone https://github.com/PedroFernando04/estoque_CITEC.git
```

---

### 📂 2. Acessar o diretório

```bash
cd estoque_CITEC
```

---

### ▶️ 3. Executar a aplicação

Se estiver usando Maven:

```bash
mvn spring-boot:run
```

Ou execute diretamente pela sua IDE (IntelliJ ou Eclipse)

---

### 🌐 4. Acessar no navegador

```bash
http://localhost:8080
```

---

## 📷 Demonstração

> 💡 Recomenda-se adicionar prints ou GIFs do sistema para enriquecer o projeto

---

## 🎯 Objetivo

O objetivo deste projeto é aplicar na prática conceitos de:

* Desenvolvimento web com Java
* Arquitetura MVC
* Separação de responsabilidades
* Manipulação de dados e regras de negócio
* Integração entre backend e frontend

---

## 🚧 Melhorias Futuras

* 🔐 Sistema de autenticação de usuários
* 🐘 Integração com PostgreSQL
* 📊 Dashboard com gráficos
* 🔎 Filtros avançados de busca
* 📱 Interface responsiva
* 📦 API REST para integração externa

---

## 🤝 Contribuição

Contribuições são bem-vindas!

1. Faça um fork do projeto
2. Crie uma branch (`git checkout -b feature/minha-feature`)
3. Commit suas alterações
4. Push para a branch
5. Abra um Pull Request

---

## 👨‍💻 Autor

Pedro Fernando
🔗 https://github.com/PedroFernando04

---

## 📄 Licença

Este projeto é de uso acadêmico e livre para estudos.

---
