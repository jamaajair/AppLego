#docker image rm -f applego-backend applego-frontend || true

#docker build -t applego-frontend ./frontend

#docker build -t applego-backend ./backend

#docker compose up -d

docker-compose down --rmi all

docker-compose up --build -d