FROM node:20.16 AS build

ARG PROFILE=local
ENV APP_PROFILE=${PROFILE}
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build-${APP_PROFILE}

FROM nginx:1.26.1-alpine

COPY nginx.conf /etc/ngnix/nginx.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80 443 5173




