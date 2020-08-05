package View;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.*;

public class HomeViewController implements Initializable {

    private Alert a;
    private ObservableList<Book> booksList;
    private ObservableList<Discount> discountsPerBookList;
    private ObservableList<Discount> discountsOnTotalList;
    private ObservableList<Book> shoppingList;

    public void transferLists(ObservableList<Book> booksList, ObservableList<Discount> discountsPerBookList,
                              ObservableList<Discount> discountsOnTotalList, ObservableList<Book>  shoppingList) {
        this.booksList = booksList;
        this.discountsPerBookList = discountsPerBookList;
        this.discountsOnTotalList = discountsOnTotalList;
        this.shoppingList = shoppingList;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML private HBox view;

    @FXML
    void viewAsCustomer(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/CustomerView.fxml"));
        Parent root = loader.load();

        CustomerViewController customerViewController = loader.getController();
        customerViewController.transferLists(booksList, discountsPerBookList, discountsOnTotalList, shoppingList);

        Scene customerScene = new Scene(root, 1200, 830);

        Stage primaryStage = (Stage) view.getScene().getWindow();
        primaryStage.setScene(customerScene);
        primaryStage.show();
    }

    @FXML
    void viewAsBookkeeper(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginView.fxml"));
        Parent root = loader.load();

        LoginViewController loginViewController = loader.getController();
        loginViewController.transferLists(booksList, discountsPerBookList, discountsOnTotalList, shoppingList);

        Scene customerScene = new Scene(root, 1200, 830);

        Stage primaryStage = (Stage) view.getScene().getWindow();
        primaryStage.setScene(customerScene);
        primaryStage.show();
    }

    @FXML
    void exit(ActionEvent e) {
        a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = a.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) {
            System.exit(0);
        }
    }
}
