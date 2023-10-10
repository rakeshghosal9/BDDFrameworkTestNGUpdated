package org.example.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(tags = "@ValidCredentials",
        features = "src/test/resources/features",
        glue = "org.example.definitions",
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
                "pretty", "json:target/cucumber-reports/Cucumber.json"},
        monochrome = true)
public class CucumberRunnerTests  extends AbstractTestNGCucumberTests {

   @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();

    }
}
