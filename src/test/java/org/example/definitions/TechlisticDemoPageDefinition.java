package org.example.definitions;

import io.cucumber.java.en.When;
import org.example.pageObjects.TechlisticDemoAutomationPage;
import org.example.utils.TestSetUp;
import org.testng.Assert;

import java.io.IOException;

public class TechlisticDemoPageDefinition {
    TestSetUp setUp;
    public TechlisticDemoAutomationPage techlisticDemo;


    public TechlisticDemoPageDefinition(TestSetUp setUp) throws IOException {
        this.setUp = setUp;
        this.techlisticDemo = setUp.pageObjectManager.getTechlisticDemoAutomationPage();
    }

    @When("user open the techlistic demo page and enter details")
    public void openTechlisticDemoAutomationPage() {

        // login to application
        Assert.assertTrue(techlisticDemo.enterAutomationDemoDetails(),"Techlistic Demo Automation not " +
                "Successful");
    }

}