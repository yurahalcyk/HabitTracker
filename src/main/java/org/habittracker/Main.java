package org.habittracker;

import org.habittracker.dao.UserDAO;
import org.habittracker.database.DatabaseSetup;
import org.habittracker.dto.UserDTO;
import org.habittracker.userInterface.LoginPage;

public class Main {
    public static void main(String[] args) {
        DatabaseSetup db = new DatabaseSetup();
//        UserDAO userDAO = new UserDAO();
//        UserDTO user1 = new UserDTO();
//        user1.setUsername("yura123");
//        user1.setPassword("p1234");
        db.createDatabaseAndTables();
        LoginPage.main(args);
    }
}