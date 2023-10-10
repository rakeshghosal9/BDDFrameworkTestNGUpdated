package org.example.pageObjects;

import org.example.reusablemethods.ReusableCommonMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TechlisticDemoAutomationPage {

    public WebDriver driver;
    @FindBy(name = "firstname")
    WebElement firstName;
    @FindBy(name = "lastname")
    WebElement lastName;
    @FindBy(xpath = "//input[@id='sex-0'][@value='Male']")
    WebElement gender;
    @FindBy(xpath = "//input[@id='exp-0'][@value='1']")
    WebElement yearOfExperience;

    public TechlisticDemoAutomationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean enterAutomationDemoDetails() {
        ReusableCommonMethods.waitForElementToBeVisible(firstName, driver, 30);
        firstName.sendKeys("Simon");
        firstName.sendKeys("L");
        gender.click();
        yearOfExperience.click();
        return true;
    }
}