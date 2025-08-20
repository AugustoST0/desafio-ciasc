# Build Angular
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .

RUN npm run build -- --configuration=production

# Serve com Nginx
FROM nginx:1.27-alpine
# Copia config do Nginx (proxy para backend)
COPY docker/nginx/default.conf /etc/nginx/conf.d/default.conf

COPY --from=build /app/dist/vehicle-manager-frontend/browser /usr/share/nginx/html
EXPOSE 80

