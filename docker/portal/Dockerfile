FROM node:20.16 AS build

WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --omit=dev

FROM nginx:1.26.1-alpine

COPY nginx.conf /etc/ngnix/nginx.conf
COPY --from=build /app/dist/farm-byte-portal/browser /usr/share/nginx/html

EXPOSE 80 443 4200




