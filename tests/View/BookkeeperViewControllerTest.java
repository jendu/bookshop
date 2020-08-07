package View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.loadui.testfx.GuiTest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;
import org.testfx.matcher.base.NodeMatchers;
import javafx.scene.input.KeyCode;
import Model.Book;
import Model.Discount;

public class BookkeeperViewControllerTest extends GuiTest {

    private static BookkeeperViewController bookkeeperViewController;

    @Override
    protected Parent getRootNode() {
        FXMLLoader loader = new FXMLLoader(BookkeeperViewController.class.getResource("/View/BookkeeperView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bookkeeperViewController = loader.getController();
        return root;
    }

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testBookkeeperViewIsRetrieved() {
        verifyThat(find("#header"), hasText("\uD83D\uDCDA Bookshop"));

        verifyThat(find("#leftText"), hasText("Options:"));
        verifyThat(find("#logoutBtn"), hasText("Logout"));
        verifyThat(find("#exitBtn"), hasText("Exit"));

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Available Books"));
        verifyThat(find("#mainContent"),
                hasText("Below is the list of books available."));
    }

    @Test
    public void testLogoutBtnReturnsHomeView() {
        click("#logoutBtn");
        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome!"));
    }

    @Test
    public void testExitBtnShowsPrompt() {
        click("#exitBtn");
        verifyThat("OK", NodeMatchers.isVisible());
        verifyThat("Cancel", NodeMatchers.isVisible());
        click("Cancel");

//        click("#exitBtn");
//        exit.expectSystemExitWithStatus(0);
//        click("OK");
    }

    @Test
    public void testAvailableBooksPopulated() {
        List<Book> booksList = Arrays.asList(
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
        List bookkeeperViewBooksList = bookkeeperViewController.booksList;

        for(int i=0;i<booksList.size();i++) {
            Book one = booksList.get(i);
            Book two = (Book) bookkeeperViewBooksList.get(i);
            assertEquals(one.getBookTitle(),(two.getBookTitle()));
            assertEquals(one.getBookYear(), (two.getBookYear()));
            assertEquals(one.getBookPrice(), (two.getBookPrice()), 0.0);
        }
    }

    @Test
    public void testDiscountsPerBookPopulated() {
        List<Discount> discountsPerBookList = Collections.singletonList(new Discount(">", 2000, 10));
        List bookkeeperViewDiscountsPerBookList = bookkeeperViewController.discountsPerBookList;

        for(int i=0;i<discountsPerBookList.size();i++) {
            Discount one = discountsPerBookList.get(i);
            Discount two = (Discount) bookkeeperViewDiscountsPerBookList.get(i);
            assertEquals(one.getCondition(),(two.getCondition()));
            assertEquals(one.getConditionValue(), (two.getConditionValue()), 0.0);
            assertEquals(one.getAmount(), (two.getAmount()));
        }
    }

    @Test
    public void testDiscountsOnTotalPopulated() {
        List<Discount> discountsOnTotalList = Collections.singletonList(new Discount(">", 30, 5));
        List bookkeeperViewDiscountsOnTotalList = bookkeeperViewController.discountsOnTotalList;

        for(int i=0;i<discountsOnTotalList.size();i++) {
            Discount one = discountsOnTotalList.get(i);
            Discount two = (Discount) bookkeeperViewDiscountsOnTotalList.get(i);
            assertEquals(one.getCondition(),(two.getCondition()));
            assertEquals(one.getConditionValue(), (two.getConditionValue()), 0.0);
            assertEquals(one.getAmount(), (two.getAmount()));
        }
    }

    @Test
    public void testAddBookSuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List bookkeeperViewBooksList = bookkeeperViewController.booksList;
        assertEquals(bookkeeperViewBooksList.size(),11);

        click("#newTitle").type("New Book");
        click("#newYear").type("2020");
        click("#newPrice").type("10");
        click("#addNewBook");

        bookkeeperViewBooksList = bookkeeperViewController.booksList;
        Book a = (Book) bookkeeperViewBooksList.get(bookkeeperViewBooksList.size()-1);
        assertEquals(a.getBookTitle(), "New Book");
        assertEquals(a.getBookYear(), 2020);
        assertThat(a.getBookPrice()).isEqualTo(10);

        assertEquals(bookkeeperViewBooksList.size(),12);
    }

    @Test
    public void testAddBookUnsuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        //all empty
        click("#addNewBook");
        verifyThat(find("#msg"), hasText("Add Book: Title cannot be empty."));

        //title empty
        click("#newYear").type("2020");
        click("#newPrice").type("10");
        click("#addNewBook");
        verifyThat(find("#msg"), hasText("Add Book: Title cannot be empty."));

        //year empty
        click("#newTitle").type("New Book");
        doubleClick("#newYear").eraseCharacters(1);
        click("#addNewBook");
        verifyThat(find("#msg"), hasText("Add Book: Year cannot be empty."));

        //price empty
        click("#newYear").type("2020");
        doubleClick("#newPrice").eraseCharacters(1);
        click("#addNewBook");
        verifyThat(find("#msg"), hasText("Add Book: Price cannot be empty."));

        //invalid year
        doubleClick("#newYear").eraseCharacters(1).type("InvalidYear");
        click("#newPrice").type("10");
        click("#addNewBook");
        verifyThat(find("#msg"), hasText("Add Book: Please enter a valid year > 0."));

        //invalid price
        doubleClick("#newYear").eraseCharacters(1).type("2020");
        doubleClick("#newPrice").eraseCharacters(1).type("InvalidPrice");
        click("#addNewBook");
        verifyThat(find("#msg"), hasText("Add Book: Please enter a valid price >= 0."));
    }

    @Test
    public void testRemoveBookSuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List<Book> bookkeeperViewBooksList = bookkeeperViewController.booksList;
        assertEquals(bookkeeperViewBooksList.size(), 11);

        click("#availableBooks");
        click("#removeOldBook");

        bookkeeperViewBooksList = bookkeeperViewController.booksList;
        assertEquals(bookkeeperViewBooksList.size(), 10);

        verifyThat(find("#msg"), hasText("Remove Book: Removed book successfully."));
    }

    @Test
    public void testRemoveBookUnsuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        click("#availableBooks");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook");
        click("#removeOldBook"); //remove last book
        verifyThat(find("#msg"), hasText("Remove Book: Removed book successfully."));

        click("#removeOldBook"); //no more books to remove
        verifyThat(find("#msg"), hasText("Remove Book: Please select a book to remove."));
    }

    @Test
    public void testAddDiscountPerBookSuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List bookkeeperViewDiscountsPerBookList = bookkeeperViewController.discountsPerBookList;
        assertEquals(bookkeeperViewDiscountsPerBookList.size(),1);

        //if book publication year == 2010, then discount by 3%
        click("#newBookCondition").press(KeyCode.EQUALS).release(KeyCode.EQUALS).press(KeyCode.EQUALS).release(KeyCode.EQUALS);
        click("#newBookConditionValue").type("2010");
        click("#newBookAmount").type("3");
        click("#addBookDiscount");

        Discount a = (Discount) bookkeeperViewDiscountsPerBookList.get(bookkeeperViewDiscountsPerBookList.size()-1);
        assertEquals(a.getCondition(), "==");
        assertThat(a.getConditionValue()).isEqualTo(2010);
        assertThat(a.getAmount()).isEqualTo(3);

        assertEquals(bookkeeperViewDiscountsPerBookList.size(),2);
    }

    @Test
    public void testAddDiscountPerBookUnsuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        //all empty
        click("#addBookDiscount");
        verifyThat(find("#msg"), hasText("Add Per Book Discount: Condition must be <, <=, >, >= or ==."));

        //condition empty
        click("#newBookConditionValue").type("2010");
        click("#newBookAmount").type("10");
        click("#addBookDiscount");
        verifyThat(find("#msg"), hasText("Add Per Book Discount: Condition must be <, <=, >, >= or ==."));

        //invalid condition
        click("#newBookCondition").type("InvalidCondition");
        click("#addBookDiscount");
        verifyThat(find("#msg"), hasText("Add Per Book Discount: Condition must be <, <=, >, >= or ==."));

        //condition value empty
        doubleClick("#newBookCondition").eraseCharacters(1).press(KeyCode.EQUALS).release(KeyCode.EQUALS).press(KeyCode.EQUALS).release(KeyCode.EQUALS);
        doubleClick("#newBookConditionValue").eraseCharacters(1);
        click("#addBookDiscount");
        verifyThat(find("#msg"), hasText("Add Per Book Discount: Condition value cannot be empty."));

        //amount empty
        click("#newBookConditionValue").type("2010");
        doubleClick("#newBookAmount").eraseCharacters(1);
        click("#addBookDiscount");
        verifyThat(find("#msg"), hasText("Add Per Book Discount: Amount cannot be empty."));

        //invalid condition value
        doubleClick("#newBookConditionValue").eraseCharacters(1).type("InvalidConditionValue");
        click("#newBookAmount").type("10");
        click("#addBookDiscount");
        verifyThat(find("#msg"), hasText("Add Per Book Discount: Please enter a valid integer condition year > 0."));

        //invalid amount
        doubleClick("#newBookConditionValue").eraseCharacters(1).type("2010");
        doubleClick("#newBookAmount").eraseCharacters(1).type("InvalidAmount");
        click("#addBookDiscount");
        verifyThat(find("#msg"), hasText("Add Per Book Discount: Please enter a valid integer amount >= 0 and <=100."));
    }

    @Test
    public void testRemoveDiscountPerBookSuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List<Discount> bookkeeperViewDiscountsPerBookList = bookkeeperViewController.discountsPerBookList;
        assertEquals(bookkeeperViewDiscountsPerBookList.size(), 1);

        moveBy(550,100);
        click();
        click("#removeBookDiscount");

        assertTrue(bookkeeperViewDiscountsPerBookList.isEmpty());

        verifyThat(find("#msg"), hasText("Remove Per Book Discount: Removed discount successfully."));
    }

    @Test
    public void testRemoveDiscountPerBookUnsuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        moveBy(550,100);
        click();
        click("#removeBookDiscount"); //remove last book
        verifyThat(find("#msg"), hasText("Remove Per Book Discount: Removed discount successfully."));

        click("#removeBookDiscount"); //no more discounts to remove
        verifyThat(find("#msg"), hasText("Remove Per Book Discount: Please select a discount to remove."));
    }

    @Test
    public void testAddDiscountOnTotalSuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List bookkeeperViewDiscountsOnTotalList = bookkeeperViewController.discountsOnTotalList;
        assertEquals(bookkeeperViewDiscountsOnTotalList.size(),1);

        //if book publication year == 2010, then discount by 3%
        click("#newTotalCondition").press(KeyCode.EQUALS).release(KeyCode.EQUALS).press(KeyCode.EQUALS).release(KeyCode.EQUALS);
        click("#newTotalConditionValue").type("57");
        click("#newTotalAmount").type("2");
        click("#addTotalDiscount");

        Discount a = (Discount) bookkeeperViewDiscountsOnTotalList.get(bookkeeperViewDiscountsOnTotalList.size()-1);
        assertEquals(a.getCondition(), "==");
        assertThat(a.getConditionValue()).isEqualTo(57);
        assertThat(a.getAmount()).isEqualTo(2);

        assertEquals(bookkeeperViewDiscountsOnTotalList.size(),2);
    }

    @Test
    public void testAddDiscountOnTotalUnsuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        //all empty
        click("#addTotalDiscount");
        verifyThat(find("#msg"), hasText("Add On Total Discount: Condition must be <, <=, >, >= or ==."));

        //condition empty
        click("#newTotalConditionValue").type("57");
        click("#newTotalAmount").type("2");
        click("#addTotalDiscount");
        verifyThat(find("#msg"), hasText("Add On Total Discount: Condition must be <, <=, >, >= or ==."));

        //invalid condition
        click("#newTotalCondition").type("InvalidCondition");
        click("#addTotalDiscount");
        verifyThat(find("#msg"), hasText("Add On Total Discount: Condition must be <, <=, >, >= or ==."));

        //condition value empty
        doubleClick("#newTotalCondition").eraseCharacters(1).press(KeyCode.EQUALS).release(KeyCode.EQUALS).press(KeyCode.EQUALS).release(KeyCode.EQUALS);
        doubleClick("#newTotalConditionValue").eraseCharacters(1);
        click("#addTotalDiscount");
        verifyThat(find("#msg"), hasText("Add On Total Discount: Condition value cannot be empty."));

        //amount empty
        click("#newTotalConditionValue").type("57");
        doubleClick("#newTotalAmount").eraseCharacters(1);
        click("#addTotalDiscount");
        verifyThat(find("#msg"), hasText("Add On Total Discount: Amount cannot be empty."));

        //invalid condition value
        doubleClick("#newTotalConditionValue").eraseCharacters(1).type("InvalidConditionValue");
        click("#newTotalAmount").type("2");
        click("#addTotalDiscount");
        verifyThat(find("#msg"), hasText("Add On Total Discount: Please enter a valid condition price > 0."));

        //invalid amount
        doubleClick("#newTotalConditionValue").eraseCharacters(1).type("57");
        doubleClick("#newTotalAmount").eraseCharacters(1).type("InvalidAmount");
        click("#addTotalDiscount");
        verifyThat(find("#msg"), hasText("Add On Total Discount: Please enter a valid integer amount >= 0 and <=100."));
    }

    @Test
    public void testRemoveDiscountOnTotalSuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List<Discount> bookkeeperViewDiscountsOnTotalList = bookkeeperViewController.discountsOnTotalList;
        assertEquals(bookkeeperViewDiscountsOnTotalList.size(), 1);

        moveBy(550,350);
        click();
        click("#removeTotalDiscount");

        assertTrue(bookkeeperViewDiscountsOnTotalList.isEmpty());

        verifyThat(find("#msg"), hasText("Remove On Total Discount: Removed discount successfully."));
    }

    @Test
    public void testRemoveDiscountOnTotalUnsuccessful() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        moveBy(550,350);
        click();
        click("#removeTotalDiscount"); //remove last book
        verifyThat(find("#msg"), hasText("Remove On Total Discount: Removed discount successfully."));

        click("#removeTotalDiscount"); //no more discounts to remove
        verifyThat(find("#msg"), hasText("Remove On Total Discount: Please select a discount to remove."));
    }

    @Test
    public void testAvailableBooksListAfterModifiedSavedWhenLogoutAndLogin() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List bookkeeperViewBooksList = bookkeeperViewController.booksList;
        assertEquals(bookkeeperViewBooksList.size(), 11);

        //two new books
        click("#newTitle").type("New Book");
        click("#newYear").type("2019");
        click("#newPrice").type("10");
        click("#addNewBook");

        click("#newTitle").type("New Book2");
        click("#newYear").type("2020");
        click("#newPrice").type("18.20");
        click("#addNewBook");

        moveBy(50,-200);
        click(); //select "New Book"
        click("#removeOldBook");

        assertEquals(bookkeeperViewBooksList.size(),12);

        Book a = (Book) bookkeeperViewBooksList.get(bookkeeperViewBooksList.size()-1);
        assertEquals(a.getBookTitle(), "New Book2");
        assertEquals(a.getBookYear(), 2020);
        assertEquals(a.getBookPrice(), 18.20, 0.0);

        click("#logoutBtn");
        click("#customerBtn");
        assertEquals(bookkeeperViewBooksList.size(),12);

        a = (Book) bookkeeperViewBooksList.get(bookkeeperViewBooksList.size()-1);
        assertEquals(a.getBookTitle(), "New Book2");
        assertEquals(a.getBookYear(), 2020);
        assertEquals(a.getBookPrice(), 18.20, 0.0);
    }

    @Test
    public void testDiscountsPerBookListAfterModifiedSavedWhenLogoutAndLogin() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List bookkeeperViewDiscountsPerBookList = bookkeeperViewController.discountsPerBookList;
        assertEquals(bookkeeperViewDiscountsPerBookList.size(), 1);

        //two new discounts
        click("#newBookCondition").press(KeyCode.SHIFT).press(KeyCode.COMMA).release(KeyCode.SHIFT).release(KeyCode.COMMA);
        click("#newBookConditionValue").type("1990");
        click("#newBookAmount").type("15");
        click("#addBookDiscount");

        click("#newBookCondition").press(KeyCode.SHIFT).press(KeyCode.PERIOD).release(KeyCode.SHIFT).release(KeyCode.PERIOD);
        click("#newBookConditionValue").type("2015");
        click("#newBookAmount").type("25");
        click("#addBookDiscount");

        moveBy(50,-125);
        click(); //select "book publication year < 1990, discount by 15"
        click("#removeBookDiscount");

        assertEquals(bookkeeperViewDiscountsPerBookList.size(),2);

        Discount a = (Discount) bookkeeperViewDiscountsPerBookList.get(bookkeeperViewDiscountsPerBookList.size()-1);
        assertEquals(a.getCondition(), ">");
        assertEquals(a.getConditionValue(), 2015, 0.0);
        assertEquals(a.getAmount(), 25);

        click("#logoutBtn");
        click("#customerBtn");
        assertEquals(bookkeeperViewDiscountsPerBookList.size(),2);

        a = (Discount) bookkeeperViewDiscountsPerBookList.get(bookkeeperViewDiscountsPerBookList.size()-1);
        assertEquals(a.getCondition(), ">");
        assertEquals(a.getConditionValue(), 2015, 0.0);
        assertEquals(a.getAmount(), 25);
    }

    @Test
    public void testDiscountsOnTotalListAfterModifiedSavedWhenLogoutAndLogin() {
        click("#logoutBtn");
        click("#bookkeeperBtn");
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        List bookkeeperViewDiscountsOnTotalList = bookkeeperViewController.discountsOnTotalList;
        assertEquals(bookkeeperViewDiscountsOnTotalList.size(), 1);

        //two new discounts
        click("#newTotalCondition").press(KeyCode.SHIFT).press(KeyCode.PERIOD).release(KeyCode.SHIFT).release(KeyCode.PERIOD);
        click("#newTotalConditionValue").type("50");
        click("#newTotalAmount").type("15");
        click("#addTotalDiscount");

        click("#newTotalCondition").press(KeyCode.SHIFT).press(KeyCode.PERIOD).release(KeyCode.SHIFT).release(KeyCode.PERIOD);
        click("#newTotalConditionValue").type("100");
        click("#newTotalAmount").type("25");
        click("#addTotalDiscount");

        moveBy(50,-125);
        click(); //select "total price of books in cart > 50, discount by 15"
        click("#removeTotalDiscount");

        assertEquals(bookkeeperViewDiscountsOnTotalList.size(),2);

        Discount a = (Discount) bookkeeperViewDiscountsOnTotalList.get(bookkeeperViewDiscountsOnTotalList.size()-1);
        assertEquals(a.getCondition(), ">");
        assertEquals(a.getConditionValue(), 100, 0.0);
        assertEquals(a.getAmount(), 25);

        click("#logoutBtn");
        click("#customerBtn");
        assertEquals(bookkeeperViewDiscountsOnTotalList.size(),2);

        a = (Discount) bookkeeperViewDiscountsOnTotalList.get(bookkeeperViewDiscountsOnTotalList.size()-1);
        assertEquals(a.getCondition(), ">");
        assertEquals(a.getConditionValue(), 100, 0.0);
        assertEquals(a.getAmount(), 25);
    }
}