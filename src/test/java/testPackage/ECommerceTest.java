package testPackage;

import base.TestBase;
import io.qameta.allure.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.AddProductPage;
import pages.HomePage;
import utils.ConfigReader;
import utils.Log;
import java.util.List;


@Epic("E-Commerce Application Tests")
@Feature("Add Product and Search Verification")
public class ECommerceTest extends TestBase {

    HomePage homePage;
    AddProductPage addProductPage;
    SoftAssert softAssert;
    ConfigReader configReader;


    @BeforeMethod
    public void navigateToEcommerce() {
        configReader = new ConfigReader();
        driver.get(configReader.getProperty("eCommerceURL"));
    }

    @Story("Add Product and Verify in UI")
    @Description("This test adds a new product and verifies its details in the UI, then searches for 'iphone' and validates the results.")
    @Test()
    public void addProductSuccessfully() {

        homePage = new HomePage(driver);
        addProductPage = new AddProductPage(driver);
        softAssert = new SoftAssert();

        Log.info("******* Navigating to Add Product Page *******");
        homePage.clickAddProductButton();
        Log.info("****** Filling in Product Details ********");
        String expectedProdName = addProductPage.enterProductName();
        String expectedProdPrice = addProductPage.enterProductPrice();
        String expectedProdDesc = addProductPage.enterProductDescription();
        addProductPage.clickCreateButton();
        Log.info("*****Searching for the newly added product: " + expectedProdName + " *****");
        homePage.searchProduct(expectedProdName);
        softAssert.assertEquals(homePage.getProductName(), expectedProdName, "Product Name does not match!");
        softAssert.assertEquals(homePage.getProductPrice(), expectedProdPrice, "Product Price does not match!");
        softAssert.assertEquals(homePage.getProductDescription(), expectedProdDesc, "Product Description does not match!");
        homePage.searchProduct(configReader.getProperty("searchKeyword"));
        Log.info("*****Searching for the product: " + configReader.getProperty("searchKeyword") + " *****");
        List<String> productNames = homePage.getAllProductNames();
        for (String name : productNames) {
            softAssert.assertTrue(name.toLowerCase().contains(configReader.getProperty("searchKeyword")), "Product name does not contain 'iphone': " + name);
        }
        softAssert.assertAll();

    }

}
