# Build
FROM node:23-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npx tsc --noEmit --skipLibCheck true || true
RUN npx vite build

# Serveur statique
FROM node:23-alpine
WORKDIR /app
RUN npm install -g serve
COPY --from=build --chown=1001:1001 /app/dist ./dist
RUN chown -R 1001:1001 /app
USER 1001
EXPOSE 3000
CMD ["serve", "-s", "dist", "-l", "3000"]