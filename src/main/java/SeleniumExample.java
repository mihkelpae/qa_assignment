import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class SeleniumExample {

    private static WebDriver driver;
    private static Actions actions;

    private static ExtentReports extent;

    private static ExtentTest test;

    @BeforeClass
    public static void setup(){
        driver = new ChromeDriver();
        actions = new Actions(driver);

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test_results.html");
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        test = extent.createTest("Testing loan calculator", "Test description");

    }

    @AfterClass
    public static void teardown(){
        extent.flush();
        driver.quit();
    }

    //Removing amount from field
    public void removeAmount(WebElement field){
        actions.doubleClick(field).sendKeys(Keys.BACK_SPACE).perform();
    }

    public void navigateToPage (String url){
        driver.get(url);
    }

    //Testing if amount field works
    @Test
    public void testLoanAmountField() throws InterruptedException {
        navigateToPage("https://laenutaotlus.bigbank.ee/");
        Thread.sleep(1000);

        WebElement amountField = driver.findElement(By.name("header-calculator-amount"));
        removeAmount(amountField);
        amountField.sendKeys("10000");
        String fieldValue = amountField.getAttribute("value");
        Assert.assertEquals("10000", fieldValue);

        test.log(Status.PASS, "Loan amount field works");
    }

    @Test
    public void testPeriodAmountField() throws InterruptedException {
        navigateToPage("https://laenutaotlus.bigbank.ee/");
        Thread.sleep(1000);

        WebElement periodField = driver.findElement(By.name("header-calculator-period"));
        removeAmount(periodField);
        periodField.sendKeys("36");
        String fieldValue = periodField.getAttribute("value");

        Assert.assertTrue("Should be true",fieldValue.equals("36"));
        test.log(Status.PASS, "Loan period field works");
    }

    // Adding new amount but not clicking save
    // After closing modal, verifying that amount is not saved
    @Test
    public void notSavingAmount() throws InterruptedException {
        navigateToPage("https://laenutaotlus.bigbank.ee/");
        Thread.sleep(1000);

        WebElement amountField = driver.findElement(By.name("header-calculator-amount"));
        removeAmount(amountField);
        amountField.sendKeys("10000");

        actions.sendKeys(Keys.ESCAPE);

        String amountAfterClosingModal = driver.findElement(By.className("bb-edit-amount__amount")).getText();
        Assert.assertEquals("5000 €",amountAfterClosingModal);

        test.log(Status.PASS, "Amount is not saved");
    }

    // Adding new amount to amount field and saving
    // Making sure saved amount is displayed
    @Test
    public void savingAmount() throws InterruptedException {
        navigateToPage("https://laenutaotlus.bigbank.ee/");
        Thread.sleep(1000);

        WebElement amountField = driver.findElement(By.name("header-calculator-amount"));
        removeAmount(amountField);
        amountField.sendKeys("10000");

        driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/header/div/dialog/div[1]/div[3]/button")).click();
        String amountAfterClosingModal = driver.findElement(By.className("bb-edit-amount__amount")).getText();
        Assert.assertEquals("10000 €",amountAfterClosingModal);
        test.log(Status.PASS, "Amount is saved");


    }

    // Changing amount and periodicity and checking if monthly payment amount
    // has also changed
    @Test
    public void checkingMonthlyPayment() throws InterruptedException {
        navigateToPage("https://laenutaotlus.bigbank.ee/");
        Thread.sleep(1000);
        String originalMonthlyPayment = driver.findElement(By.className("bb-labeled-value__value")).getText();

        WebElement amountField = driver.findElement(By.name("header-calculator-amount"));
        removeAmount(amountField);
        amountField.sendKeys("10000");

        WebElement periodField = driver.findElement(By.name("header-calculator-period"));
        removeAmount(periodField);
        periodField.sendKeys("72");

        Thread.sleep(2000);
        String newMonthlyPayment = driver.findElement(By.className("bb-labeled-value__value")).getText();
        Assert.assertNotEquals(originalMonthlyPayment,newMonthlyPayment);
        test.log(Status.PASS, "Monthly payment amount works");

    }


}