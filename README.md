# Videoclub

## With docker

The docker build is the most similar to a production build.

### Requirements
- JDK 8u222 (or newer jdk8 update)
- Docker

### Setup and run
Create the following environment variables.
- `JWT_SECRET` - A random secret used to sign JSON Web Tokens. **Must be econded in Base64**
- `POSTGRES_DB` - The name of the database. **Must be a valid ASCII string**
- `POSTGRES_USER` - The user used by the server to connect to the database. **Must be a valid ASCII string**
- `POSTGRES_PASSWORD` - The password of the database user. **Must be a valid ASCII string**

In the root folder run the commands:
```shell script
./gradlew :videoclub-backend:shadowJar

docker-compose up
```

### Access
To access the web interface open the folling url [`http://localhost:9080`](http://localhost:9080)


## Without docker

This is just runs the aplication in developement mode, so you may experience worse performance than if using docker.

### Requirements
- JDK 8u222 (or newer jdk8 update)
- npm 6.13.1 (or newer)
- PostgreSQL 12 Server

### Setup and run
Create a PostgreSQL server.

Create the following environment variables.
- `JWT_SECRET` - A random secret used to sign JSON Web Tokens. **Must be econded in Base64**
- `POSTGRES_DB` - The name of the database you just created.
- `POSTGRES_USER` - The username of the database you just created.
- `POSTGRES_PASSWORD` - The password of the database you just created.

Run de following command in the root of the project.
```shell script
./gradlew :videoclub-backend:run
```

**In a new terminal**, run de following commands in the `videoclub-frontend` folder.
```shell script
npm install
npm run serve
```

### Access
To access the web interface open the folling url [`http://localhost:9080`](http://localhost:9080)
