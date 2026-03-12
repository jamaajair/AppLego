# Build
FROM gitlab.uliege.be:5050/speam/2025-2026/team8/applego/node:22-alpine as build

ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npx tsc --noEmit --skipLibCheck true || true
RUN npx vite build
RUN echo "VITE_API_BASE_URL=$VITE_API_BASE_URL"

# Serveur statique
FROM gitlab.uliege.be:5050/speam/2025-2026/team8/applego/node:22-alpine
WORKDIR /app
RUN npm install -g serve
COPY --from=build --chown=1001:1001 /app/dist ./dist
RUN chown -R 1001:1001 /app
USER 1001
EXPOSE 3000
CMD ["serve", "-s", "dist", "-l", "3000"]