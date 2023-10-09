package org.example.pageObjects;

import org.example.reusablemethods.ReusableCommonMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ForgotPasswordPage {

    WebDriver driver;
    @FindBy(xpath = "//*[@id='app']//h6[contains(@class,'orangehrm-forgot-password-title')]")
    WebElement forgotPasswordPageTitle;
    @FindBy(xpath = "//div[@class='orangehrm-forgot-password-button-container']//button[@type='button']")
    WebElement cancelBtn;
    @FindBy(xpath = "//div[@class='orangehrm-forgot-password-button-container']//button[@type='submit']")
    WebElement resetPasswordBtn;
    @FindBy(name = "username")
    WebElement userName;
    @FindBy(xpath = "//*[@id='app']//h6[contains(@class,'orangehrm-forgot-password-title')]")
    WebElement resetMessage;

    public ForgotPasswordPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // Get the Title of ForgotPage
    public String getForgotPageText() {
        ReusableCommonMethods.waitForElementToBeVisible(forgotPasswordPageTitle,driver,20);
        return forgotPasswordPageTitle.getText();
    }

    // Click Cancel Button
    public void clickOnCancelBtn() {
        ReusableCommonMethods.clickOnWebElement(driver,cancelBtn);
    }

    // Click ResetPassword Button
    public void clickOnRestPasswordBtn() {
        ReusableCommonMethods.clickOnWebElement(driver,resetPasswordBtn);
    }

    // Type username in TextBox
    public void TypeOnUsernameTextBox(String username) {
        ReusableCommonMethods.enterValueInTextBox(userName,username,driver);
    }

    // Get Message
    public String getRestMessage() {
        ReusableCommonMethods.waitForElementToBeVisible(forgotPasswordPageTitle,driver,20);
        return resetMessage.getText();
    }
}
