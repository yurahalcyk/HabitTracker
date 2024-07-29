package org.habittracker.database;

import org.habittracker.utility.PropertiesLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup {
    private static PropertiesLoader propertiesLoader;

    /* static block initialises the properties loader with the prop file. This ensures the properties are loaded when the class
    is first loaded.*/
    static {
        propertiesLoader = new PropertiesLoader("database.properties");
    }

    public void createDatabaseAndTables(){
        String createUserTableSql = propertiesLoader.getProperty("userDatabaseSchema");
        String createHabitTableSql = propertiesLoader.getProperty("habitsDatabaseSchema");
        try (Connection conn = this.connect()){
            if (conn != null) {
                createTables(conn, createUserTableSql);
                createTables(conn, createHabitTableSql);
            }
        } catch (Exception e) {
            System.out.println("createDatabaseAndTables() failed: " + e.getMessage());
        }
    }

    private static void createTables(Connection conn, String sql){
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tables created successfully or already exist.");
        } catch (SQLException e) {
            System.out.println("createTables() failed: " + e.getMessage());
        }
    }

    public Connection connect() {
        String url = propertiesLoader.getProperty("databaseName");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println("Connection to database failed: " + e.getMessage());
        }
        return conn;
    }

}
