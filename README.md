![Java 21](https://img.shields.io/badge/Java-21-blue?logo=java&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-C71A36?logo=apachemaven&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-6DB33F?logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Containerized-2496ED?logo=docker&logoColor=white)
[![Continuous Integration With Github Actions](https://github.com/sahid-sousa/rest-with-spring-boot/actions/workflows/continuous-integration.yml/badge.svg)](https://github.com/sahid-sousa/rest-with-spring-boot/actions/workflows/continuous-integration.yml)

# 📡 Feed Hub

Feed Hub é uma API desenvolvida em Java com Spring Boot e Mavem, utilizando PostgreSQL como banco de dados. O projeto está containerizado com Docker para facilitar o ambiente de desenvolvimento e execução.

---

## 🚀 Tecnologias Utilizadas

- Java 21
- Spring Boot
- PostgreSQL 12.4
- Docker
- Docker Compose

---

## 🛠️ Pré-requisitos

Certifique-se de ter os seguintes softwares instalados:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)

---

## 🧪 Subindo a aplicação com Docker Compose

Para rodar a aplicação localmente com Docker Compose:

```
docker-compose up --build
```

> A aplicação será exposta na porta `8085` e o banco de dados na porta `5432`.

---

## 🌐 Endpoints

Após o container estar em execução, a API poderá ser acessada via:

```
http://localhost:8085/swagger-ui/index.html
```

---

## ⚙️ Configurações

As principais variáveis de ambiente já estão definidas no `docker-compose.yml`:

| Variável               | Valor                                             | Descrição                           |
|------------------------|---------------------------------------------------|-------------------------------------|
| `SERVER_PORT`          | 8080                                              | Porta interna do container          |
| `DATASOURCE_URL`       | jdbc:postgresql://postgres-db/feedhub_development | URL do banco de dados               |
| `DATASOURCE_USERNAME`  | postgres                                          | Usuário do banco de dados           |
| `DATASOURCE_PASSWORD`  | postgres                                          | Senha do banco de dados             |
| `DATASOURCE_DBCREATE`  | update                                            | Estratégia de atualização do schema |
| `SECRET_KEY`           | 4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P                   | Chave secreta para autenticação JWT |
| `ISSUER`               | feedhub                                           | Emissor do token                    |
| `VALIDITY`             | 3600000                                           | Validade do token em milissegundos  |

---

## 🩺 Healthcheck

O container `postgres-db` possui um healthcheck configurado para garantir que o banco esteja pronto antes de iniciar a aplicação.

---

## 📂 Estrutura do Projeto

A aplicação Spring Boot é empacotada como um `.jar` e copiada via Dockerfile:

```
FROM openjdk:21
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

## 🧼 Parando os containers

Para parar e remover os containers criados:

```
docker-compose down
```

> Para remover volumes e dados persistentes, execute com `-v`:
```
docker-compose down -v
```





