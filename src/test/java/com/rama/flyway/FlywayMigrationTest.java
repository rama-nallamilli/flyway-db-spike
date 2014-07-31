package com.rama.flyway;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertThat;

/**
 * User: rama.nallamilli
 * Date: 31/07/2014
 * Time: 13:16
 */
public class FlywayMigrationTest {
    final String databaseUrl = "jdbc:h2:file:target/foobar";
    final String username = "sa";
    final String password = null;

    @Test
    public void shouldMigrateDatabase() throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(databaseUrl, username, password);
        flyway.migrate();
        assertThat(getFirstNameField(), Matchers.equalTo("RAMA"));
    }

    private String getFirstNameField() throws Exception {
        //Crude JDBC connection test, (use hibernate in production code)
        String sql = "SELECT FIRST_NAME FROM PERSON";
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(databaseUrl, username, password);
            stmt = conn.createStatement();
            ResultSet results = stmt.executeQuery(sql);
            results.last();
            String name = results.getString("FIRST_NAME");
            results.close();
            return name;
        } finally {
            if (conn != null)
                conn.close();

            if (stmt != null)
                conn.close();
        }
    }
}
