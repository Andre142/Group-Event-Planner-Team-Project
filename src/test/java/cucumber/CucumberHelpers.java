package cucumber;

import org.openqa.selenium.WebDriver;

public class CucumberHelpers {
    private static final String ROOT_URL = "http://localhost:8080/";

    public static void loginToUser(WebDriver driver, int id)
    {
        driver.get(ROOT_URL);
        if (driver.getCurrentUrl() != ROOT_URL + "login.html")
        {

        }
    }
}
