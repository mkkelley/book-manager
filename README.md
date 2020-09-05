# Book Manager

This creatively named application is a tool to track the books you own and have read.

This was created to allow me to track my reading habits without paying in information on the
internet. As a result, there are probably many freely available alternatives on the internet.


## Technology

Book Manager is a web application with an Angular frontend and a Spring Boot backend.

## Runtime Requirements

- Java 14
- PostgreSQL database

The database schema will be created automatically with flyway unless you explicitly disable it.

Data source configuration is typical for Spring, put an application.properties file somewhere
on the search path and set the relevant properties. An example can be found under
`src/main/resources`.

The `app.apiBaseUrl` property needs to be accurate. An initial request will be made at `/config`
to get that configuration file.

## Building

- JDK 14
- Maven

`mvn clean package` will get you a runnable fat JAR. Node/npm don't need to be set up on the build
system, the maven frontend plugin will download it for you.
