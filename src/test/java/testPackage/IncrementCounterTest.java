package testPackage;
import base.TestBase;
import io.qameta.allure.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HomePage;
import utils.ConfigReader;
import utils.Log;
import utils.VisualUtil;
import java.awt.image.BufferedImage;

@Epic("Increment Counter Tests")
@Feature("Visual Validation of Counter Increment")
public class IncrementCounterTest extends TestBase {

    SoftAssert softAssert;
    ConfigReader configReader;
    HomePage homePage;


    @BeforeMethod
    public void navigateToIncrementCounter() {
        configReader = new ConfigReader();
        driver.get(configReader.getProperty("incrementCounterURL"));
    }



    @Story("Verify Counter Increment with Visual Validation")
    @Description("This test verifies that incrementing the counter results in a visual change on the page using hybrid visual validation.")
    @Test
    public void verifyCounterIncrementHybrid() {
        homePage = new HomePage(driver);
        softAssert = new SoftAssert();
        homePage.clickSideNav();
        BufferedImage before = VisualUtil.capture(driver);
        VisualUtil.saveImage(before, "Counter_Before");
        attachScreenshot(driver, "Counter Before");
        Log.info("Captured before screenshot for visual comparison.");

        homePage.incrementCounter(1);
        BufferedImage after = VisualUtil.capture(driver);
        VisualUtil.saveImage(after, "Counter_After");
        attachScreenshot(driver, "Counter After");
        Log.info("Captured after screenshots for visual comparison.");


        boolean hasChange = VisualUtil.hasVisualChange(before, after);
        softAssert.assertTrue(hasChange, "Visual change not detected after incrementing counter.");
        softAssert.assertAll();

    }

    @Attachment(value = "{name}", type = "image/png")
    public byte[] attachScreenshot(WebDriver driver, String name) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
