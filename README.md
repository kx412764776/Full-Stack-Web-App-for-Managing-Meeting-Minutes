# 2766657c-development-project
The way to start this web application

---
### You need to install docker before running the project

1. Build database in docker

Navigate to the root directory of the project and run:

```bash
docker compose up -d
```

2. Go to the ./backend directory in project

run: 

```bash
mvn -U clean compile
```

then start Springboot application in IDE or run:

```bash
mvn spring-boot:run
```

3. Go to ./frontend/apprenticeship-react directory in project

run:

```bash
npm install
```

then:

```
npm run dev
```

4. Open browser enter the URL: localhost:5173/apprenticeship

