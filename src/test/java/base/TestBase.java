package base;

import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import utils.DataReader;
import utils.ConfigReader;
import utils.Log;

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
        if (!shouldRunHeadless()) {
            driver.manage().window().maximize();
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitDuration));
    }

    private boolean shouldRunHeadless() {
        String headlessProp = System.getProperty("headless");
        if (headlessProp != null) {
            return Boolean.parseBoolean(headlessProp);
        }
        String headlessEnv = System.getenv("HEADLESS");
        if (headlessEnv != null) {
            return Boolean.parseBoolean(headlessEnv);
        }
        String ciEnv = System.getenv("CI");
        if (ciEnv != null) {
            return true;
        }
        String gha = System.getenv("GITHUB_ACTIONS");
        if (gha != null) {
            return true;
        }
        try {
            ConfigReader cr = new ConfigReader();
            String cfg = cr.getProperty("headless");
            if (cfg != null) {
                return Boolean.parseBoolean(cfg.trim());
            }
        } catch (Exception ignored) {}
        String osName = System.getProperty("os.name", "").toLowerCase();
        return osName.contains("linux") || osName.contains("unix");
    }

    private ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        String chromeBin = System.getenv("CHROME_BIN");
        if (chromeBin != null && !chromeBin.isBlank()) {
            options.setBinary(chromeBin);
        }
        boolean headless = shouldRunHeadless();
        Log.info("Effective headless mode: " + headless);
        if (headless) {
            System.setProperty("java.awt.headless", "true");
        }
        options.setAcceptInsecureCerts(true);
        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE);
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--headless");
        }
        return options;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}
