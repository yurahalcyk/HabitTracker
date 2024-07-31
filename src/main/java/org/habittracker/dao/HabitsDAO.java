package org.habittracker.dao;

import org.habittracker.database.DatabaseSetup;
import org.habittracker.dto.HabitsDTO;
import org.habittracker.dto.UserDTO;
import org.habittracker.utility.PropertiesLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HabitsDAO {
    private PropertiesLoader databasePropertiesLoader;
    DatabaseSetup db = new DatabaseSetup();

    public HabitsDAO() {
        databasePropertiesLoader = new PropertiesLoader("database.properties");
    }

    public int addHabit(int userId, String habitName) {
        int habitId = 0;
        String addHabitSql = databasePropertiesLoader.getProperty("addHabit");
        try (Connection conn = db.connect()){
            PreparedStatement pstmt = conn.prepareStatement(addHabitSql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, habitName);
            pstmt.executeUpdate();

            ResultSet generatedKey = pstmt.getGeneratedKeys();
            if (generatedKey.next()) {
                habitId = generatedKey.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("addHabit failed: " + e.getMessage());
        }
        return habitId;
    }

    public List<HabitsDTO> getHabitsByUser(int userId) {
        String getHabitsSql = databasePropertiesLoader.getProperty("getHabits");
        List<HabitsDTO> habits = new ArrayList<>();

        try (Connection conn = db.connect()){
            PreparedStatement pstmt = conn.prepareStatement(getHabitsSql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()){
                habits.add(new HabitsDTO(rs.getInt("id"), rs.getInt("user_id"), rs.getString("habit_name")));
            }
        } catch (SQLException e) {
            System.out.println("getHabitsByUser failed: " + e.getMessage());
        }
        return habits;
    }

    public void updateHabit(int habitId, String habitName) {
        String updateHabitSql = databasePropertiesLoader.getProperty("updateHabit");
        try (Connection conn = db.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(updateHabitSql);
            pstmt.setString(1, habitName);
            pstmt.setInt(2, habitId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("updateHabit failed: " + e.getMessage());
        }
    }

    public void deleteHabit(int habitId) {
        String deleteHabitSql = databasePropertiesLoader.getProperty("deleteHabit");
        try(Connection conn = db.connect()) {
            PreparedStatement pstmt = conn.prepareStatement(deleteHabitSql);
            pstmt.setInt(1, habitId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("deleteHabit failed: " + e.getMessage());
        }
    }
}
