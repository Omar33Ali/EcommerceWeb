package testPackage;
import base.TestBase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HomePage;
import utils.ConfigReader;
import utils.Log;
import utils.VisualUtil;
import java.awt.image.BufferedImage;


public class IncrementCounterTest extends TestBase {

    SoftAssert softAssert;
    ConfigReader configReader;
    HomePage homePage;


    @BeforeMethod
    public void navigateToIncrementCounter() {
        configReader = new ConfigReader();
        driver.get(configReader.getProperty("incrementCounterURL"));
    }


    @Test
    public void verifyCounterIncrementHybrid() {
        homePage = new HomePage(driver);
        softAssert = new SoftAssert();

        BufferedImage before = VisualUtil.capture(driver);
        VisualUtil.attachScreenshot(before, "Counter Before");
        Log.info("Captured before screenshot for visual comparison.");
        BufferedImage after = VisualUtil.capture(driver);
        VisualUtil.attachScreenshot(after, "Counter After");
        Log.info("Captured after screenshots for visual comparison.");
        homePage.clickSideNav();
        homePage.incrementCounter(1);
        boolean hasChange = VisualUtil.hasVisualChange(before, after);
        softAssert.assertTrue(hasChange, "Visual change not detected after incrementing counter.");
        softAssert.assertAll();

    }


}
