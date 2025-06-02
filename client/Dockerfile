# 빌드 단계
FROM node:23-alpine AS build

WORKDIR /app

ARG VITE_API_URL
ARG API_URL

ENV VITE_API_URL=$VITE_API_URL
ENV API_URL=$API_URL

COPY package.json package-lock.json ./

RUN npm ci

COPY . .

RUN npm run build

# 실행 단계
FROM nginx:mainline-alpine-slim

ARG VITE_API_URL
ARG API_URL

ENV API_URL=$API_URL
ENV VITE_API_URL=$VITE_API_URL

COPY nginx.conf /etc/nginx/nginx.conf
COPY ./templates /etc/nginx/templates
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]