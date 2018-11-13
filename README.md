**money-transfer-api**

Test task for Revolut.

Quick start: build a jar with _mvn install_ then launch the application:

_java -jar money-transfer-api-1.0-SNAPSHOT.jar server ../config.yml_
(if you are in 'target' folder)

You can use Postman, Insomnia or any other tool of your choice to probe the API.

_GET_  **localhost:8080/account/list**

_POST_  **localhost:8080/transaction/transfer** _{"fromAccNumber":"1","toAccNumber":"2","amount":"100"}_

_GET_  **localhost:8080/transaction/list**

Liquibase script adds 5 different accounts on boot, if it's not enough - then use 
_POST_  **localhost:8080/account/create** _{"balance" : "100","currency" : "EUR"}_

A proper end-to-end test is included (TransferTest.java)

To run the app within Intellij IDEA, add a new "Application" configuration, specify the main class "**com.revolut.transfer.TransferApplication**" and add "**server config.yml**" to program arguments.

PS
I've had a look at various frameworks after I've been assigned this task. "No Spring" requirement sure made this interesting. I've chosen Dropwizard as it bundles Jersey for REST, Jackson for JSON and Jetty as an embedded server - all of these are familiar to me. I've chosen Hibernate as a JPA implementation, which also comes as a Dropwizard module. Initial table creation is done with a Liquibase script. In-memory H2 database made this tricky as the standard Dropwizard app usually runs twice, once with "db migrate" command to set up the DB structure, and then with "server" to start the app itself. This was the only non-obvious part of the application development.

PPS The application was built with Java 8 in mind, if you're running tests with Java 9 or higher, you'll have to specify a VM option: _--add-modules java.xml.bind_  
