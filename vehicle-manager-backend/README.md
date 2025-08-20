# Vehicle Manager — Backend (Quarkus)

API RESTful construída com **Quarkus 3.25.3** (Java **21**), Hibernate ORM com Panache e JWT.

> **Pastas**: `/mnt/data/vehicle-manager/vehicle-manager/vehicle-manager-backend`

## Requisitos

- **Java 21** (JDK 21) — verifique com: `java -version`
- **Maven** (o projeto inclui `./mvnw`, então não precisa instalar Maven globalmente)
- **MySQL 8.0+**

## Configuração do Banco de Dados (MySQL)

O backend está configurado por padrão para conectar em:
```
URL:  jdbc:mysql://localhost:3306/vehicle_manager
User: admin
Pass: 12345678
```
(veja `src/main/resources/application.properties` — `quarkus.datasource.*`)

O Hibernate está com **`quarkus.hibernate-orm.database.generation=update`**, então as tabelas serão criadas/atualizadas automaticamente.

### Opção A — Docker Compose (recomendado para desenvolvimento)

Crie um arquivo `docker-compose.mysql.yml` com o conteúdo:

```yaml
version: "3.9"
services:
  mysql:
    image: mysql:8.0
    container_name: vm_mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: vehicle_manager
      MYSQL_USER: admin
      MYSQL_PASSWORD: 12345678
    ports:
      - "3306:3306"
    command: ["--default-authentication-plugin=caching_sha2_password"]
    healthcheck:
      test: ["CMD-SHELL", "mysqladmin ping -h 127.0.0.1 -u$$MYSQL_USER -p$$MYSQL_PASSWORD"]
      interval: 10s
      timeout: 5s
      retries: 10
    volumes:
      - ./mysql-data:/var/lib/mysql
```

Suba o banco:

```bash
docker compose -f docker-compose.mysql.yml up -d
```

> **Dica**: se quiser um client web, rode também o Adminer:
> ```yaml
>   adminer:
>     image: adminer
>     ports:
>       - "8081:8080"
> ```
> Acesse em http://localhost:8081

### Opção B — MySQL local (manual)

Conecte-se como root e execute:

```sql
CREATE DATABASE IF NOT EXISTS vehicle_manager
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY '12345678';
GRANT ALL PRIVILEGES ON vehicle_manager.* TO 'admin'@'%';
FLUSH PRIVILEGES;
```

## Usuário Administrador Inicial

Há duas formas fáceis de garantir que o banco **inicie com 1 usuário admin** (tabela `users`):

### 1) Usando `import.sql` do Hibernate (automático ao subir)

Crie o arquivo `src/main/resources/import.sql` **no backend** com o conteúdo abaixo **antes** de iniciar a aplicação. E garanta esta linha no `application.properties` (se não existir, adicione):

```
quarkus.hibernate-orm.sql-load-script=import.sql
```

> Senha padrão: **Admin@123** (bcrypt).

```sql
INSERT INTO users (name, email, password, isAdmin)
VALUES ('Administrador', 'admin@local', '$2b$10$3aUb7M4fyj3Va4oyYxXHNOk3aS6syXZ4S6keju.XZCwGLNr8hxrIu', true)
ON DUPLICATE KEY UPDATE email = email;
```

### 2) Inserção manual (após a primeira subida)

Inicie o backend uma vez (para criar as tabelas) e depois execute no MySQL:

```sql
INSERT INTO users (name, email, password, isAdmin)
VALUES ('Administrador', 'admin@local', '$2b$10$3aUb7M4fyj3Va4oyYxXHNOk3aS6syXZ4S6keju.XZCwGLNr8hxrIu', true);
```

> Troque a senha em produção. O hash é **bcrypt/10** da senha `Admin@123`.

## Como rodar o Backend

No diretório do backend:

```bash
cd vehicle-manager/vehicle-manager/vehicle-manager-backend

# (opcional) conferir Java
java -version

# Rodar em modo dev (hot reload)
./mvnw quarkus:dev
```

A API sobe por padrão em: **http://localhost:8080**.

### Testes (backend)

```bash
./mvnw test
```

## Autenticação & Endpoints principais

- **Login**: `POST /api/v1/auth/login`
    - Request JSON:
      ```json
      { "email": "admin@local", "password": "Admin@123" }
      ```
    - Response:
      ```json
      { "accessToken": "...", "refreshToken": "..." }
      ```

- **Refresh**: `POST /api/v1/auth/refresh`
    - Request JSON:
      ```json
      { "refreshToken": "..." }
      ```

- **Usuários** (`/api/v1/users`) – registrar, atualizar, apagar
- **Marcas** (`/api/v1/brands`), **Modelos** (`/api/v1/modelos`), **Veículos** (`/api/v1/vehicles`)

> O CORS já está habilitado para `*` durante o desenvolvimento em `application.properties`.
