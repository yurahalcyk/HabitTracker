package org.habittracker;

import org.habittracker.database.DatabaseSetup;
import org.habittracker.userInterface.LoginPage;

public class Main {
    public static void main(String[] args) {
        DatabaseSetup db = new DatabaseSetup();
        db.createDatabaseAndTables();
        LoginPage.main(args);
    }
}