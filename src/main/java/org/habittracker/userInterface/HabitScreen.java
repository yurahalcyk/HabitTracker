package org.habittracker.userInterface;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import lombok.Getter;
import org.habittracker.dao.HabitsDAO;
import org.habittracker.dto.HabitsDTO;
import org.habittracker.dto.UserDTO;

import java.util.List;

public class HabitScreen {
    public HabitsDAO habitsDAO;
    public UserDTO userDTO;
    @Getter
    private Scene scene;
    private TextField habitNameTF;
    private ObservableList<HabitsDTO> data;
    private TableView<HabitsDTO> table;

    public HabitScreen(UserDTO userDTO, HabitsDAO habitsDAO) {
        this.userDTO = userDTO;
        this.habitsDAO = habitsDAO;
        createHabitsScreen();
    }

    public void createHabitsScreen() {

        BorderPane root = new BorderPane();
        root.autosize();

        HBox navbar = new HBox(20);
        navbar.setPadding(new Insets(0, 10, 10, 10));
        navbar.setAlignment(Pos.CENTER);

        HBox habitsHbox = new HBox(20);
        habitsHbox.setPadding(new Insets(10, 10, 10, 10));
        habitsHbox.setAlignment(Pos.CENTER);

        List<HabitsDTO> habits = null;

        // populating habits with db data
        try {
            habits = habitsDAO.getHabitsByUser(userDTO.getId());
        } catch (Exception e) {
            System.out.println("Error with fetching user habits: " + e.getMessage());
        }

        table = new TableView<>();
        table.setPlaceholder(new Label("No habits to track as of yet"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        // populating observable list with current db data
        data = FXCollections.observableArrayList(habits);

        Label label = new Label(userDTO.getUsername() + "'s Habits");
        label.setFont(new Font("Arial",18));

        // habit name column
        TableColumn<HabitsDTO, String> habitNameColumn = new TableColumn<>();
        habitNameColumn.setCellValueFactory(new PropertyValueFactory<>("habitName"));
        // setting header style of column
        habitNameColumn.setGraphic(createHeaderWithCustomFont("Habit", "Arial", 18));
        habitNameColumn.setMinWidth(250);
        // setting font style of cell
        habitNameColumn.setCellFactory(param-> new TableCell<HabitsDTO, String>() {
            @Override
            protected void updateItem(String habit, boolean empty) {
                super.updateItem(habit, empty);
                if (empty) {
                    setText(null);
                    setStyle("");
                    setPrefHeight(38);
                } else {
                    setText(habit);
                    setFont(Font.font("Arial", 16));
                    setPrefHeight(38);
                }
            }
        });

        // delete habit column
        TableColumn<HabitsDTO, HabitsDTO> deleteHabitColumn = new TableColumn<>();
        deleteHabitColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        // setting header style of column
        deleteHabitColumn.setGraphic(createHeaderWithCustomFont("Delete Habit", "Arial", 18));
        deleteHabitColumn.setMinWidth(120);
        // setting functionality of delete button in the cell
        deleteHabitColumn.setCellFactory(param-> new TableCell<HabitsDTO, HabitsDTO>() {
            final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(HabitsDTO habit, boolean empty) {
                super.updateItem(habit, empty);
                if (habit == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(deleteButton);
                setAlignment(Pos.CENTER);
                deleteButton.setOnAction(e->{
                    try {
                        System.out.println("deleting habit id: " + habit.getId() + " habit name: " + habit.getHabitName());
                        habitsDAO.deleteHabit(habit.getId());
                    } catch (Exception err) {
                        System.out.println("Error with deleting habit: " + err.getMessage());
                    }
                    data.remove(habit);
                    table.getSelectionModel().clearSelection();
                });
            }
        });

        // populating the table with data and columns
        table.setItems(data);
        table.getColumns().addAll(habitNameColumn, deleteHabitColumn);

        // placing the label and table in a vbox for layout
        VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, table);
        VBox.setVgrow(table, Priority.ALWAYS);
        vbox.setPrefSize(400, 300);

        // hbox component + button functionality
        habitNameTF = new TextField();
        Button addHabit = new Button("Add habit");
        Button getHabits = new Button("Get habits");

        // navbar component
        Button toHome = new Button("Home");

        addHabit.setOnAction(e -> {handleAddHabitButtonAction(habitNameTF.getText());});
        getHabits.setOnAction(e -> {handleGetHabitsButtonAction(userDTO.getId());});

        habitsHbox.getChildren().addAll(habitNameTF, addHabit, getHabits);
        navbar.getChildren().addAll(toHome);
        root.setTop(habitsHbox);
        root.setCenter(vbox);
        root.setBottom(navbar);
        scene = new Scene(root);
    }

    public void handleAddHabitButtonAction(String habitName){
        try {
            int habitId = habitsDAO.addHabit(userDTO.getId(), habitName);
            HabitsDTO newHabit = new HabitsDTO(habitId, userDTO.getId(), habitName);
            data.add(newHabit);
            clearFields();
        } catch (Exception e) {
            System.out.println("handleAddHabitButton failed: " + e.getMessage());
        }
    }

    public void handleGetHabitsButtonAction(int userId){
        try {
            List<HabitsDTO> habits = habitsDAO.getHabitsByUser(userId);
            System.out.println(habits);
        } catch (Exception e) {
            System.out.println("handleGetHabitsButtonAction: " + e.getMessage());
        }
    }

    public javafx.scene.control.Label createHeaderWithCustomFont(String text, String fontFamily, int size) {
        javafx.scene.control.Label header = new javafx.scene.control.Label(text);
        header.setFont(Font.font(fontFamily, FontWeight.BOLD, size));
        return header;
    }

    public void clearFields() {
        habitNameTF.clear();
        habitNameTF.requestFocus();
    }
}
