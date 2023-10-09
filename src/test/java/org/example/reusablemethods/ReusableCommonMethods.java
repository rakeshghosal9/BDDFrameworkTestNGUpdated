package org.example.reusablemethods;

import io.cucumber.java.Scenario;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

public class ReusableCommonMethods {

    static long GLOBAL_EXPLICIT_TIMEOUT = 60;
    static long GLOBAL_EXPLICIT_POLLING_TIME = 2;

    /**
     * This method will return a Wait<WebDriver> object of type fluent wait by accepting timeout and polling time
     * as parameter.
     * @param timeout - Total wait time by Fluent Wait
     * @param pollingTime - Polling time is the interval after which Fluent Wait will keep checking
     * @param driver - WebDriver object
     * @return Wait<WebDriver>
     */
    public static Wait<WebDriver> getFluentWaitObject(int timeout, int pollingTime, WebDriver driver) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofSeconds(pollingTime))
                .ignoring(NoSuchElementException.class);
    }

    /**
     * This method will return a Wait<WebDriver> object of type fluent wait for which timeout and polling time would
     * be taken from config.properties file.
     * @param driver - WebDriver object
     * @return Wait<WebDriver>
     */
    public static Wait<WebDriver> getFluentWaitObject(WebDriver driver) {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(GLOBAL_EXPLICIT_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(GLOBAL_EXPLICIT_POLLING_TIME))
                .ignoring(NoSuchElementException.class);
    }

    /**
     * This method will wait for a WebElement to be visible using Fluent Wait with timeout time as 60 secs and polling
     *  time as 2 secs.
     * @param element - The WebElement to be visible
     * @param driver - WebDriver object
     * @param timeout - Total wait time by Fluent Wait for the element to be visible
     * @return boolean
     */
    public static boolean waitForElementToBeVisible(WebElement element, WebDriver driver, int timeout) {
        try {
            Wait<WebDriver> fluentWait = getFluentWaitObject(60, 2, driver);
            fluentWait.until(ExpectedConditions.visibilityOf(element));
            return true;

        } catch (Exception e) {

            System.out.println("Element not visible after waiting for [" + timeout + "] seconds");
            return false;
        }
    }

    /**
     * This method will enter value in a text box WebElement by accepting element, value as parameter.
     * @param element - The text box in which value needs to be entered
     * @param value - Value to be entered in the text box
     * @param driver - Webdriver object
     * @return boolean
     */
    public static boolean enterValueInTextBox(WebElement element, String value, WebDriver driver) {
        try {
            Wait<WebDriver> fluentWait = getFluentWaitObject(driver);
            fluentWait.until(ExpectedConditions.elementToBeClickable(element));
            element.clear();
            element.sendKeys(value);
            System.out.println("Successfully entered value in text box [" + element + "]");
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while entering value in text box [" + element + "], Exception is : "+e);
            return false;
        }
    }

    /**
     * This method will return a Properties file object that helps to load properties of a Properties file by passing
     * the file path of the Properties file. If the file path is invalid, then it returns null.
     * @param filePath - Filepath of the properties file
     * @return Properties
     */
    public static Properties getPropertiesFileObject(String filePath) {
        try {
            FileInputStream fis = null;
            Properties prop = null;
            try {
                fis = new FileInputStream(filePath);
                prop = new Properties();
                prop.load(fis);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
            return prop;
        } catch (Exception e) {
            System.out.println("Exception occurred while reading properties file : " + e);
            return null;
        }
    }

    /**
     *  This method will click on a WebElement using Fluent Wait with global config parameters
     * @param driver - WebDriver object
     * @param element - WebElement to be clicked
     * @return boolean
     */
    public static boolean clickOnWebElement(WebDriver driver, WebElement element) {
        try {
            Wait<WebDriver> fluentWait = getFluentWaitObject(driver);
            fluentWait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
            System.out.println("Successfully clicked on element [" + element + "]");
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while clicking on web element [" + element + "], "+e);
            return false;
        }
    }

    /**
     * This method will help to select a dropdown value from a dropdown. It returns false if the dropdown is not
     * present
     * @param element - WebElement of the dropdown
     * @param dropdownValue - Value to be selected from the dropdown
     * @return boolean
     */

    public static boolean selectDropdownValue(WebElement element, String dropdownValue) {
        try {
            System.out.println("Selecting dropdown value : "+dropdownValue);
            Select dropdown = new Select(element);
            dropdown.selectByValue(dropdownValue);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while selecting dropdown value [" + dropdownValue + "] " + e);
            return false;
        }
    }

    /**
     * This method will help to select a dropdown value from a dropdown. It returns false if the dropdown is not
     * present
     * @param element - Dropdown WebElement
     * @param dropdownValue - Dropdown value to be selected
     * @return boolean
     */

    public static boolean selectDropdownByVisibleText(WebElement element, String dropdownValue) {
        try {
            System.out.println("Selecting dropdown value : "+dropdownValue);
            Select dropdown = new Select(element);
            dropdown.selectByVisibleText(dropdownValue);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while selecting dropdown value [" + dropdownValue + "] " + e);
            return false;
        }
    }

    /**
     * This method will generate a Alphabetic String of a maximum length provided as parameter
     * @param length - the intended length of the random String
     * @return String
     */

    public static String generateRandomAlphabaticString(int length) {
        try {
            return RandomStringUtils.randomAlphabetic(length);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method will generate a random integer within a range of 0 to the max length passed as parameter
     * @param max - the max length of the integer.
     * @return String
     */
    public static int getRandomIntegerBetweenZeroAndGivenMaxInteger(int max) {
        try {
            Random rnd = new Random();
            return rnd.nextInt(max);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * This method will take a full snanpshot of the current web page and attach with the current Cucumber Scenario
     * @param driver - Webdriver object
     * @param scenario - Current Scenario Object
     */
    public static void takeScreenshot(WebDriver driver, Scenario scenario) {
        try {
            System.out.println("Taking screenshot");
            // Take a screenshot...
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            // embed it in the report.
            scenario.attach(screenshot, "image/png", "Screenshot");
        } catch (Exception e) {
            System.out.println("Exception occurred while taking screenshot : " + e);
        }
    }

    /**
     * This method will generate a Alphanumeric String of a maximum length provided as parameter
     * @param length - the intended length of the random String
     * @return String
     */

    public static String generateRandomAlphnumericString(int length) {
        try {
            return RandomStringUtils.randomAlphanumeric(length);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method will create a folder/directory that doesn't exist
     * @param directoryPath - the directory path that needs to be created
     */
    public static void createDirectoryIfNotExists(String directoryPath) {
        try {
            File theDir = new File(directoryPath);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
        } catch (Exception e) {
            System.out.println("Unable to create directory [" + directoryPath + "] : " + e);

        }
    }

    /**
     * This method will return today's date and time in the given format
     * @return String
     */

    public static String getTodaysDateAndTime(String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = new Date();
            return formatter.format(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * This method will click on the WebElement using JavaScript Executor
     * @param driver
     * @param element
     * @return boolean
     */

    public static boolean javaScriptExecutorClick(WebDriver driver,WebElement element) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", element);
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while clicking element using JavaScript : " + e);
            return false;

        }
    }

    /**
     * This method will perform web page scroll by the pixel given using JavaScript Executor
     * @param driver
     * @param pixel
     * @return boolean
     */
    public static boolean javaScriptExecutorScrollDown(WebDriver driver,int pixel) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,"+pixel+")");
            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred while scrolling down the webpage : " + e);
            return false;

        }
    }

}
