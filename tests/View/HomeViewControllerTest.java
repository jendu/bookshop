package View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.loadui.testfx.GuiTest;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import static org.loadui.testfx.controls.Commons.hasText;
import static org.testfx.api.FxAssert.verifyThat;
import org.testfx.matcher.base.NodeMatchers;

public class HomeViewControllerTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        FXMLLoader loader = new FXMLLoader(HomeViewController.class.getResource("/View/HomeView.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Test
    public void testHomeViewIsRetrieved() {
        verifyThat(find("#header"), hasText("\uD83D\uDCDA Bookshop"));

        verifyThat(find("#leftText"), hasText("Enter as:"));
        verifyThat(find("#customerBtn"), hasText("Customer"));
        verifyThat(find("#bookkeeperBtn"), hasText("Bookkeeper"));
        verifyThat(find("#exitBtn"), hasText("Exit"));

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome!"));
        verifyThat(find("#mainContent"),
                hasText("Welcome to the Bookshop. Please enter either as a customer, or a bookkeeper."));
    }

    @Test
    public void testCustomerBtnReturnsCustomerView() {
        click("#customerBtn");
        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Customer!"));
    }

    @Test
    public void testBookkeeperBtnReturnsBookkeeperView() {
        click("#bookkeeperBtn");
        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Bookkeeper!"));
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

}