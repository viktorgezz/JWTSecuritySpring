FROM nginx:alpine

# Копируем файлы HTML и JS в директорию Nginx
COPY html /usr/share/nginx/html
COPY js /usr/share/nginx/js

# Копируем конфигурационный файл Nginx
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 4200

# Запуск Nginx
CMD ["nginx", "-g", "daemon off;"]