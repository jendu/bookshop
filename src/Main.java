import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Model.*;
import View.HomeViewController;

public class Main extends Application {

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

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/HomeView.fxml"));
        Parent root = loader.load();

        HomeViewController homeViewController = loader.getController();
        homeViewController.transferLists(booksList, discountsPerBookList, discountsOnTotalList, shoppingList);

        primaryStage.setScene(new Scene(root, 1200, 830));

        primaryStage.setTitle("Bookshop");
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
