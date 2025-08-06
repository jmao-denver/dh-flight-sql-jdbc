# Deephaven Enterprise Flight Sql Jdbc Driver

This repository contains the DHE Flight SQL JDBC driver, which allows users to connect to Deephaven 
Enterprise Persistent Queries using the Flight SQL protocol.

## Initial Setup

The driver can be considered as a fork of the Apache Arrow Flight SQL JDBC driver (https://github.com/apache/arrow-java/tree/main/flight/flight-sql-jdbc-core), with modifications to support 
Deephaven Enterprise's specific authentication/connection requirements.

The process of setting up the DHE Flight SQL JDBC driver repository involves working with two repositories: the private
DHE Flight SQL JDBC driver repository and the public Apache Arrow Java repository.

The steps are as follows:

1. Clone the `arrow-java` repository.
    ```commandline
    git clone git@github.com:apache/arrow-java.git
    cd arrow-java
    ```
2. Check out a release version, then extract the flight sql jdbc subdirectory into a new branch
    ```commandline
    git checkout v18.3.0
    git subtree split --prefix=flight/flight-sql-jdbc-core -b arrow-flight-sql-jdbc
    ```
3. Push the new branch to the DHE Flight SQL JDBC driver repository
    ```commandline
    git remote add dhe-flight-sql-jdbc git@github.com:deephaven-ent/coreplus-jdbc-driver.git
    git push -u dhe-flight-sql-jdbc arrow-flight-sql-jdbc
    ```
4. In the local clone of the DHE Flight SQL JDBC driver repository (git@github.com:deephaven-ent/coreplus-jdbc-driver.git),
check out the `arrow-flight-sql-jdbc` branch and create the main branch off it.
    ```commandline
    git checkout arrow-flight-sql-jdbc
    git checkout -b main
    ```
5. Update the package name and delete unneeded code.

   - load the project into IntelliJ IDEA, select the `org.apache.arrow.driver.jdbc` package, and refactor-> Move Package ... > To directory, then specify 
the new desitination `io.deephaven.enterprise.flight.sql.jdbc`.
   - delete the `src/test/java/org/apache/arrow/driver/jdbc` directory, as it won't work with the DHE Flight SQL server.
   - git commit and push the changes to the remote main branch. 


6. Make the necessary changes to create a DHE Flight SQL JDBC driver.
   - Make the project a Gradle one
   - Add DHE authentication and connection logic
   - Add any additional dependencies or configurations required for using the DHE Java client
   - Add some tests
   - git commit and push the changes to the remote  main branch.

## Resync with the Apache Arrow Flight SQL JDBC Driver

The ongoing development of the DHE Flight SQL JDBC driver follows the normal DHE development process. When there are
upstream changes in the Apache Arrow Flight SQL JDBC driver that need to be incorporated into the DHE Flight SQL JDBC driver,
the following steps should be followed:

1. In the local clone of the `arrow-java` repository, pull the latest changes from the remote, check out the new release version, e.g.
    ```commandline
    git pull
    git checkout v18.4.0
    git subtree split --prefix=flight/flight-sql-jdbc-core --onto arrow-flight-sql-jdbc -b arrow-flight-sql-jdbc
    ```
    or if arrow-flight-sql-jdbc branch does not already exist
    ```commandline
    git subtree split --prefix=flight/flight-sql-jdbc-core -b arrow-flight-sql-jdbc
    ```
2. Push the new (or updated) branch to the DHE Flight SQL JDBC driver repository
    ```commandline
    git remote add dhe-flight-sql-jdbc git@github.com:deephaven-ent/coreplus-jdbc-driver.git
    git push -u dhe-flight-sql-jdbc arrow-flight-sql-jdbc
    ```
3. In the local clone of the DHE Flight SQL JDBC driver repository, check out the `arrow-flight-sql-jdbc` branch and merge it into the main branch.
    ```commandline
    git checkout arrow-flight-sql-jdbc
    git pull
    git checkout main
    git merge arrow-flight-sql-jdbc
    ```
4. Resolve any merge conflicts, if necessary, and commit the changes.

>**Note:**
The merge will bring back the deleted files from `arrow-flight-sql-jdbc` branch, so you will need to delete them again.



