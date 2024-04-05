package org.example.pageObjects;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.definitions.StockAnalysisSteps;
import org.example.reusablemethods.ReusableCommonMethods;
import org.example.reusablemethods.ReusableUtilities;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

public class StockAnalysisPage {

    WebDriver driver;

    @FindBy(xpath = "//*[@name='q']")
    WebElement googleSearchInputBox;
    @FindBy(xpath = "//div[@data-attrid='Price']/child::span[1]/child::span/child::span")
    WebElement currentIndex;
    @FindBy(xpath = "//div[@data-attrid='Price']/child::span[2]/child::span[1]")
    WebElement todaysChange;
    @FindBy(xpath = "//div[@data-attrid='52-wk high']")
    WebElement weekHigh52;
    @FindBy(xpath = "//div[@data-attrid='52-wk low']")
    WebElement weeklow52;

    @FindBy(xpath = "//div[@data-attrid='Chart']")
    WebElement googleSearchChart;


    public StockAnalysisPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean validateGoogleHomePageDisplayed() {
        return ReusableCommonMethods.waitForElementToBeVisible(googleSearchInputBox, driver, 60);
    }

    public boolean enterSearchValueAndClickOnEnterButton(String searchString) {
        try {
            ReusableCommonMethods.enterValueInTextBox(googleSearchInputBox, searchString, driver);
            googleSearchInputBox.sendKeys(Keys.ENTER);
            return true;

        } catch (Exception e) {

            return false;
        }
    }

    public void fetchStockAnalysis() {
        try {
            ReusableCommonMethods.waitForElementToBeVisible(googleSearchChart,driver,30);
            //HashMap<String,Double> tempData = new HashMap<>();
            HashMap<String,String> tempData = new HashMap<>();
            System.out.println("Current Index : " + currentIndex.getText().trim());
            System.out.println("Today's Change : " + todaysChange.getText().trim());
            System.out.println("52 Week High : " + weekHigh52.getText().trim());
            System.out.println("52 Week Low : " + weeklow52.getText().trim());

            tempData.put("CUR_INDEX",currentIndex.getText().trim().replaceAll(",",""));
            tempData.put("TODAYS_CHANGE",todaysChange.getText().trim().replaceAll(",",""));
            tempData.put("WEEK_HIGH",weekHigh52.getText().trim().replaceAll(",",""));
            tempData.put("WEEK_LOW",weeklow52.getText().trim().replaceAll(",",""));


            /*tempData.put("CUR_INDEX",convertDouble(currentIndex.getText().trim().replaceAll(",","")));
            tempData.put("TODAYS_CHANGE",convertDouble(todaysChange.getText().trim().replaceAll(",","")));
            tempData.put("WEEK_HIGH",convertDouble(weekHigh52.getText().trim().replaceAll(",","")));
            tempData.put("WEEK_LOW",convertDouble(weeklow52.getText().trim().replaceAll(",","")));*/
            String weekHigh52Data = weekHigh52.getText().trim();
            weekHigh52Data = weekHigh52Data.replaceAll(",","");
            double weekHighDouble = ReusableUtilities.convertDouble(weekHigh52Data);
            String weekLow52Data = weeklow52.getText().trim();
            weekLow52Data = weekLow52Data.replaceAll(",","");
            double weekLowDouble = ReusableUtilities.convertDouble(weekLow52Data);
            double currentIndexDouble = ReusableUtilities.convertDouble(currentIndex.getText().trim().replaceAll(",",""));
            tempData.put("HIGH_MINUS_CURRENT_INDEX",""+ReusableUtilities.sub(weekHighDouble,currentIndexDouble));
            tempData.put("CURRENT_INDEX_MINUS_LOW",""+ReusableUtilities.sub(currentIndexDouble,weekLowDouble));
            StockAnalysisSteps.consolidatedData.add(tempData);
        } catch (Exception e) {
            System.out.println("Exception Occurred while fetching data: " + e);
        }
    }

    public void writeTodaysChangeInLastColumn(List<HashMap<String,String>> consolidatedData, List<String> allStocks, String sheetName)
    {
        try
        {
            Workbook workbook = ReusableUtilities.getWorkbookObject(System.getProperty("user.dir")+
                    "\\src\\test\\resources\\OtherData\\StockName.xlsx");
            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(0);
            int lastColumn = row.getLastCellNum();
            String todaysDate = ReusableCommonMethods.getTodaysDateAndTime("dd/MM/yyyy");
            row.createCell(lastColumn).setCellValue(todaysDate);
            for(int i=0;i<allStocks.size();i++)
            {
                row = sheet.getRow(i+1);
                row.createCell(lastColumn).setCellValue(consolidatedData.get(i).get("TODAYS_CHANGE"));
            }
            FileOutputStream outputStream = new FileOutputStream(System.getProperty("user.dir")+
                    "\\src\\test\\resources\\OtherData\\StockName.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        }catch (Exception e)
        {
            System.out.println("Exception Occurred while updating Stockname.xlsx workbook: "+e);
        }
    }

}
