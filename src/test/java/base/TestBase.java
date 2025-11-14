package base;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import utils.DataReader;

import java.time.Duration;

public class TestBase {

    protected WebDriver driver;
    protected JSONObject testData;
    protected static final int implicitDuration = 10;

    @BeforeClass
    public void setUp() {

        ChromeOptions options = buildChromeOptions();
        driver = new ChromeDriver(options);
        // Initialize ConfigReader and load test data
        testData = DataReader.readFromResource("test_data.json");
        // Maximize the window and set implicit wait
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitDuration));
    }

    private ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        if (shouldRunHeadless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private boolean shouldRunHeadless() {
        String headlessProp = System.getProperty("headless");
        if (headlessProp != null) {
            return Boolean.parseBoolean(headlessProp);
        }
        String ciEnv = System.getenv("CI");
        if (ciEnv != null) {
            return true;
        }
        String osName = System.getProperty("os.name", "").toLowerCase();
        return osName.contains("linux") || osName.contains("unix");
    }

    @AfterMethod
    public void tearDown() {
        // Close the browser and clean up resources
        if (driver != null) {
            driver.quit();
        }
    }

}
