package org.example.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {

    public WebDriver driver;

    By userName = By.name("username");
    By passWord = By.name("password");
    By login = By.xpath("//button[@type='submit']");
    By errorMessage = By.xpath("//div[@class='orangehrm-login-error']");
    By forgotPasswordLink = By.xpath("//div[@class='orangehrm-login-error']/div/div//p");
    By loginPageTitle = By.xpath("//*[@id='app']/div[1]/div/div[1]/div/div[2]/h5");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getErrorMessage() {
        return driver.findElement(errorMessage).getText();
    }

    public void login(String strUserName, String strPassword) throws InterruptedException {
        // Fill user name
        driver.findElement(userName).sendKeys(strUserName);

        // Fill password
        driver.findElement(passWord).sendKeys(strPassword);

        // Click Login button
        driver.findElement(login).click();

    }
    // Click on Forgot Password link
    public void clickOnForgotPasswordLink() {
        driver.findElement(forgotPasswordLink).click();
    }
    //Get Login Page Title
    public String getLoginPageTitle() {
        return driver.findElement(loginPageTitle).getText();
    }
}