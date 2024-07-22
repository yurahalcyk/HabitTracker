package org.habittracker.database;

import java.sql.*;

public class SqliteConnectionPlayground {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:sample.db";

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                System.out.println("A new db has been created");
            }

            String createTableSQL = "CREATE TABLE IF NOT EXISTS users ("
                    + "id integer PRIMARY KEY,"
                    + "name text NOT NULL,"
                    + "email text NOT NULL UNIQUE"
                    + ");";

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                System.out.println("Table 'users' created.");
            }

            String insertSQL = "INSERT INTO users (name, email) VALUES "
                    + "('Alice', 'alice@example.com'),"
                    + "('Bob', 'bob@example.com');";


            try (Statement stmt = conn.createStatement()) {
                stmt.execute(insertSQL);
                System.out.println("Data inserted into 'users' table.");
            }

            String selectSQL = "SELECT * FROM users;";

            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(selectSQL)) {
                while (rs.next()) {
                    System.out.println(rs.getInt("id") + "\t" +
                            rs.getString("name") + "\t" +
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
