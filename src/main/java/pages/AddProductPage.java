package pages;

import io.qameta.allure.Allure;
import utils.PageBase;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Log;


public class AddProductPage extends PageBase {

    By productNameInput = By.xpath("//input[@name='title']");
    By productPriceInput = By.xpath("//input[@name='price']");
    By productDescriptionInput = By.xpath("//input[@name='description']");
    By createButton = By.xpath("//button[@type='submit']");


    public AddProductPage(WebDriver driver) {
        super(driver);
    }

    public String enterProductName() {
        String productName = "Test Product " + randomString(3);
        Allure.step("Entering product name: " + productName);
        Log.info("Entering product name: "+ productName);
        waitForElement(productNameInput);
        setText(productNameInput, productName);
        return productName;

    }

    public String enterProductPrice() {
        String productPrice = String.valueOf(generateRandomPrice(3000));
        Allure.step("Entering product price: " + productPrice);
        Log.info("Entering product price: "+ productPrice);
        waitForElement(productPriceInput);
        clearText(productPriceInput);
        setText(productPriceInput, productPrice);
        return productPrice;
    }

    public String enterProductDescription() {
        String productDescription = "New product has been added to test " + randomString(5);
        Allure.step("Entering product description: " + productDescription);
        Log.info("Entering product description: "+ productDescription);
        waitForElement(productDescriptionInput);
        setText(productDescriptionInput, productDescription);
        return productDescription;
    }

    public void clickCreateButton() {
        Log.info("Clicking on Create button");
        Allure.step("Clicking on Create button");
        waitForClickable(createButton);
        click(createButton);
    }
}