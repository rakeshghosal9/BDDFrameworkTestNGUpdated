package org.example.pageObjects;

import org.openqa.selenium.WebDriver;

public class PageObjectManager {

    public LoginPage loginPage;
    public HomePage homePage;
    public ForgotPasswordPage forgotPasswordPage;
    public TechlisticDemoAutomationPage techlisticDemoAutomationPage;
    public StockAnalysisPage stockAnalysisPage;
    public WebDriver driver;

    public PageObjectManager(WebDriver driver) {
        this.driver = driver;
    }

    public LoginPage getLoginPage() {
        loginPage = new LoginPage(driver);
        return loginPage;
    }
    public HomePage getHomePage() {
        homePage = new HomePage(driver);
        return homePage;
    }
    public ForgotPasswordPage getForgotPasswordPage() {
        forgotPasswordPage = new ForgotPasswordPage(driver);
        return forgotPasswordPage;
    }
    public TechlisticDemoAutomationPage getTechlisticDemoAutomationPage() {
        techlisticDemoAutomationPage = new TechlisticDemoAutomationPage(driver);
        return techlisticDemoAutomationPage;
    }

    public StockAnalysisPage getStockAnalysisPage() {
        stockAnalysisPage = new StockAnalysisPage(driver);
        return stockAnalysisPage;
    }


}


