import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class SeleniumExample {

    private WebDriver driver;
    private Actions actions;


    @Before
    public void setup(){
        driver = new ChromeDriver();
        actions = new Actions(driver);
    }

    @After
    public void quit(){
        driver.quit();
    }


    //Removing amount from field
    public void removeAmount(WebElement field){
        actions.doubleClick(field).sendKeys(Keys.BACK_SPACE).perform();
    }

    public void addAmount(WebElement field){
        field.sendKeys("10000");
    }

    @Test
    public void testLoanAmountField() throws InterruptedException {
        driver.get("https://laenutaotlus.bigbank.ee/");

        WebElement amountField = driver.findElement(By.name("header-calculator-amount"));
        WebElement periodField = driver.findElement(By.name("header-calculator-period"));

        removeAmount(amountField);
        addAmount(amountField);
        String fieldValue = amountField.getAttribute("value");
        Assert.assertEquals("10000", fieldValue);


        removeAmount(periodField);

        Thread.sleep(20000);

    }



}