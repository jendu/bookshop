package Model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Book {

    private final SimpleStringProperty bookTitle;
    private final SimpleIntegerProperty bookYear;
    private final SimpleDoubleProperty bookPrice;

    public Book(String bookTitle, int bookYear, double bookPrice) {
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.bookYear = new SimpleIntegerProperty(bookYear);
        this.bookPrice = new SimpleDoubleProperty(bookPrice);
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle.set(bookTitle);
    }

    public int getBookYear() {
        return bookYear.get();
    }

    public void setBookYear(int bookYear) {
        this.bookYear.set(bookYear);
    }

    public double getBookPrice() {
        return bookPrice.get();
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice.set(bookPrice);
    }

}
