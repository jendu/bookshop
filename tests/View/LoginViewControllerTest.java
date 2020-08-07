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

public class LoginViewControllerTest extends GuiTest {

    @Override
    protected Parent getRootNode() {
        FXMLLoader loader = new FXMLLoader(LoginViewController.class.getResource("/View/LoginView.fxml"));
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
    public void testLoginViewIsRetrieved() {
        verifyThat(find("#header"), hasText("\uD83D\uDCDA Bookshop"));

        verifyThat(find("#leftText"), hasText("Options:"));
        verifyThat(find("#homeBtn"), hasText("Home"));
        verifyThat(find("#exitBtn"), hasText("Exit"));

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Bookkeeper!"));
        verifyThat(find("#mainContent"),
                hasText("Please login in order to manage books/discounts."));

    }

    @Test
    public void testHomeBtnReturnsHomeView() {
        click("#homeBtn");
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
    public void testLoginSuccessful() {
        click("#username").type("Admin");
        click("#password").type("Password");
        click("#loginBtn");

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Available Books"));
    }

    @Test
    public void testLoginUnsuccessful() {
        //empty username
        click("#password").type("Password");
        click("#loginBtn");

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Bookkeeper!"));
        verifyThat(find("#msg"), hasText("Username cannot be left empty."));

        //incorrect username
        click("#username").type("Fail");
        click("#loginBtn");

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Bookkeeper!"));
        verifyThat(find("#msg"), hasText("Incorrect information provided. Please try again."));

        //incorrect password
        doubleClick("#username").eraseCharacters(1).type("Admin");
        doubleClick("#password").eraseCharacters(1).type("Fail");
        click("#loginBtn");

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Bookkeeper!"));
        verifyThat(find("#msg"), hasText("Incorrect information provided. Please try again."));

        //both incorrect
        doubleClick("#username").eraseCharacters(1).type("Fail");
        click("#loginBtn");

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Bookkeeper!"));
        verifyThat(find("#msg"), hasText("Incorrect information provided. Please try again."));

        //empty password
        doubleClick("#username").eraseCharacters(1).type("Admin");
        doubleClick("#password").eraseCharacters(1);
        click("#loginBtn");

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Bookkeeper!"));
        verifyThat(find("#msg"), hasText("Password cannot be left empty."));

        //both empty
        doubleClick("#username").eraseCharacters(1);
        click("#loginBtn");

        verifyThat(find("#mainTitle"), hasText("\uD83D\uDCD6 Welcome Bookkeeper!"));
        verifyThat(find("#msg"), hasText("Username cannot be left empty."));
    }
}