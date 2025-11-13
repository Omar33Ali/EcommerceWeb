package pages;

import utils.Log;
import utils.PageBase;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;


public class HomePage extends PageBase {


    By addButton = By.cssSelector("a[href='/add']");
    By searchInput = By.xpath("//input[contains(@placeholder, 'Search ')]");
    By productName = By.xpath("(//div[contains(@class, 'cursor-pointer')])[1]");
    By productDescription = By.xpath("(//div[contains(@class, 'text-xs')])[1]");
    By productPrice = By.xpath("(//div[@class='card-price'])[1]");
    By allProductNames = By.xpath("//div[contains(@class, 'cursor-pointer')]");

    // Counter Locators
    By counterInput = By.xpath("//input[@type='number']");
    By sideNav = By.xpath("//button[contains(@aria-label, 'sidenav')]");
    By loadingSpinner = By.cssSelector(".spinner");

    public HomePage(WebDriver driver) {
        super(driver);
    }


    public void clickAddProductButton() {
        Log.info("Clicking on Add Product button");
        Allure.step("Clicking on Add Product button");
        waitForClickable(addButton);
        click(addButton);
    }


    public void searchProduct(String productName) {
        Log.info("Searching for product: " + productName);
        Allure.step("Searching for product: " + productName);
        waitForElement(searchInput);
        clearText(searchInput);
        setText(searchInput, productName);
        clickEnter();
        waitForTextToBePresent(this.productName, productName, 1);
    }

    public String getProductName() {
        waitForElement(productName);
        String prodName = getElementText(productName).trim();
        Log.info("Getting product name: " + prodName);
        Allure.step("Getting product name: " + prodName);
        return prodName.trim();
    }

    public String getProductPrice() {
        waitForElement(productPrice);
        String productPriceTxt = getElementText(productPrice).trim().replace("USD", "");
        Log.info("Getting product price: " + productPriceTxt);
        Allure.step("Getting product price: " + productPriceTxt);
        return productPriceTxt.trim();
    }

    public String getProductDescription() {
        waitForElement(productDescription);
        String prodDesc = getElementText(productDescription).trim();
        Log.info("Getting product description: " + prodDesc);
        Allure.step("Getting product description: " + prodDesc);
        return prodDesc.trim();
    }

    public List<String> getAllProductNames() {
        waitForElements(allProductNames);
        List<String> productNames = getTextFromElements(allProductNames);
        Log.info("Getting all product names: " + productNames);
        Allure.step("Getting all product names: " + productNames);
        return productNames;
    }

    public void incrementCounter(int value)
    {
        Log.info("Setting counter input to: " + value);
        Allure.step("Setting counter input to: " + value);
        waitForElement(counterInput);
        clearText(counterInput);
        setText(counterInput, String.valueOf(value));
        waitForPageLoad(2);
    }

    public void clickSideNav() {
        Log.info("Clicking on Side Navigation button");
        Allure.step("Clicking on Side Navigation button");
        waitForClickable(sideNav);
        click(sideNav);
        waitUntilLoaded(loadingSpinner);
    }


}
