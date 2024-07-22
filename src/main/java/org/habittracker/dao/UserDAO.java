package org.habittracker.dao;

import org.habittracker.utility.PropertiesLoader;
import org.habittracker.database.DatabaseSetup;
import org.habittracker.dto.UserDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private PropertiesLoader databasePropertiesLoader;
    DatabaseSetup db = new DatabaseSetup();
    UserDTO userDTO = new UserDTO();

    public UserDAO () {
        databasePropertiesLoader = new PropertiesLoader("database.properties");
    }

    public boolean registerUser(UserDTO user) {
        String registerUserSql = databasePropertiesLoader.getProperty("registerUser");

        if (isUsernameTaken(user.getUsername())) {
            System.out.println("Username is already taken.");
            return false;
        }

        try (Connection conn = db.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(registerUserSql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.executeUpdate();
            System.out.println("User created successfully.");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public UserDTO loginUser(String username, String password) {
        String loginUserSql = databasePropertiesLoader.getProperty("loginUser");
        try (Connection conn = db.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(loginUserSql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userDTO.setUsername(rs.getString("username"));
                userDTO.setPassword(rs.getString("password"));
                System.out.println("User found. Logging in....");
                return userDTO;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean isUsernameTaken(String username) {
        String usernameCheckSql = databasePropertiesLoader.getProperty("usernameCheck");
        try (Connection conn = db.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(usernameCheckSql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
