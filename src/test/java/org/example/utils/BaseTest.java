package org.example.utils;

import org.example.reusablemethods.ReusableCommonMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;

import java.util.Properties;

public class BaseTest {

    public WebDriver driver;

    public WebDriver WebDriverManager() {

        String DEFAULT_ENVIRONMENT = "ORANGEHRM";
        String URL = null;
        Properties prop;
        //Validate if environment parameter is not passed from maven command line
        if (System.getProperty("env") != null && !System.getProperty("env").trim().isEmpty()) {
            //Take the environment passed from the maven command line
            String filePath = System.getProperty("user.dir") + "//src//test//resources//environments//" +
                    System.getProperty("env").trim() + ".properties";
            prop = ReusableCommonMethods.getPropertiesFileObject(filePath);
            if (prop != null) {
                URL = prop.getProperty("url");
                System.out.println("URL Fetched : " + URL);
            } else {
                Assert.fail("No Properties file has been created under src/test/resources/environments folder " +
                        "in the name of environment provided [" + System.getProperty("env").trim() + "] from " +
                        "maven command line");
            }

        } else {
            //take the default environment to read the properties file
            String filePath = System.getProperty("user.dir") + "//src//test//resources//environments//" +
                    DEFAULT_ENVIRONMENT + ".properties";
            prop = ReusableCommonMethods.getPropertiesFileObject(filePath);
            if (prop != null) {
                URL = prop.getProperty("url");
                System.out.println("URL Fetched : " + URL);
            } else {
                Assert.fail("No Properties file has been created under src/test/resources/environments folder " +
                        "in the name of DEFAULT ENVIRONMENT provided [" + DEFAULT_ENVIRONMENT + "]");
            }
        }

        if (driver == null) {
            if (prop.getProperty("browser").equalsIgnoreCase("chrome")) {
                //WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
            } else if (prop.getProperty("browser").equalsIgnoreCase("firefox")) {
                //WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else if (prop.getProperty("browser").equalsIgnoreCase("edge")) {
                //WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
            } else {
                System.out.println("No Browser is provided");
            }
            //driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
            driver.manage().window().maximize();
            driver.get(URL);

        }
        return driver;
    }
}
