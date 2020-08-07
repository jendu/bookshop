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
import org.testfx.matcher.base.NodeMatchers;
import Model.Book;
import Model.Discount;

public class CustomerViewControllerTest extends GuiTest {

    private static CustomerViewController customerViewController;

    @Override
    protected Parent getRootNode() {
        FXMLLoader loader = new FXMLLoader(CustomerViewController.class.getResource("/View/CustomerView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        customerViewController = loader.getController();
        return root;
    }

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testCustomerViewIsRetrieved() {
        verifyThat(find("#header"), hasText("\uD83D\uDCDA Bookshop"));

        verifyThat(find("#leftText"), hasText("Options:"));
        verifyThat(find("#logoutBtn"), hasText("Logout"));
        verifyThat(find("#exitBtn"), hasText("Exit"));

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Customer!"));
        verifyThat(find("#mainContent"),
                hasText("Below is the list of books available. Please select the books you would like to purchase."));
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
        List customerViewBooksList = customerViewController.booksList;

        for(int i=0;i<booksList.size();i++) {
            Book one = booksList.get(i);
            Book two = (Book) customerViewBooksList.get(i);
            assertEquals(one.getBookTitle(),(two.getBookTitle()));
            assertEquals(one.getBookYear(), (two.getBookYear()));
            assertEquals(one.getBookPrice(), (two.getBookPrice()), 0.0);
        }
    }

    @Test
    public void testDiscountsPerBookPopulated() {
        List<Discount> discountsPerBookList = Collections.singletonList(new Discount(">", 2000, 10));
        List customerViewDiscountsPerBookList = customerViewController.discountsPerBookList;

        for(int i=0;i<discountsPerBookList.size();i++) {
            Discount one = discountsPerBookList.get(i);
            Discount two = (Discount) customerViewDiscountsPerBookList.get(i);
            assertEquals(one.getCondition(),(two.getCondition()));
            assertEquals(one.getConditionValue(), (two.getConditionValue()), 0.0);
            assertEquals(one.getAmount(), (two.getAmount()));
        }
    }

    @Test
    public void testDiscountsOnTotalPopulated() {
        List discountsOnTotalList = Collections.singletonList(new Discount(">", 30, 5));
        List customerViewDiscountsOnTotalList = customerViewController.discountsOnTotalList;

        for(int i=0;i<discountsOnTotalList.size();i++) {
            Discount one = (Discount) discountsOnTotalList.get(i);
            Discount two = (Discount) customerViewDiscountsOnTotalList.get(i);
            assertEquals(one.getCondition(),(two.getCondition()));
            assertEquals(one.getConditionValue(), (two.getConditionValue()), 0.0);
            assertEquals(one.getAmount(), (two.getAmount()));
        }
    }

    @Test
    public void testAddToCart() {
        click("#logoutBtn");
        click("#customerBtn");

        List customerViewShoppingList = customerViewController.shoppingList;
        assertTrue(customerViewShoppingList.isEmpty());

        click("#addBtn"); //add "Moby Dick"

        assertEquals(customerViewShoppingList.size(),1);

        Book a = (Book) customerViewShoppingList.get(0);
        assertEquals(a.getBookTitle(), "Moby Dick");
        assertEquals(a.getBookYear(), 1851);
        assertEquals(a.getBookPrice(), 15.20, 0.0);

        verifyThat(find("#finalCost"), hasText("Final Cost: £15.20"));
    }

    @Test
    public void testRemoveFromCartSuccessful() {
        click("#logoutBtn");
        click("#customerBtn");

        click("#addBtn");

        List customerViewShoppingList = customerViewController.shoppingList;

        assertEquals(customerViewShoppingList.size(),1);
        verifyThat(find("#finalCost"), hasText("Final Cost: £15.20"));

        moveBy(50,50);
        click(); //select "Moby Dick"
        click("#removeBtn");

        assertTrue(customerViewShoppingList.isEmpty());
        verifyThat(find("#finalCost"), hasText("Final Cost: £0.00"));
    }

    @Test
    public void testRemoveFromCartUnsuccessful() {
        click("#logoutBtn");
        click("#customerBtn");

        click("#removeBtn");
        verifyThat(find("#msg"), hasText("Please select a book to remove."));
    }


    @Test
    public void testCostCalculationCorrect() {
        click("#logoutBtn");
        click("#customerBtn");

        moveBy(300,150); //add "The Terrible Privacy of Maxwell Sim"
        click();
        click("#addBtn");

        moveBy(-280, 150); //add "Three Men in a Boat"
        click();
        click("#addBtn");

        moveBy(-280, 300); //add "Great Expectations"
        click();
        click("#addBtn");

        verifyThat(find("#finalCost"), hasText("Final Cost: £36.01"));
    }

    @Test
    public void testShoppingCartSavedWhenLogoutAndLogin() {
        click("#logoutBtn");
        click("#customerBtn");
        List customerViewShoppingList = customerViewController.shoppingList;
        assertTrue(customerViewShoppingList.isEmpty());

        click("#addBtn"); //add "Moby Dick"
        click("#addBtn"); //add "Moby Dick" again

        moveBy(50,50);
        click(); //select "Moby Dick"
        click("#removeBtn");

        assertEquals(customerViewShoppingList.size(),1);
        verifyThat(find("#finalCost"), hasText("Final Cost: £15.20"));

        Book a = (Book) customerViewShoppingList.get(0);
        assertEquals(a.getBookTitle(), "Moby Dick");
        assertEquals(a.getBookYear(), 1851);
        assertEquals(a.getBookPrice(), 15.20, 0.0);

        click("#logoutBtn");
        click("#customerBtn");
        assertEquals(customerViewShoppingList.size(),1);

        a = (Book) customerViewShoppingList.get(0);
        assertEquals(a.getBookTitle(), "Moby Dick");
        assertEquals(a.getBookYear(), 1851);
        assertEquals(a.getBookPrice(), 15.20, 0.0);

        verifyThat(find("#finalCost"), hasText("Final Cost: £15.20"));
    }
}