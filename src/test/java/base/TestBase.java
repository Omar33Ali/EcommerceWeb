package base;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import utils.ConfigReader;
import utils.DataReader;

import java.time.Duration;

public class TestBase {

    protected WebDriver driver;
    protected JSONObject testData;
    protected static final int implicitDuration = 10;

    @BeforeClass
    public void setUp() {

        // Set up WebDriver and load configuration
        driver = new ChromeDriver();
        // Initialize ConfigReader and load test data
        testData = DataReader.readFromResource("test_data.json");
        // Maximize the window and set implicit wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitDuration));
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser and clean up resources
        if (driver != null) {
            driver.quit();
        }
    }

}
