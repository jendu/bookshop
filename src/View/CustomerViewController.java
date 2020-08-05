package View;

import javafx.application.Platform;
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

    private Alert a;
    private static ObservableList<Book> booksList;
    private static ObservableList<Discount> discountsPerBookList;
    private static ObservableList<Discount> discountsOnTotalList;
    private static ObservableList<Book> shoppingList;

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

            bookTitleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("bookTitle"));
            bookYearCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("bookYear"));
            bookPriceCol.setCellValueFactory(new PropertyValueFactory<Book, Double>("bookPrice"));
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

            cartTitleCol.setCellValueFactory(new PropertyValueFactory<Book, String>("bookTitle"));
            cartYearCol.setCellValueFactory(new PropertyValueFactory<Book, Integer>("bookYear"));
            cartPriceCol.setCellValueFactory(new PropertyValueFactory<Book, Double>("bookPrice"));
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

            String discountsText = "";
            if(discountsPerBookList.isEmpty() && discountsOnTotalList.isEmpty()) {
                discountsText="There are no discounts available at this time.";
            } else {
                for(Discount d : discountsPerBookList) {
                    discountsText += String.format("If book publication year %s %.0f, then discount by %d%%\n",
                                                    d.getCondition(), d.getConditionValue(), d.getAmount());
                }
                for(Discount d : discountsOnTotalList) {
                    discountsText += String.format("If total price %s %.2f, then discount by %d%%\n",
                                                    d.getCondition(), d.getConditionValue(), d.getAmount());
                }
            }

            discounts.setText(discountsText);
            receipt.setText(calculateTotal()+applyDiscounts());

        });

    }

    @FXML private HBox view;
    @FXML private Label msg;
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

    @FXML
    void addBook(ActionEvent e) {
        try {
            Book selectedBook = availableBooks.getSelectionModel().getSelectedItem();
            shoppingList.add(new Book(selectedBook.getBookTitle(), selectedBook.getBookYear(),
                    selectedBook.getBookPrice()));
            shoppingCart.getItems().add(selectedBook);
            receipt.setText(calculateTotal()+applyDiscounts());
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
        return total+= String.format("\u00A3%.2f\n", costs);
    }

    private String applyDiscounts() {
        String discountedPrice = "\nDiscounts applied:\n";
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
                    discountedPrice += String.format("Discount applied of %d%% off on the book \"%s\"\n",
                                                      d.getAmount(), b.getBookTitle());
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
                discountedPrice+= String.format("Discount applied of %d%% off on the total\n", d.getAmount());
            }
        }

        if(discountedPrice.equals("\nDiscounts applied:\n")) {
            discountedPrice = "\nDiscounts applied: none";
        }
        discountedPrice+= String.format("\nTotal price after applying discounts: \u00A3%.2f", costs);

        return discountedPrice;
    }
}
