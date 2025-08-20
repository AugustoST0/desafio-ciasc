# Vehicle Manager — Guia de Execução

Este README reúne **passos de execução do projeto completo** (backend Quarkus + frontend Angular + banco MySQL) e **como rodar os testes**.  
O repositório tem a seguinte estrutura relevante:

```
vehicle-manager/
├─ .env                       # Variáveis usadas pelo docker-compose
├─ docker-compose.yml         # Sobe DB, Backend e Frontend
├─ db/init/01-seed.sql        # Script que cria usuário(s) inicial(is)
├─ vehicle-manager-backend/   # API (Quarkus 3, Java 21, Maven)
└─ vehicle-manager-frontend/  # Angular 20
```

---

## 1) Pré‑requisitos

> Você pode executar **com Docker** (recomendado) ou **localmente**. Escolha um caminho e siga as seções correspondentes.

### Com Docker (recomendado)

- Docker (20+)
- Docker Compose (v2)

### Sem Docker (execução local)

- **Java 21** (Temurin/OpenJDK)  
- **Maven 3.9+**
- **Node.js 20** e **npm 10** (Angular 20)
- **MySQL 8** rodando localmente

---

## 2) Variáveis de ambiente (.env)

O arquivo `.env` na raiz já traz valores padrão (podem ser ajustados):

```env
# Banco
MYSQL_ROOT_PASSWORD=password
MYSQL_DATABASE=vehicle_manager_db
MYSQL_USER=admin
MYSQL_PASSWORD=12345678

# Backend (Quarkus)
QUARKUS_HTTP_PORT=8080
QUARKUS_HTTP_CORS=true
QUARKUS_HTTP_CORS_ORIGINS=*
```

> O `docker-compose.yml` usa essas variáveis. O banco sobe com um **seed** em `db/init/01-seed.sql` (vide seção de credenciais).

---

## 3) Subir tudo com Docker

Na raiz do repositório (`vehicle-manager/`):

```bash
# 1) (Opcional) Limpar caches antigos de build
docker compose down -v 2>/dev/null || true

# 2) Subir TUDO (DB, Backend, Frontend)
docker compose up -d --build
```

A pilha sobe com:
- **MySQL** (porta `3306`)
- **Backend Quarkus** (porta `8080` interna / acessível via proxy do Nginx)
- **Frontend Angular** servido pelo **Nginx** (porta **`80`** → http://localhost )

O Nginx já faz **proxy** de `http://localhost/api/v1/...` para o backend (service `backend`), conforme `vehicle-manager-frontend/docker/nginx/default.conf`.

> **Dica:** para ver logs agregados:
>
> ```bash
> docker compose logs -f
> ```

Para **parar** tudo:

```bash
docker compose down
```

Para **parar e remover volumes** (apaga dados do banco):

```bash
docker compose down -v
```

---

## 4) Execução local (sem Docker)

### 4.1) Banco de dados MySQL

1. Suba o MySQL 8 em `localhost:3306` e crie um usuário/senha compatível com `src/main/resources/application.properties` do backend **ou** ajuste este arquivo para o seu ambiente local:
   ```properties
   quarkus.datasource.db-kind=mysql
   quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/vehicle_manager_db
   quarkus.datasource.username=admin
   quarkus.datasource.password=12345678
   ```

2. (Opcional) Rode o seed inicial manualmente (criação de tabela `users` e usuários padrão):
   ```bash
   mysql -uadmin -p12345678 vehicle_manager_db < db/init/01-seed.sql
   ```

> O projeto usa `quarkus.hibernate-orm.database.generation=update`, então o **Hibernate cria/atualiza** as tabelas automaticamente no primeiro start.

### 4.2) Backend (Quarkus 3, Java 21)

No diretório `vehicle-manager-backend/`:

```bash
# Instalar dependências e rodar em modo dev (hot reload)
./mvnw quarkus:dev
# ...ou com Maven instalado no PATH:
mvn quarkus:dev
```

- A API sobe por padrão em **http://localhost:8080**.
- Em **dev**, o Swagger UI costuma estar em **`/q/swagger-ui`** (se habilitado).

Para **build de produção** (JAR):
```bash
mvn -DskipTests package
java -jar target/quarkus-app/quarkus-run.jar
```

### 4.3) Frontend (Angular 20)

No diretório `vehicle-manager-frontend/`:

```bash
# Instalar dependências
npm ci

# Rodar em dev
npm start
# (equivale a: ng serve)
```

- A aplicação web sobe em **http://localhost:4200**.
- O frontend está configurado para usar a API em `http://localhost:8080/api/v1` (ver `src/app/environments/environment*.ts`).
- Se for **apontar para outro host/porta**, ajuste `baseApiUrl` nos arquivos `environment.ts`/`environment.prod.ts`.

> **CORS**: no `application.properties` já há `quarkus.http.cors=true`.  
> Em produção via Docker, o Nginx faz proxy de `/api/v1` → backend, evitando problemas de CORS.

---

## 5) Credenciais iniciais

O script `db/init/01-seed.sql` cria usuários padrão na tabela `users`:

- **admin** — email `admin@exemplo.com` — `isAdmin = 1`
- **user**  — email `user@exemplo.com`  — `isAdmin = 0`

As senhas no seed estão **com hash Bcrypt**. Caso não saiba a senha em texto puro, você tem duas opções:

1. **Definir sua própria senha**: gere um hash Bcrypt e atualize via SQL:
   ```sql
   UPDATE users SET password = '<SEU_HASH_BCRYPT>' WHERE email = 'admin@exemplo.com';
   ```

2. **Criar um novo usuário admin** com a senha que desejar (gerando o hash antes) e removendo o antigo.

> O backend utiliza Bcrypt (biblioteca `at.favre.lib.crypto.bcrypt`) para validar senha no login.

---

## 6) Como rodar os testes

### Backend (Quarkus)

No diretório `vehicle-manager-backend/`:

```bash
# Testes unitários e de integração
mvn test

# Cobertura + verificação completa do build
mvn verify
```

Os testes incluem mapeadores de exceção, modelos e testes de recursos/integração (p.ex. `AuthResourceIntegrationTest`, `VehicleResourceIntegrationTest`, etc.).

### Frontend (Angular)

No diretório `vehicle-manager-frontend/`:

```bash
# Testes unitários com Karma/Jasmine
npm test
```

> Se você estiver rodando via Docker, os testes **não** são executados automaticamente no `docker-compose up`. Execute-os localmente com Node/npm conforme acima.

---

## 7) Endpoints principais (referência rápida)

Prefixo base: **`/api/v1`**

- **Auth**: `POST /auth/login`, `POST /auth/refresh`
- **Users**: `GET /users`, `GET /users/{id}`, `POST /users/register`, `PUT /users/{id}`, `DELETE /users/{id}`
- **Vehicles / Brands / Models**: CRUD em `/vehicles`, `/brands`, `/models`

> Em dev, se o Swagger UI estiver habilitado, acesse `http://localhost:8080/q/swagger-ui`.

---

## 8) Dicas de troubleshooting

- **Portas ocupadas** (`3306`, `8080`, `80/4200`): pare serviços locais que conflitam ou altere as portas no `.env`/config.
- **CORS em dev local**: garanta `quarkus.http.cors=true` ou acesse via Nginx (Docker) que já faz proxy.
- **Seed não rodou** no MySQL do Docker: confirme o volume `./db/init:/docker-entrypoint-initdb.d:ro` e **suba o compose do zero**:
  ```bash
  docker compose down -v && docker compose up -d --build
  ```
- **Banco com dados antigos**: use `docker compose down -v` para resetar, ou rode migrações/seeds manualmente.
- **Angular apontando para host errado**: ajuste `environment.ts`/`environment.prod.ts` (`baseApiUrl`).

---

## 9) Comandos úteis

```bash
# Backend: limpar e compilar
mvn clean package

# Backend: rodar com perfis/propriedades customizadas
mvn quarkus:dev -Dquarkus.http.port=8081

# Docker: rebuild apenas do backend
docker compose build backend && docker compose up -d backend

# Docker: logs
docker compose logs -f backend
docker compose logs -f db
docker compose logs -f frontend
```

---

## 10) Acesso rápido

- **App (Docker)**: http://localhost  
- **API dev**: http://localhost:8080  
- **Swagger UI (dev, se habilitado)**: http://localhost:8080/q/swagger-ui

---

Se quiser, eu também posso gerar **READMEs separados** (backend e frontend) com instruções específicas, ou adicionar **scripts `Makefile`/`npm run`** para encurtar os comandos do dia‑a‑dia.
