package utils;

import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class AllureScreenshotListener implements ITestListener {
    private WebDriver driver;

    // Inject driver (you can set it via constructor or a static holder)
    public AllureScreenshotListener(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void onTestFailure(ITestResult result) {
        if (driver != null) {
            captureScreenshot(driver, "Failure Screenshot - " + result.getName());
        }
    }

    @Attachment(value = "{name}", type = "image/png")
    public static byte[] captureScreenshot(WebDriver driver, String name) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
