package cucumber;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
	private static final String ROOT_URL = "http://localhost:8080/";

	private final WebDriver driver = new ChromeDriver();

	@Given("I am on the index page")
	public void i_am_on_the_index_page() {
		driver.get(ROOT_URL);
	}

	@When("I click the link {string}")
	public void i_click_the_link(String linkText) {
		driver.findElement(By.linkText(linkText)).click();
	}

	@Then("I should see header {string}")
	public void i_should_see_header(String header) {
		assertTrue(driver.findElement(By.cssSelector("h2")).getText().contains(header));
	}
	
	@Then("I should see text {string}")
	public void i_should_see_text(String text) {
		assertTrue(driver.getPageSource().contains(text));
	}

	@After()
	public void after() {
		driver.quit();
	}

    @Given("I am on the signup page")
    public void iAmOnTheSignupPage() {
        driver.get(ROOT_URL + "signup.html");
    }

    @When("I fill out my credentials")
    public void iFillOutMyCredentials() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("asdf");
    }

    @And("I click on the button at the bottom of the form")
    public void iClickOnTheButtonAtTheBottomOfTheForm() {
        driver.findElement(By.cssSelector(".on-enter-target")).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {

        }
    }

    @And("I go to the login page")
    public void iGoToTheLoginPage() {
        driver.get(ROOT_URL + "login.html");
    }

    @Then("I should see no errors")
    public void iShouldSeeNoErrors() {
        List<WebElement> l = driver.findElements(By.cssSelector(".error-msg"));
        for(WebElement w : l) {
            assertTrue(w.getAttribute("class").contains("hidden"));
        }
    }

    @Then("I should see errors under the username and password field")
    public void iShouldSeeErrorsUnderTheUsernameAndPasswordField() {
        List<WebElement> l = driver.findElements(By.cssSelector(".error-msg"));
        assertFalse(l.get(1).getAttribute("class").contains("hidden"));
        assertFalse(l.get(2).getAttribute("class").contains("hidden"));
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        driver.get(ROOT_URL + "login.html");
    }

    @And("I go to the signup page")
    public void iGoToTheSignupPage() {
        driver.get(ROOT_URL + "signup.html");
    }

    @Then("I should see an error at the top of the screen")
    public void iShouldSeeAnErrorAtTheTopOfTheScreen() {
        assertFalse(driver.findElement(By.cssSelector(".error-msg")).getAttribute("class").contains("hidden"));
    }

    @And("I fill out the wrong username")
    public void iFillOutTheWrongUsername() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("o");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("asdf");
    }
    @And("I fill out the wrong password")
    public void iFillOutTheWrongPassword() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("o");
    }

    @And("I fill out new credentials")
    public void iFillOutNewCredentials() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf" + Integer.toString( (int)Math.random()*100000));
        driver.findElement(By.cssSelector("#input-password")).sendKeys("o");
    }
}
