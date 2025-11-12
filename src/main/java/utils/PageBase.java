package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PageBase {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public PageBase(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    /**
     * Waits for the presence of all elements located by the specified locator.
     *
     * @param locator The By locator to find the elements.
     * @return A list of web elements that are present on the page.
     */
    public List<WebElement> waitForElements(By locator) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Waits for the presence of a single web element located by the specified locator.
     *
     * @param locator The By locator to find the element.
     */
    public void waitForElement(By locator) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        fluentWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        fluentWait.until(driver -> driver.findElement(locator));
    }


    /**
     * Waits for a web element to be clickable.
     *
     * @param locator The By locator to find the element.
     */
    protected void waitForClickable(By locator) {
        try {
            Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(10))
                    .pollingEvery(Duration.ofMillis(500))
                    .ignoring(NoSuchElementException.class)
                    .ignoring(StaleElementReferenceException.class);

            fluentWait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            System.out.println("Element not clickable: " + locator);
        }
    }

    /**
     *
     */
    public boolean waitForElementDisplayed(By locator, int timeoutSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void waitForPageLoad(int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete")
        );
    }

    public boolean waitForTextToBePresent(By locator, String expectedText, int timeoutSecs) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSecs));
            return customWait.until(ExpectedConditions.textToBePresentInElementLocated(locator, expectedText));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the text of a web element located by the specified locator.
     *
     * @param locator The By locator to find the element.
     * @return The text of the web element.
     */
    public String getElementText(By locator) {
        return driver.findElement(locator).getText();
    }

    public void setText(By locator, String text) {
        driver.findElement(locator).sendKeys(text);
    }

    public void click(By locator) {
        driver.findElement(locator).click();
    }

    public boolean isElementDisplayed(By locator) {
        return driver.findElement(locator).isDisplayed();
    }

    public String randomString(int length) {
        if (length <= 0) return "";
        java.security.SecureRandom rnd = new java.security.SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append((char) ('0' + rnd.nextInt(10)));
        }
        return sb.toString();
    }

    public static String generateStreet() {
        return "Street-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public void uploadFilesByRobot(String filePath) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        robot.delay(1000);
        StringSelection stringSelection = new StringSelection(filePath);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(500);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        System.out.println("File path was successfully sent to the Windows upload popup.");
    }

    public void clickEnter() {
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.ENTER).perform();
    }


    public void selectDropdownOption(WebDriver driver, By dropdownLocator, String visibleText) {
        WebElement dropdown = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(dropdownLocator));
        dropdown.click();

        if (visibleText != null) {
            By optionLocator = By.xpath("//div[@role='option' and normalize-space()='" + visibleText + "']");
            WebElement option = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(optionLocator));
            option.click();
        } else {
            // Select first available option
            List<WebElement> options = driver.findElements(By.xpath("//div[@role='option']"));
            if (!options.isEmpty()) options.get(0).click();
        }
    }

    public void printAllCookies() {
        Set<Cookie> cookies = driver.manage().getCookies();
        System.out.println("=== Browser cookies ===");
        for (Cookie c : cookies) {
            System.out.println(c.getName() + " -> " + c.getValue() + " (httpOnly=" + c.isHttpOnly() + ", domain=" + c.getDomain() + ")");
        }
        System.out.println("=======================");
    }

    /**
     * Generate a random price starting from 30 up to a given max.
     *
     * @param maxPrice The maximum price limit
     * @return Random price between 30 and maxPrice
     */
    public static int generateRandomPrice(int maxPrice) {
        return ThreadLocalRandom.current().nextInt(30, maxPrice + 1);
    }

    /**
     * Generate a random price starting from 30 with decimals.
     *
     * @param maxPrice The maximum price limit
     * @return Random price between 30.00 and maxPrice (double)
     */
    public static double generateRandomPriceDouble(double maxPrice) {
        return ThreadLocalRandom.current().nextDouble(30.0, maxPrice);
    }

    public List<String> getTextFromElements(By locator) {
        List<WebElement> elements = driver.findElements(locator);

        // Stream over elements and collect their text
        return elements.stream()
                .map(WebElement::getText)
                .map(String::trim) // optional: clean whitespace
                .toList();
    }

    public void clearText(By locator) {
        driver.findElement(locator).clear();
    }



}


