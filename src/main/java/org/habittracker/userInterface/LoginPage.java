package org.habittracker.userInterface;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.habittracker.dao.UserDAO;
import org.habittracker.dto.UserDTO;

public class LoginPage extends Application {
    private UserDAO userDAO;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        userDAO = new UserDAO();

        BorderPane root = new BorderPane();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 10, 0, 10));
        grid.setMinSize(400, 100);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // labels and inputs
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        GridPane.setConstraints(usernameLabel, 0, 0);

        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        passwordLabel.setPrefWidth(73);
        passwordLabel.setAlignment(Pos.BASELINE_RIGHT);
        GridPane.setConstraints(passwordLabel, 0, 1);

        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 1);

        grid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput);
        root.setCenter(grid);

        // buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setPadding(new Insets(10, 10, 20, 10));
        buttonBox.setAlignment(Pos.CENTER);

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(71);
        loginButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        Button registerButton = new Button("Register");
        registerButton.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        buttonBox.getChildren().addAll(loginButton, registerButton);
        root.setBottom(buttonBox);

        // button logic
        loginButton.setOnAction(e -> {handleLoginButtonAction(usernameInput.getText(), passwordInput.getText());});
        registerButton.setOnAction(e -> {handleRegisterButtonAction(usernameInput.getText(), passwordInput.getText());});

        Scene scene=new Scene(root);
        primaryStage.setTitle("Login page");
        primaryStage.setScene(scene);
        primaryStage.show();

//        Platform.runLater(() -> {
//            double labelWidth = registerButton.getWidth();
//            System.out.println("Label Width: " + labelWidth);
//        });
    }

    public void errorAlert(String title, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void informationAlert(String title, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public void handleLoginButtonAction(String username, String password){
        boolean doesUsernameExist = userDAO.isUsernameTaken(username);
        if (!doesUsernameExist) {
            errorAlert("Attempted Login", "Username does not exist. Please register.");
        } else {
            UserDTO user = userDAO.loginUser(username, password);
            if (user != null) {
                informationAlert("Login", "Login Successful");
                // proceed to habit tracker main application
            } else {
                errorAlert("Attempted Login", "Invalid username or password");
            }
        }
    }

    public void handleRegisterButtonAction(String username, String password){
        boolean isUsernameTaken = userDAO.isUsernameTaken(username);
        if (!isUsernameTaken) {
            boolean user = userDAO.registerUser(username, password);
            if (user) {
                informationAlert("Registration", "Registration Successful");
                // proceed to habit tracker main application
            } else {
                errorAlert("Registration", "Invalid username or password");
            }
        } else {
            errorAlert("Registration", "Username taken. Try another username.");
        }
    }
}
