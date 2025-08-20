# Vehicle Manager — Frontend (Angular 20)

Frontend em **Angular 20**. Por padrão, ele chama a API em `http://localhost:8080/api/v1`.

> **Pastas**: `vehicle-manager/vehicle-manager/vehicle-manager-frontend`

## Requisitos

- **Node.js 20 LTS** (sugestão: use nvm)
- **npm** 10+

## Instalação

```bash
cd vehicle-manager/vehicle-manager/vehicle-manager-frontend
npm ci    # ou: npm install
```

### Configurar URL da API (se necessário)

Por padrão o projeto usa:
`src/app/environments/environment.ts`

```ts
export const environment = {
  production: false,
  baseApiUrl: "http://localhost:8080/api/v1",
};
```

Altere `baseApiUrl` caso rode o backend em outro host/porta.

## Executar em desenvolvimento

```bash
npm start    # atalho para: ng serve
# abre em http://localhost:4200
```

> Como o backend permite CORS `*`, não é necessário proxy no `ng serve`.  
> Se preferir proxy reverso, crie um `proxy.conf.json` e adicione o `--proxy-config` no script `start`.

## Build de produção

```bash
npm run build
# artefatos em dist/
```

## Testes (frontend)

```bash
npm test    # ng test
```
