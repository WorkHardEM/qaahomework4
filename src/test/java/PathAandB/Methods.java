package PathAandB;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Methods {
    String randomName;
    int randomCount;
    float randomPrice;

    static String generateRandomWord(int wordLength) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(wordLength);
        for(int i = 0; i < wordLength; i++) {
            char tmp = (char) ('a' + r.nextInt('z' - 'a'));
            sb.append(tmp);
        }
        return sb.toString();
    }

    public void generateRandomNameProduct(WebDriver driver) {
        randomName = generateRandomWord(10);
        randomName = firstUpperCase(randomName);
        WebElement nameFieldLocator = driver.findElement(By.cssSelector("#form_step1_name_1"));
        nameFieldLocator.sendKeys(randomName);
        System.out.println("Generated random name product: " + randomName);
    }

    public String firstUpperCase(String randomName){
        if(randomName == null || randomName.isEmpty()) return "";
        return randomName.substring(0, 1).toUpperCase() + randomName.substring(1);
    }

    static int generateRandomCount() {
        int randomCount = (int) (1 + Math.random()* 99);
        return randomCount;
    }

    public void generateRandomCountProduct(WebDriver driver) throws InterruptedException {
        WebElement countTabLocator = driver.findElement(By.id("tab_step3"));
        countTabLocator.click();
        System.out.println("The tab \"quantity\" is opened");
        WebElement countFieldLocator = driver.findElement(By.id("form_step3_qty_0"));
        TimeUnit.SECONDS.sleep(2);
        countFieldLocator.clear();
        randomCount = generateRandomCount();
        countFieldLocator.sendKeys(String.valueOf(randomCount));
        System.out.println("Generated random count product: " + randomCount);
    }

    static float generateRandomPrice() {
        float randomPrice = (float) (0.1 + Math.random()* 100);
        randomPrice = new BigDecimal(randomPrice).setScale(2, RoundingMode.UP).floatValue();
        return randomPrice;
    }

    public void generateRandomPriceProduct(WebDriver driver) throws InterruptedException {
        WebElement priceTabLocator = driver.findElement(By.id("tab_step2"));
        priceTabLocator.click();
        TimeUnit.SECONDS.sleep(2);
        System.out.println("The tab \"prices\" is opened");
        WebElement priceFieldLocator = driver.findElement(By.id("form_step2_price"));
        priceFieldLocator.clear();
        randomPrice = generateRandomPrice();
        priceFieldLocator.sendKeys(String.valueOf(randomPrice));
        System.out.println("Genereted random price product: " + randomPrice);
    }

    public void closeSuccessPopUp(WebDriver driver) throws InterruptedException {
        new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".growl.growl-notice.growl-medium")));
        TimeUnit.SECONDS.sleep(2);
        WebElement closeSuccessPopupLocator2 = driver.findElement(By.cssSelector(".growl-close"));
        closeSuccessPopupLocator2.click();
        System.out.println("Close success Pop-up");
    }

    public void openSite(WebDriver driver, String url) throws InterruptedException {
        driver.get(url);
        System.out.println("Open Page: " + driver.getTitle());
    }

    public void openPageForCreateNewProduct(WebDriver driver) throws InterruptedException {
        Actions action = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 10);
        TimeUnit.SECONDS.sleep(3);
        WebElement catalogLocator = driver.findElement(By.xpath(".//*[@id='subtab-AdminCatalog']/a"));
        action.moveToElement(catalogLocator).perform();
        System.out.println("The mouse pointer is on the \"Catalog\" menu");;
        TimeUnit.SECONDS.sleep(3);
        driver.findElement(By.linkText("товары")).click();
        System.out.println("Clicked on the submenu \"products\"");
        TimeUnit.SECONDS.sleep(3);
        wait.until(ExpectedConditions.titleIs("товары • prestashop-automation"));
        System.out.println("The products page is open");
        WebElement newProductButtonLocator = driver.findElement(By.cssSelector("#page-header-desc-configuration-add"));
        newProductButtonLocator.click();
        TimeUnit.SECONDS.sleep(3);
        System.out.println("New product page is open");
    }

    public void productActivation(WebDriver driver) {
        WebElement switchProductLocator = driver.findElement(By.cssSelector(".switch-input"));
        switchProductLocator.click();
        System.out.println("The product is activated");
    }

    public void saveProduct(WebDriver driver) {
        WebElement saveButtonLocator = driver.findElement(By.cssSelector(".btn.btn-primary.js-btn-save"));
        saveButtonLocator.click();
        System.out.println("Product saved");
    }

    public void openAllProducts(WebDriver driver) throws InterruptedException {
        WebElement allProductLink = driver.findElement(By.xpath(".//*[@id='content']/section/a"));
        allProductLink.click();
        System.out.println("Click All Products");
        TimeUnit.SECONDS.sleep(5);
    }

    public void productNameVerification(WebDriver driver) {
        String productNameOnPage = driver.findElement(By.cssSelector(".h1")).getText();
        randomName = randomName.toUpperCase();
        Assert.assertEquals(randomName, productNameOnPage);
        System.out.println("The name of product on the page is the same as the name in the Admin panel");
    }

    public void countProductVerification(WebDriver driver) {
        String countOnPage = driver.findElement(By.cssSelector(".product-quantities>span")).getText();
        countOnPage = countOnPage.substring(0, 2).trim();
        int countOnPageInt = Integer.parseInt(countOnPage);
        Assert.assertEquals(randomCount, countOnPageInt);
        System.out.println("The count of product on the page is the same as the count in the Admin panel");
    }

    public void priceProductVerification(WebDriver driver) {
        String priceOnPage = driver.findElement(By.cssSelector(".current-price>span")).getAttribute("content");
        String randomPriceStr = String.valueOf(randomPrice);
        Assert.assertEquals(randomPriceStr, priceOnPage);
        System.out.println("The price of product on the page is the same as the price in the Admin panel");
    }

    // В этом методе я намудрил. Если много товаров, и только что созданный товар появляется не на первой странице,
    // то нужно переключаться и искать на следующей странице, метод ищет товар до 4 страницы включительно
    public void searchCreatedProduct(WebDriver driver) throws InterruptedException {
        WebElement nextPage = driver.findElement(By.cssSelector(".next.js-search-link"));
        try {
            searchProduct(driver);
        } catch (WebDriverException e1) {
            try {
                nextPage.click();
                searchProduct(driver);
            } catch (WebDriverException e2) {
                try {
                    nextPage.click();
                    searchProduct(driver);
                } catch (WebDriverException e3) {
                    nextPage.click();
                    searchProduct(driver);
                }
            }
        }
    }

    public void searchProduct(WebDriver driver) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        Assert.assertTrue(driver.findElement(By.linkText(randomName)).isDisplayed());
        System.out.println("The created product is found on the listing");
        driver.findElement(By.linkText(randomName)).click();
        System.out.println("The page of the created product is opened");
        TimeUnit.SECONDS.sleep(3);
    }

    public void loginInAdminPanel(WebDriver driver, String name, String password) throws InterruptedException {
        WebElement emailFieldLocator = driver.findElement(By.name("email"));
        emailFieldLocator.sendKeys(name);
        WebElement passwordFieldLocator = driver.findElement(By.name("passwd"));
        passwordFieldLocator.sendKeys(password);
        WebElement loginButtonLocator = driver.findElement(By.name("submitLogin"));
        loginButtonLocator.click();
        System.out.println("Login in admin panel");
        TimeUnit.SECONDS.sleep(3);
    }

}
