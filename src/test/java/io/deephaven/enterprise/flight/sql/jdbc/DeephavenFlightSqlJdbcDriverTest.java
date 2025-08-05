package io.deephaven.enterprise.flight.sql.jdbc;

import org.junit.jupiter.api.*;
import java.sql.*;

class DeephavenFlightSqlJdbcDriverTest {
    private static Connection connection;

    @BeforeAll
    static void connect() throws SQLException {
//        String jdbcUrl = "jdbc:arrow-flight-sql://localhost:10000?Authorization=Anonymous&useEncryption=false&x-deephaven-auth-cookie-request=true";
//        String jdbcUrl = "jdbc:deephaven-flight-sql://localhost:10000?Authorization=Anonymous&useEncryption=false&x-deephaven-auth-cookie-request=true";
        String jdbcUrl = "jdbc:deephaven-flight-sql://jianfengmao-bhs-sanluis.int.illumon.com:8123?pqname=FlightSqlJdbcPQ&user=iris&password=iris";

        try {
            final Driver driver = DriverManager.getDriver(jdbcUrl);
            System.out.println("Using driver: " + driver.getClass().getName());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get driver for URL: " + jdbcUrl, e);
        }

        connection = DriverManager.getConnection(jdbcUrl);
    }

    @AfterAll
    static void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testSelectNoData() throws SQLException {
        System.out.println("Connection established successfully: " + connection);
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT 42 As Foo")) {
            System.out.println("Query executed successfully: SELECT 42 As Foo");
            System.out.println("ResultSet: " + resultSet);
            while (resultSet.next()) {
                System.out.println(resultSet.getLong("Foo"));
            }
        }
    }

    @Test
    void testSelectAll() {
        try (
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM source")) {
            System.out.println("Query executed successfully: SELECT * FROM source");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("id: " + id + ", name: " + name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testStatementWhere() throws SQLException {
        final String query = "SELECT * FROM source WHERE id > 50";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(query)) {
            System.out.println("Query executed successfully: " + query);
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("id: " + id + ", name: " + name);
            }
            Assertions.assertEquals(49, rowCount);
        }
    }

    @Test
    void testPreparedStatementWhere() throws SQLException {
        final String query = "SELECT * FROM source WHERE id > 50";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet resultSet = stmt.executeQuery()) {
            System.out.println("Query executed successfully: " + query);
            int rowCount = 0;
            while (resultSet.next()) {
                rowCount++;
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("id: " + id + ", name: " + name);
            }
            Assertions.assertEquals(49, rowCount);
        }
    }

    @Test
    @Disabled("This test is skipped because it seems that DH Flight Sql server doesn't support parameterized query yet(?).")
    void testPreparedStatement() throws SQLException {
        final String query = "SELECT * FROM source WHERE id > ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {

            // Bind the parameter (e.g. ID = 42)
            stmt.setInt(1, 42);

            try (ResultSet resultSet = stmt.executeQuery()) {
                System.out.println("Query executed successfully: " + query);
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    System.out.println("id: " + id + ", name: " + name);
                }
            }
        }
    }
}
