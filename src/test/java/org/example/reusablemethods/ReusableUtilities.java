package org.example.reusablemethods;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReusableUtilities {

    public static double sub(double minuend, double subtrahend) {
        BigDecimal b1 = new BigDecimal(Double.toString(minuend));
        BigDecimal b2 = new BigDecimal(Double.toString(subtrahend));
        return b1.subtract(b2).doubleValue();
    }

    public static XSSFWorkbook getWorkbookObject(String filePath) {
        try {
            System.out.println("Excel Filepath : "+filePath);
            FileInputStream fis = new FileInputStream(filePath);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            //fis.close();
            return wb;
        } catch (Exception e) {
            System.out.println("Exception occurred while fetching workbook object : " + e);
            return null;
        }
    }

    public static XSSFSheet getWorkbookSheet(String sheetName, XSSFWorkbook wb) {
        try {
            XSSFSheet sheet = wb.getSheet(sheetName);
            return sheet;

        } catch (Exception e) {
            System.out.println("Exception occurred while fetching sheet from the workbook : " + e);
            return null;
        }
    }

    public static XSSFSheet getWorkbookSheet(int sheetIndex, XSSFWorkbook wb) {
        try {
            XSSFSheet sheet = wb.getSheetAt(sheetIndex);
            return sheet;

        } catch (Exception e) {
            System.out.println("Exception occurred while fetching sheet from the workbook : " + e);
            return null;
        }
    }

    public static List<String> fetchValueFromGivenColumnInExcel(int columnNumber, XSSFSheet sheet)
    {
        try
        {
            int lastRowNum = sheet.getLastRowNum();
            System.out.println("Total Row : "+lastRowNum);
            List<String> valueFromColumn = new ArrayList<>(lastRowNum);
            for(int i=1;i<=lastRowNum;i++)
            {
                valueFromColumn.add(sheet.getRow(i).getCell(columnNumber).getStringCellValue());
            }
            System.out.println(valueFromColumn);
            return valueFromColumn;
        }catch (Exception e)
        {
            System.out.println("Exception occurred while reading the excel : "+e);
            return new ArrayList<>();
        }
    }

    public static void writeExcelData(String filePath, List<HashMap<String,String>> consolidatedData, List<String> stockName)
    {
        try
        {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Stock Analysis");
            //Set Header
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("Stock Name");
            row.createCell(1).setCellValue("Current Index");
            row.createCell(2).setCellValue("Today's Change");
            row.createCell(3).setCellValue("52 Week High");
            row.createCell(4).setCellValue("52 Week Low");
            row.createCell(5).setCellValue("High Minus Current Index");
            row.createCell(6).setCellValue("Current Index Minus Low Index");

            for(int i=0;i<consolidatedData.size();i++)
            {
                row = sheet.createRow((i+1));
                row.createCell(0).setCellValue(stockName.get(i));
                row.createCell(1).setCellValue(convertDouble(consolidatedData.get(i).get("CUR_INDEX")));
                row.createCell(2).setCellValue(consolidatedData.get(i).get("TODAYS_CHANGE"));
                row.createCell(3).setCellValue(convertDouble(consolidatedData.get(i).get("WEEK_HIGH")));
                row.createCell(4).setCellValue(convertDouble(consolidatedData.get(i).get("WEEK_LOW")));
                row.createCell(5).setCellValue(convertDouble(consolidatedData.get(i).get("HIGH_MINUS_CURRENT_INDEX")));
                row.createCell(6).setCellValue(convertDouble(consolidatedData.get(i).get("CURRENT_INDEX_MINUS_LOW")));
            }

            FileOutputStream outputStream = new FileOutputStream(filePath);
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();

        }catch (Exception e)
        {
            System.out.println("Exception occurred : "+e);
        }
    }

    public static double convertDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return Double.valueOf(value);
        }

    }

}
