package View;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

import Model.*;

public class LoginViewController {

    private static ObservableList<Book> booksList;
    private static ObservableList<Discount> discountsPerBookList;
    private static ObservableList<Discount> discountsOnTotalList;
    private static ObservableList<Book> shoppingList;

    public void transferLists(ObservableList<Book> booksList, ObservableList<Discount> discountsPerBookList,
                              ObservableList<Discount> discountsOnTotalList, ObservableList<Book>  shoppingList) {
        LoginViewController.booksList = booksList;
        LoginViewController.discountsPerBookList = discountsPerBookList;
        LoginViewController.discountsOnTotalList = discountsOnTotalList;
        LoginViewController.shoppingList = shoppingList;

    }

    @FXML HBox view;
    @FXML TextField username;
    @FXML PasswordField password;
    @FXML Label msg;

    @FXML
    void home(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/HomeView.fxml"));
        Parent root = loader.load();

        HomeViewController homeViewController = loader.getController();
        homeViewController.transferLists(booksList, discountsPerBookList, discountsOnTotalList, shoppingList);

        Stage primaryStage = (Stage) view.getScene().getWindow();
        primaryStage.setScene(new Scene(root, 1200, 830));
        primaryStage.show();
    }

    @FXML
    void exit(ActionEvent e) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = a.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    void loginBtn(ActionEvent e) throws IOException {
        if(username.getText().equals("Admin") && password.getText().equals("Password")) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/BookkeeperView.fxml"));
            Parent root = loader.load();

            BookkeeperViewController bookkeeperViewController = loader.getController();
            bookkeeperViewController.transferLists(booksList, discountsPerBookList, discountsOnTotalList, shoppingList);

            Stage primaryStage = (Stage) view.getScene().getWindow();
            primaryStage.setScene(new Scene(root, 1200, 830));
            primaryStage.show();
        } else if(username.getText().isEmpty()) {
            msg.setText("Username cannot be left empty.");
        } else if(password.getText().isEmpty()) {
            msg.setText("Password cannot be left empty.");
        } else {
            msg.setText("Incorrect information provided. Please try again.");
        }
    }

}
