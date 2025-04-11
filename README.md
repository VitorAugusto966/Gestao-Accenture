# Sistema de Gestão de Empresas e Fornecedores

Este é um sistema completo de gestão de empresas e fornecedores desenvolvido com **Vue 3 + TypeScript** no frontend e **Java + Spring Boot** no backend. A proposta é permitir o cadastro, edição, vínculo e visualização dessas entidades de forma prática, validada e com uma interface moderna.

## 🚀 Tecnologias Utilizadas

### Frontend
- [Vue 3](https://vuejs.org/)
- [TypeScript](https://www.typescriptlang.org/)
- [Vue Router](https://router.vuejs.org/)
- [Axios](https://axios-http.com/)
- [Toastification](https://vue-toastification.maronato.dev/)
- [Vite](https://vitejs.dev/)

### Backend
- Java 21
- Spring Boot
- Spring Data JPA
- Jakarta Bean Validation
- MySQL

## 💻 Funcionalidades

- Cadastro e edição de **empresas** com:
  - Validação de CNPJ
  - Máscara e validação de CEP (via ViaCEP)
  - Seleção múltipla de fornecedores vinculados

- Cadastro e edição de **fornecedores** com:
  - Validação de documento
  - Máscara de CEP
  - Vinculação a múltiplas empresas

- Listagem com filtros
- Layout responsivo e moderno
- Utilização de `localStorage` para preencher dados na navegação
- Feedbacks visuais com `toast` para sucesso e erro

## ⚙️ Como Executar

### Backend
```bash
./mvn clean install
./mvnw spring-boot:run
```

> Certifique-se de configurar corretamente o arquivo `application.properties` com as credenciais do seu banco MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

A aplicação estará disponível em `http://localhost:5173/`

---
