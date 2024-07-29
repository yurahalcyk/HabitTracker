package org.habittracker.userInterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.Getter;
import org.habittracker.dao.HabitsDAO;

public class HabitScreen {
    private HabitsDAO habitsDAO;
    @Getter
    private Scene scene;

    public HabitScreen() {
        createHabitsScreen();
    }

    public void createHabitsScreen() {
        HBox habitsHbox = new HBox(20);
        TextField habitname = new TextField();
        Button addHabit = new Button("add habit");
        Button getHabits = new Button("get habits");
        habitsHbox.getChildren().addAll(habitname, addHabit, getHabits);
        scene = new Scene(habitsHbox, 400, 400);
    }

    public void handleAddHabitButtonAction(){

    }
}
