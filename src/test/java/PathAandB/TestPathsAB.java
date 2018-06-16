package PathAandB;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.*;

import java.util.concurrent.TimeUnit;

public class TestPathsAB extends Methods {
    protected WebDriver driver;

    @DataProvider
    public Object[][] testData() {
        return new Object[][]{
                new Object[]{"webinar.test@gmail.com", "Xcg7299bnSmMuRLp9ITw"}
        };
    }

    @Parameters("browser")
    @BeforeTest
    protected WebDriver getDriver(String browser) {
        if(browser.equals("chrome")) {
            driver = new ChromeDriver();
            driver.manage().window().maximize();
        }
        else if(browser.equals("firefox")) {
            driver = new FirefoxDriver();;
        }
        else if (browser.equals("ie")) {
            driver = new InternetExplorerDriver();
        }
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
    }

    @Test(dataProvider = "testData")
    public void testPathA(String name, String password) throws InterruptedException {
        openSite(driver, "http://prestashop-automation.qatestlab.com.ua/admin147ajyvk0/");
        loginInAdminPanel(driver, name, password);
        openPageForCreateNewProduct(driver);
        generateRandomNameProduct(driver);
        generateRandomCountProduct(driver);
        generateRandomPriceProduct(driver);
        productActivation(driver);
        closeSuccessPopUp(driver);
        saveProduct(driver);
        closeSuccessPopUp(driver);
    }

    @Test
    public void testPathB() throws InterruptedException {
        openSite(driver, "http://prestashop-automation.qatestlab.com.ua/");
        openAllProducts(driver);
        searchCreatedProduct(driver);
        productNameVerification(driver);
        countProductVerification(driver);
        priceProductVerification(driver);
    }

    @AfterTest
    protected void tearDown() {
        if(driver != null)
            driver.quit();
    }
}


