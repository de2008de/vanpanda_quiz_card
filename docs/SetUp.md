# Vanpanda Quiz Card Set Up Instructions

This is an instruction manual for setting up Vanpanda Quiz Card App.

## Prerequisite

Install the following:

- Java 8

- Docker

- React

## Start Server

All instructions should be done under the server code unless otherwise specified.

Run this under root folder to create compiled codes under target folder

```
./mvnw clean package -DskipTests
```

Now we can start building VM on docker

```
docker-compose up --build -d
```

Now all VMs are running.

If you want to deploy the server code on cloud server, please read this [document](./DeployServerOnCloud.md).

## Start UI

All instructions should be done under the UI code unless otherwise specified.

To ensure all dependencies are up-to-date, please run:

```
npm install
```

To run the UI code locally, just run this command under root folder:

```
npm start
```

If you want to deploy UI code on cloud server, the instructions are the same as deploying a React app.
