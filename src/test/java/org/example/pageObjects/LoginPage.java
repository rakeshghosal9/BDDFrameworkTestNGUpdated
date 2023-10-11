package org.example.pageObjects;

import org.example.reusablemethods.ReusableCommonMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    public WebDriver driver;

    @FindBy(name = "username")
    WebElement userName;
    @FindBy(name = "password")
    WebElement passWord;
    @FindBy(xpath = "//button[@type='submit']")
    WebElement login;
    @FindBy(xpath = "(//div[@class='orangehrm-login-error']//p)[1]")
    WebElement errorMessage;
    @FindBy(xpath = "//div[@class='orangehrm-login-forgot']//p")
    WebElement forgotPasswordLink;
    @FindBy(xpath = "//*[@id='app']/div[1]/div/div[1]/div/div[2]/h5")
    WebElement loginPageTitle;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getErrorMessage() {
        ReusableCommonMethods.waitForElementToBeVisible(errorMessage, driver, 20);
        return errorMessage.getText();
    }

    public boolean login(String strUserName, String strPassword) {
        // Fill user name
        if (!ReusableCommonMethods.enterValueInTextBox(userName, strUserName, driver)) {
            System.out.println("User Name field is not displayed after waiting for 60 seconds : ");
            return false;
        }
        // Fill password
        if (!ReusableCommonMethods.enterValueInTextBox(passWord, strPassword, driver)) {
            System.out.println("Password field is not displayed after waiting for 60 seconds : ");
            return false;
        }
        // Click Login button
        if (!ReusableCommonMethods.clickOnWebElement(driver, login)) {
            System.out.println("Login button is not clicked");
            return false;
        }
        return true;
    }

    // Click on Forgot Password link
    public void clickOnForgotPasswordLink() {
        ReusableCommonMethods.clickOnWebElement(driver, forgotPasswordLink);
    }

    //Get Login Page Title
    public String getLoginPageTitle() {
        ReusableCommonMethods.waitForElementToBeVisible(loginPageTitle, driver, 20);
        return loginPageTitle.getText();
    }
}