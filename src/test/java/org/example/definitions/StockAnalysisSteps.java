package org.example.definitions;


import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.pageObjects.HomePage;
import org.example.pageObjects.LoginPage;
import org.example.pageObjects.StockAnalysisPage;
import org.example.reusablemethods.ReusableCommonMethods;
import org.example.reusablemethods.ReusableUtilities;
import org.example.utils.TestSetUp;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockAnalysisSteps {

    TestSetUp setUp;
    public StockAnalysisPage stockAnalysisPage;

    public StockAnalysisSteps(TestSetUp setUp) throws IOException {
        this.setUp = setUp;
        this.stockAnalysisPage = setUp.pageObjectManager.getStockAnalysisPage();
    }
    List<String> allStocks = new ArrayList<String>();
    public static List<HashMap<String, String>> consolidatedData = new ArrayList<>();
    public String sheetNameGlobal;

    @When("landed on the google homepage")
    public void landed_on_the_google_homepage() {
        Assert.assertTrue(stockAnalysisPage.validateGoogleHomePageDisplayed(),"Google Homepage is not displayed");
    }


    @Then("capture all the stock statistics")
    public void capture_all_the_stock_statistics() {
        for (int i = 0; i < allStocks.size(); i++) {
            stockAnalysisPage.enterSearchValueAndClickOnEnterButton(allStocks.get(i));
            stockAnalysisPage.fetchStockAnalysis();
        }
        //System.out.println(consolidatedData);
        ReusableUtilities.writeExcelData(
                System.getProperty("user.dir") + "\\src\\test\\resources\\OtherData\\" + ReusableCommonMethods.getTodaysDateAndTime("dd_MM_yyyy_HH_mm_ss") + ".xlsx",
                consolidatedData,
                allStocks
        );
        stockAnalysisPage.writeTodaysChangeInLastColumn(consolidatedData,allStocks,sheetNameGlobal);
    }

    @Given("we read all the company name from excel {string} and sheet name {string}")
    public void we_read_all_the_company_name_from_excel_and_sheet_name(String workbookName, String sheetName) {

        sheetNameGlobal = sheetName;
        System.out.println("Reading the excel");
        XSSFWorkbook wb = ReusableUtilities.getWorkbookObject(System.getProperty("user.dir") + "\\src\\test\\resources\\OtherData\\" + workbookName + ".xlsx");
        System.out.println("Workbook : " + wb);
        XSSFSheet sheet = ReusableUtilities.getWorkbookSheet(sheetName, wb);
        System.out.println("Sheet : " + sheet);
        allStocks = ReusableUtilities.fetchValueFromGivenColumnInExcel(0, sheet);
    }

    /*@When("landed on the google homepage")
    public void landed_on_the_google_homepage_updated(DataTable table) {
        List<Map<String,String>> tableValues = table.asMaps();
        for(int i=1;i<tableValues.size();i++)
        {
            System.out.println(tableValues.get(i).get("reason"));
        }

    }*/
}
