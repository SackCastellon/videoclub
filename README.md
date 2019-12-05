# Videoclub

## Developing
### Requirements
To develop this application you will need Java 8, the recommended version is Java 8 update 222.

## Docker installation
### Setup
Before running the containers you need to setup the required environment variables:
- `JWT_SECRET` - A secret used to sign JSON Web Tokens.
- `POSTGRES_DB` - The name of the database to be used by the server.
- `POSTGRES_USER` - The user used by the server to connect to the database.
- `POSTGRES_PASSWORD` - The password of the database user.

Additionally, you will need to run this command to build the backend.
```shell script
gradlew shadowJar
```
You will have to rerun this command if you make changes in the backend.

### Running
Run the following command in this directory.
```shell script
docker-compose up
```

### Reload
If you make any code changes you will need rerun the previous command with the `--build` option, for the changes to take
effect.
```shell script
docker-compose up --build
```

### Access
To access the application simply open the following url in a browser.
```
http://localhost:9080/
```
