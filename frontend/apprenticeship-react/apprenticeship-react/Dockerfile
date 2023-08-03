FROM node:19-alpine
ARG api_base_url
WORKDIR /apprenticeship
COPY package*.json .
RUN npm i --silent
COPY frontend/frontend-apprenticeship .
RUN echo "VITE_API_BASE_URL=${api_base_url}" > .env
EXPOSE 5173
CMD ["npm", "run", "dev"]