package org.habittracker;

import javafx.application.Application;
import javafx.stage.Stage;
import org.habittracker.database.DatabaseSetup;
import org.habittracker.userInterface.HabitScreen;
import org.habittracker.userInterface.LoginScreen;

public class Main extends Application {

    public Stage primaryStage;
    DatabaseSetup db = new DatabaseSetup();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        db.createDatabaseAndTables();
        showLoginScreen();
        primaryStage.setTitle("Habit Tracker");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void showLoginScreen() {
        LoginScreen loginScreen = new LoginScreen(this);
        primaryStage.setScene(loginScreen.getScene());
    }

    public void showHabitScreen() {
        HabitScreen habitScreen = new HabitScreen();
        primaryStage.setScene(habitScreen.getScene());
    }
}