package org.example.pageObjects;

import org.example.reusablemethods.ReusableCommonMethods;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    public WebDriver driver;
    @FindBy(xpath = "//*[@id='app']/div[1]/div[1]/header/div[1]/div[1]/span/h6")
    WebElement homePageUserName;
    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    public String getHomePageText() {
        ReusableCommonMethods.waitForElementToBeVisible(homePageUserName,driver,30);
        return homePageUserName.getText();
    }
}