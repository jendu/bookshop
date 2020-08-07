package View;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;

import Model.*;


public class CustomerViewController implements Initializable {

    //initialising for TestFX
    public ObservableList<Book> booksList =
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
    public ObservableList<Discount> discountsPerBookList =
            FXCollections.observableArrayList(
                    new Discount(">", 2000, 10)
            );
    public ObservableList<Discount> discountsOnTotalList =
            FXCollections.observableArrayList(
                    new Discount(">", 30, 5)
            );
    public ObservableList<Book> shoppingList = FXCollections.observableArrayList();
    public double costs=0;

    public void transferLists(ObservableList<Book> booksList, ObservableList<Discount> discountsPerBookList,
                              ObservableList<Discount> discountsOnTotalList, ObservableList<Book>  shoppingList) {
        this.booksList = booksList;
        this.discountsPerBookList = discountsPerBookList;
        this.discountsOnTotalList = discountsOnTotalList;
        this.shoppingList = shoppingList;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {

            bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            bookYearCol.setCellValueFactory(new PropertyValueFactory<>("bookYear"));
            bookPriceCol.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            bookPriceCol.setCellFactory(tc -> new TableCell<Book, Double>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(currencyFormat.format(price));
                    }
                }
            });
            availableBooks.getItems().setAll(booksList);

            availableBooks.requestFocus();
            availableBooks.getSelectionModel().select(0);
            availableBooks.getFocusModel().focus(0);
            availableBooks.setPlaceholder(new Label("No books available"));

            cartTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
            cartYearCol.setCellValueFactory(new PropertyValueFactory<>("bookYear"));
            cartPriceCol.setCellValueFactory(new PropertyValueFactory<>("bookPrice"));
            cartPriceCol.setCellFactory(tc -> new TableCell<Book, Double>() {
                @Override
                protected void updateItem(Double price, boolean empty) {
                    super.updateItem(price, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(currencyFormat.format(price));
                    }
                }
            });
            shoppingCart.getItems().setAll(shoppingList);

            shoppingCart.requestFocus();
            shoppingCart.getSelectionModel().select(0);
            shoppingCart.getFocusModel().focus(0);
            shoppingCart.setPlaceholder(new Label("Your shopping cart is empty"));

            StringBuilder discountsText = new StringBuilder();
            if(discountsPerBookList.isEmpty() && discountsOnTotalList.isEmpty()) {
                discountsText = new StringBuilder("There are no discounts available at this time.");
            } else {
                for(Discount d : discountsPerBookList) {
                    discountsText.append(String.format("If book publication year %s %.0f, then discount by %d%%\n",
                            d.getCondition(), d.getConditionValue(), d.getAmount()));
                }
                for(Discount d : discountsOnTotalList) {
                    discountsText.append(String.format("If total price %s %.2f, then discount by %d%%\n",
                            d.getCondition(), d.getConditionValue(), d.getAmount()));
                }
            }

            discounts.setText(discountsText.toString());
            receipt.setText(calculateTotal()+applyDiscounts());
            finalCost.setText(String.format("Final Cost: £%.2f",costs));

        });

    }

    @FXML private HBox view;
    @FXML private Label msg, finalCost;
    @FXML private TextArea discounts, receipt;

    @FXML private TableView<Book> availableBooks;
    @FXML private TableColumn<Book, String> bookTitleCol;
    @FXML private TableColumn<Book, Integer> bookYearCol;
    @FXML private TableColumn<Book, Double> bookPriceCol;

    @FXML private TableView<Book> shoppingCart;
    @FXML private TableColumn<Book, String> cartTitleCol;
    @FXML private TableColumn<Book, Integer> cartYearCol;
    @FXML private TableColumn<Book, Double> cartPriceCol;

    @FXML
    void logout(ActionEvent event) throws IOException {
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
    void addBook(ActionEvent e) {
        try {
            Book selectedBook = availableBooks.getSelectionModel().getSelectedItem();
            shoppingList.add(new Book(selectedBook.getBookTitle(), selectedBook.getBookYear(),
                    selectedBook.getBookPrice()));
            shoppingCart.getItems().add(selectedBook);
            receipt.setText(calculateTotal()+applyDiscounts());
            finalCost.setText(String.format("Final Cost: £%.2f", costs));
            msg.setText("Added to cart successfully.");
        } catch(Exception ex) {
            msg.setText("There are no books to add!");
        }
    }

    @FXML
    void removeBook(ActionEvent e) {
        int selectedBook = shoppingCart.getSelectionModel().getSelectedIndex();
        try {
            shoppingList.remove(selectedBook);
            shoppingCart.getItems().remove(selectedBook);
            receipt.setText(calculateTotal()+applyDiscounts());
            finalCost.setText(String.format("Final Cost: £%.2f",costs));
            msg.setText("Removed from cart successfully.");
        } catch(IndexOutOfBoundsException ex) {
            msg.setText("Please select a book to remove.");
        }
    }

    private String calculateTotal() {
        String total = "Total price prior to discounts: ";
        double costs = 0;
        for(Book b: shoppingList) {
            costs += b.getBookPrice();
        }
        total+=String.format("\u00A3%.2f\n", costs);
        return total;
    }

    private String applyDiscounts() {
        StringBuilder discountedPrice = new StringBuilder("\nDiscounts applied:\n");
        double costs = 0;
        boolean discountApplied;
        for(Book b: shoppingList) {
            discountApplied = false;
            for(Discount d: discountsPerBookList) {
                switch(d.getCondition()){
                    case ">":
                        if(b.getBookYear() > d.getConditionValue()) {
                            costs += b.getBookPrice() * (100 - d.getAmount())/100.0;
                            discountApplied = true;
                        }
                        break;
                    case ">=":
                        if(b.getBookYear() >= d.getConditionValue()) {
                            costs += b.getBookPrice() * (100 - d.getAmount())/100.0;
                            discountApplied = true;
                        }
                        break;
                    case "<":
                        if(b.getBookYear() < d.getConditionValue()) {
                            costs += b.getBookPrice() * (100 - d.getAmount())/100.0;
                            discountApplied = true;
                        }
                        break;
                    case "<=":
                        if(b.getBookYear() <= d.getConditionValue()) {
                            costs += b.getBookPrice() * (100 - d.getAmount())/100.0;
                            discountApplied = true;
                        }
                        break;
                    case "==":
                        if(b.getBookYear() == d.getConditionValue()) {
                            costs += b.getBookPrice() * (100 - d.getAmount())/100.0;
                            discountApplied = true;
                        }
                        break;
                    default:
                        break;
                }

                if(discountApplied) {
                    discountedPrice.append(String.format("Discount applied of %d%% off on the book \"%s\"\n",
                            d.getAmount(), b.getBookTitle()));
                }
            }
            if(!discountApplied) {
                costs += b.getBookPrice();
            }

        }

        discountApplied = false;
        for(Discount d: discountsOnTotalList) {
            switch(d.getCondition()){
                case ">":
                    if(costs > d.getConditionValue()) {
                        costs *= (100 - d.getAmount())/100.0;
                        discountApplied = true;
                    }
                    break;
                case ">=":
                    if(costs >= d.getConditionValue()) {
                        costs *= (100 - d.getAmount())/100.0;
                        discountApplied = true;
                    }
                    break;
                case "<":
                    if(costs < d.getConditionValue()) {
                        costs *= (100 - d.getAmount())/100.0;
                        discountApplied = true;
                    }
                    break;
                case "<=":
                    if(costs <= d.getConditionValue()) {
                        costs *= (100 - d.getAmount())/100.0;
                        discountApplied = true;
                    }
                    break;
                case "==":
                    if(costs == d.getConditionValue()) {
                        costs *= (100 - d.getAmount())/100.0;
                        discountApplied = true;
                    }
                    break;
                default:
                    break;
            }
            if(discountApplied) {
                discountedPrice.append(String.format("Discount applied of %d%% off on the total\n", d.getAmount()));
            }
        }

        if(discountedPrice.toString().equals("\nDiscounts applied:\n")) {
            discountedPrice = new StringBuilder("\nDiscounts applied: none");
        }

        setCosts(costs);
        discountedPrice.append(String.format("\nTotal price after applying discounts: \u00A3%.2f", costs));

        return discountedPrice.toString();
    }

    private void setCosts(double costs) {
        this.costs=costs;
    }
}
