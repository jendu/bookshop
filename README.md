# bookshop

Bookshop implementation using JDK 8 (which includes JavaFX).
Test framework uses TestFX.

Calculates total purchase cost of selected books after applying any discounts.
Allows for management of books and discounts (UI for this is shown below).

![Admin UI](ui.png)

### How To Run:
1. I used JDK 8 which already includes JavaFX for building & running my application in IntelliJ IDEA. If using newer versions of Java, you will need to install JavaFX separately (as newer JDKs no longer include JavaFX).

    If using JDK > 8 and separate JavaFX, the following may be useful:

    1. https://www.jetbrains.com/help/idea/javafx.html#add-javafx-lib
    2. https://www.jetbrains.com/help/idea/javafx.html#vm-options

    You may also need to add "lib" to class path. If using IntelliJ IDEA:
    File -> Project Structure -> Modules -> Dependencies -> Add (+ button) -> Add the path to the "lib" directory included in this project.

    Once built, either run Main.java or Launcher.java. I only created the Launcher.java as it is needed in order to create an executable jar for JavaFX.

    If building is unsuccessful, you can still run my executable jar found in /out/artifacts/bookshop_jar. This is because I built it as a full fat jar which includes the required JavaFX libraries so you would not be required to have JavaFX installed.

2. To be able to run the tests successfully, you must be using JDK 8 as the test framework used (TestFX) is incompatible with higher JDK versions. When running tests, please do not use the keyboard or mouse as the tests launches the application and uses the mouse and keyboard operations to interact with the GUI. It is completely automated (the mouse/keyboard are controlled for the duration of these tests). Furthermore, MainTest.java must be run individually, but the rest of the tests (tests in /src/tests/View) can be run together all at once.

3. The username and password required to access the manage (add/remove/update) books and discounts page as the authorised bookkeeper was chosen for simplicity (and not security) and are:

    Username: Admin

    Password: Password
