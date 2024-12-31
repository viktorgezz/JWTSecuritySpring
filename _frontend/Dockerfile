FROM nginx:alpine

COPY html /usr/share/nginx/html
COPY js /usr/share/nginx/html/js
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
