import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.loadui.testfx.controls.Commons.hasText;
import static org.testfx.api.FxAssert.verifyThat;

public class MainTest extends ApplicationTest {

    @Before
    public void setUpClass() throws Exception {
        ApplicationTest.launch(Main.class);
    }

    @Test
    public void testHomeViewIsRetrieved() {
        verifyThat("#header", hasText("\uD83D\uDCDA Bookshop"));

        verifyThat("#leftText", hasText("Enter as:"));
        verifyThat("#customerBtn", hasText("Customer"));
        verifyThat("#bookkeeperBtn", hasText("Bookkeeper"));
        verifyThat("#exitBtn", hasText("Exit"));

        verifyThat("#mainTitle", hasText("\uD83D\uDCD6 Welcome!"));
        verifyThat("#mainContent",
                hasText("Welcome to the Bookshop. Please enter either as a customer, or a bookkeeper."));
    }
}