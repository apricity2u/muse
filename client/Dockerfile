# 빌드 단계
FROM node:23-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./

RUN npm ci

COPY . .

RUN npm run build

# 실행 단계
FROM nginx:mainline-alpine-slim

COPY nginx.conf /etc/nginx/nginx.conf
COPY ./templates /etc/nginx/templates
COPY --from=build /app/build /usr/share/nginx/html

EXPOSE 80 443
CMD ["nginx", "-g", "daemon off;"]