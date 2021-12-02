package cucumber;

import csci310.models.Events;
import csci310.models.RawResult;
import csci310.models.User;
import csci310.utilities.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.Before;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import static org.junit.Assert.*;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Step definitions for Cucumber tests.
 */
public class StepDefinitions {
    private static final String ROOT_URL = "http://localhost:8080/";
    private final WebDriver driver = new ChromeDriver();

    private static String keywords = null;
    private static String startDate = null;
    private static String endDate = null;
    private static String countrycode = null;

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
        User u = new User("asdf", "asdf");
        DatabaseManager.object().deleteUser(u);
        keywords = null;
        startDate = null;
        endDate = null;
        countrycode = null;
    }

    @Before()
    public void before() {
        User u = new User("asdf", "asdf");
        DatabaseManager.object().insertUser(u);
    }

    @Given("I am on the signup page")
    public void iAmOnTheSignupPage() {
        driver.get(ROOT_URL + "signup.html");
    }

    @When("I fill out my credentials")
    public void iFillOutMyCredentials() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("asdf");
        try {
            driver.findElement(By.cssSelector("#input-password-confirm")).sendKeys("asdf");
        } catch(NoSuchElementException e) {}
    }

    @And("I go to the login page")
    public void iGoToTheLoginPage() {
        driver.get(ROOT_URL + "login.html");
    }

    @Then("I should be taken to the dashboard")
    public void iShouldBeTakenToTheDashboard() {
        assertEquals(driver.getCurrentUrl(), ROOT_URL+"dashboard.html");
    }

    @Then("I should see errors in the username and password field")
    public void iShouldSeeErrorsUnderTheUsernameAndPasswordField() {
        assertTrue(driver.findElement(By.cssSelector("#input-username")).getAttribute("class").contains("invalid"));
        assertTrue(driver.findElement(By.cssSelector("#input-password")).getAttribute("class").contains("invalid"));
    }

    @Given("I am on the login page")
    public void iAmOnTheLoginPage() {
        driver.get(ROOT_URL + "login.html");
    }

    @And("I go to the signup page")
    public void iGoToTheSignupPage() {
        driver.get(ROOT_URL + "signup.html");
    }

    @Then("I should see an error at the bottom of the screen")
    public void iShouldSeeAnErrorAtTheBottomOfTheScreen() {
        assertFalse(driver.findElement(By.cssSelector(".error-msg")).getAttribute("class").contains("hidden"));
    }

    @And("I fill out the wrong username")
    public void iFillOutTheWrongUsername() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("o");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("asdf");
    }
    @And("I fill out the wrong password")
    public void iFillOutTheWrongPassword() {
        driver.findElement(By.cssSelector("#input-username")).clear();
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf");
        driver.findElement(By.cssSelector("#input-password")).clear();
        driver.findElement(By.cssSelector("#input-password")).sendKeys("o");
    }

    @And("I fill out new credentials")
    public void iFillOutNewCredentials() {
        driver.findElement(By.cssSelector("#input-username")).sendKeys("asdf" + Integer.toString( (int)Math.random()*100000));
        driver.findElement(By.cssSelector("#input-password")).sendKeys("o");
    }

    @Then("I should be taken to the login page")
    public void iShouldBeTakenToTheLoginPage() {
        assertEquals(ROOT_URL + "login.html", driver.getCurrentUrl());
    }

    @And("I click on the log in button")
    public void iClickOnTheLogInButton() {
        driver.findElement(By.cssSelector(".on-enter-target")).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {}
    }

    @When("I click on the create account button")
    public void iClickOnTheCreateAccountButton() {
        driver.findElement(By.cssSelector("#create-account")).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {}
    }

    @And("I change the confirm password field to not match my password")
    public void iChangeTheConfirmPasswordFieldToNotMatchMyPassword() {
        User u = new User("asdf", "asdf");
        DatabaseManager.object().deleteUser(u);
        driver.findElement(By.cssSelector("#input-password-confirm")).sendKeys("12345");
    }

    @And("I click the cancel button")
    public void iClickTheCancelButton() {
        driver.findElement(By.cssSelector("#cancel")).click();
    }

    @Given("I am logged in")
    public void iAmLoggedIn() {
        iAmOnTheLoginPage();
        iFillOutMyCredentials();
        iClickOnTheLogInButton();
        iShouldBeTakenToTheDashboard();
    }

    @And("I click search")
    public void iClickSearch() {
        driver.findElement(By.id("search-button")).click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {

        }
    }

    @And("I enter keywords")
    public void iEnterKeywords() {
        keywords = "lit";
        driver.findElement(By.id("keywords")).sendKeys(keywords);
    }

    @Then("After timeout I should see the alert Signed out")
    public void iShouldSeeTheAlertSignedOut() throws InterruptedException {
        Thread.sleep(61000);
        Alert alert = driver.switchTo().alert();
        String alertMessage= driver.switchTo().alert().getText();
        assertEquals(alertMessage, "You have been safely logged out due to being inactive for more than 60 seconds.");
    }

    @Then("I should see results matching my query")
    public void iShouldSeeResultsMatchingMyQuery() {
        //replicate API call to match against results
        String urlString = databaseConfig.rootUrl + "&size=10"
                + (keywords != null ? "&keyword=" + keywords : "")
                + (startDate != null && endDate != null ? "&localStartDateTime=" + startDate + "T00:00:00" + "," + endDate + "T00:00:00": "")
                + (countrycode != null ? "&countryCode=" + countrycode.toUpperCase() : "");
        String apiResponse;

        try {
            apiResponse = HelperFunctions.get(urlString);
        } catch (IOException e) {
            fail("Error in API response");
            return;
        }

        RawResult rawResult = HelperFunctions.shared().fromJson(apiResponse,RawResult.class);
        Events events;

        try {
            events = new Events(rawResult);
        } catch (Exception e) {
            fail("Couldn't create events list");
            return;
        }

        int eventIndex = 0;
        List<WebElement> l = driver.findElements(By.cssSelector(".result"));
        Map<Long, String> ordinalNumbers = new HashMap<>(42);
        ordinalNumbers.put(1L, "1st");
        ordinalNumbers.put(2L, "2nd");
        ordinalNumbers.put(3L, "3rd");
        ordinalNumbers.put(21L, "21st");
        ordinalNumbers.put(22L, "22nd");
        ordinalNumbers.put(23L, "23rd");
        ordinalNumbers.put(31L, "31st");
        for (long d = 1; d <= 31; d++) {
            ordinalNumbers.putIfAbsent(d, "" + d + "th");
        }

        DateTimeFormatter dayOfMonthFormatter = new DateTimeFormatterBuilder()
                .appendPattern("MMMM ")
                .appendText(ChronoField.DAY_OF_MONTH, ordinalNumbers)
                .appendPattern(" yyyy")
                .toFormatter();
        for(WebElement w : l) {
            String date = w.findElement(By.cssSelector(".subtitle")).getAttribute("innerText");
            String title = w.findElement(By.cssSelector("a")).getAttribute("innerText");
            String url = w.findElement(By.cssSelector("a")).getAttribute("href");
            try {
                assertEquals(dayOfMonthFormatter.format(DateTimeFormatter.ofPattern("yyyy-MM-dd").parse(events.getEvents().get(eventIndex).getDate())).toUpperCase(), date);
            } catch (DateTimeException e) {
                fail("Could not parse date " + events.getEvents().get(eventIndex).getDate());
            }
            try {
                assertEquals(events.getEvents().get(eventIndex).getName(), title);
                assertEquals(events.getEvents().get(eventIndex).getUrl(), url);
            } catch (IndexOutOfBoundsException exp) {
                fail("Results longer than API returned list of events");
            }
            eventIndex++;
        }
        if(events.getEvents().size() == 0) {
            try {
                driver.findElement(By.cssSelector(".error"));
            } catch (NoSuchElementException exp) { fail("Could not see results empty message"); }
        }
    }

    @And("I specify a date range")
    public void iSpecifyADateRange() {
        startDate = "2021-11-10";
        endDate = "2021-12-25";
        driver.findElement(By.id("start-date")).sendKeys("11102021");
        driver.findElement(By.id("end-date")).sendKeys("12252021");
    }

    @And("I specify a location")
    public void iSpecifyALocation() {
        countrycode = "GB";
        driver.findElement(By.id("country")).sendKeys(countrycode);
    }

    @Given("I am on the signup page for the first time")
    public void iAmOnTheSignupPageForTheFirstTime() {
        User u = new User("asdf", "asdf");
        DatabaseManager.object().deleteUser(u);
        iAmOnTheSignupPage();
    }

    @And("I visit dashboard.html")
    public void iVisitDashboardHtml() {
        driver.get(ROOT_URL + "dashboard.html");
    }

    @And("I specify my location as North Korea")
    public void iSpecifyMyLocationAsNorthKorea() {
        driver.findElement(By.id("country")).sendKeys("KP");
    }

    @Then("I should see no results")
    public void iShouldSeeNoResults() {
        try {
            driver.findElement(By.cssSelector(".error"));
        } catch (NoSuchElementException exp) { fail("Could not see results empty message"); }
    }

    @And("I click profile")
    public void iClickProfile() {
        driver.findElement(By.cssSelector("#profile")).click();
    }

    @Then("I should be taken to the account page")
    public void iShouldBeTakenToTheAccountPage() {
        assertEquals(ROOT_URL + "account.html", driver.getCurrentUrl());
    }

    // pendinginvites.feature
    @Given("I am on the Pending Invites page")
    public void iAmOnThePendingInvitesPages() {
        iAmOnTheLoginPage();
        driver.findElement(By.cssSelector("#input-username")).sendKeys("test");
        driver.findElement(By.cssSelector("#input-password")).sendKeys("a");
        try {
            driver.findElement(By.cssSelector("#input-password-confirm")).sendKeys("a");
        } catch(NoSuchElementException e) {}
        iClickOnTheLogInButton();
        driver.get("http://localhost:8080/pendinginvites.html");
    }

    @When("I click the check mark")
    public void iClickTheCheckMark(){
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebElement Category_Body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("check")));
        Category_Body.click();
    }

    @Then("I should see the alert Accepted Invite")
    public void iShouldSeeTheAlertAcceptedInvite(){
        assertEquals(driver.switchTo().alert().getText(), "Accepted Invite");
    }

    @When("I click the cross mark")
    public void iClickTheCrossMark(){
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebElement Category_Body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("cross")));
        Category_Body.click();
    }

    @Then("I should see the alert Declined Invite")
    public void iShouldSeeTheAlertDeclinedInvite(){
        assertEquals(driver.switchTo().alert().getText(), "Declined Invite");
    }

    // proposalResponse.feature
    @Given("I am on the proposal response page")
    public void iAmOnTheProposalResponsePage() {
        iAmOnTheLoginPage();
        iFillOutMyCredentials();
        iClickOnTheLogInButton();
        driver.get(ROOT_URL + "proposalResponse.html");
        WebDriverWait wait = new WebDriverWait(driver, 1);
    }

    @And("I click yes")
    public void iClickYes() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        driver.findElement(By.cssSelector("#yes0")).click();
    }

    @Then("the button corresponding to yes should be clicked")
    public void theButtonCorrespondingToYesShouldBeClicked() {
        assertTrue(driver.findElement(By.cssSelector("input[id*='yes0']")).isSelected());
    }

    @And("I click 1 in the excitement menu")
    public void iClick1InTheExcitementMenu() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("0")));
        Select dropdown = new Select(driver.findElement(By.id("0")));
        dropdown.selectByVisibleText("1");

    }

    @Then("One should be selected for excitement")
    public void oneShouldBeSelectedForExcitement() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("0")));
        Select dropdown = new Select(driver.findElement(By.id("0")));
        WebElement w = dropdown.getFirstSelectedOption();
        assertEquals(1, Integer.parseInt(w.getAttribute("value")));
    }

    // dashboard.feature
    @And("I click next")
    public void i_click_next() {
        driver.findElement(By.id("next-button")).click();
    }

    @Then("I should see a no events alert")
    public void i_should_see_a_no_events_alert() {
        assertEquals(driver.switchTo().alert().getText(), "Please add at least one event");
    }

    @And("I select an event")
    public void i_select_an_event() throws InterruptedException {
        driver.findElement(By.id("keywords")).sendKeys("b");
        driver.findElement(By.id("search-button")).click();
        WebDriverWait wait = new WebDriverWait(driver, 50);
        WebElement Category_Body = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("eventsBox1")));
        Category_Body.click();
    }

    @And("I click submit")
    public void i_click_submit() {
        driver.findElement(By.id("submit-button")).click();
    }

    @Then("I should see a no users alert")
    public void i_should_see_a_no_users_alert() {
        assertEquals(driver.switchTo().alert().getText(), "Please add at least one user");
    }

    @And("I select an user")
    public void i_select_an_user() {
        WebDriverWait wait = new WebDriverWait(driver, 1);
        driver.findElement(By.id("username")).sendKeys("asdf");
        driver.findElement(By.id("username")).sendKeys(Keys.ENTER);
        driver.findElement(By.id("usersBox")).click();
    }

    @Then("I should see a no proposal name alert")
    public void i_should_see_a_no_proposal_name_alert() {
        assertEquals(driver.switchTo().alert().getText(), "Please add a proposal name");
    }

    @And("I write a proposal name")
    public void i_write_a_proposal_name() {
        driver.findElement(By.id("proposalName")).sendKeys("testing");
    }

    @Then("I should see a success alert")
    public void i_should_see_a_success_alert() {
        assertEquals(driver.switchTo().alert().getText(), "Proposal sent!");
    }

}