package View;

import javafx.collections.FXCollections;
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

    private ObservableList<Book> booksList =
        FXCollections.observableArrayList(
                new Book("Moby Dick", 1851, 15.20),
                new Book("The Terrible Privacy of Maxwell Sim", 2010, 13.14),
                new Book("Still Life With Woodpecker", 1980, 11.05),
                new Book("Sleeping Murder", 1976, 10.24),
                new Book("Three Men in a Boat", 1889, 12.87),
                new Book("The Time Machine", 1895, 10.43),
                new Book("The Caves of Steel", 1954, 8.12),
                new Book("Idle Thoughts of an Idle Fellow", 1886, 7.32),
                new Book("A Christmas Carol", 1843, 4.23),
                new Book("A Tale of Two Cities", 1859, 6.32),
                new Book("Great Expectations", 1861, 13.21)
        );
    private ObservableList<Discount> discountsPerBookList =
            FXCollections.observableArrayList(
                    new Discount(">", 2000, 10)
            );
    private ObservableList<Discount> discountsOnTotalList =
            FXCollections.observableArrayList(
                    new Discount(">", 30, 5)
            );
    private ObservableList<Book> shoppingList = FXCollections.observableArrayList();

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

        Stage primaryStage = (Stage) view.getScene().getWindow();
        primaryStage.setScene(new Scene(root, 1200, 830));
        primaryStage.show();
    }

    @FXML
    void viewAsBookkeeper(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/LoginView.fxml"));
        Parent root = loader.load();

        LoginViewController loginViewController = loader.getController();
        loginViewController.transferLists(booksList, discountsPerBookList, discountsOnTotalList, shoppingList);

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
}
