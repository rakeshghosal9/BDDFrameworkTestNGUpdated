package org.example.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GenerateHTMLReport {

    public static List<HashMap<String,String>> failedScenarioDetails = new ArrayList<>();
    public static boolean failureFlag = false;

    public static void main(String args[]) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object object = parser.parse(new FileReader(System.getProperty("user.dir") + "\\target\\cucumber-reports\\Cucumber.json"));
        JSONArray rootArray = (JSONArray) object;
        HashMap<String, HashMap<String, Integer>> summaryMap = new HashMap<>();
        //System.out.println(rootArray.toString());
        for (int featureID = 0; featureID < rootArray.size(); featureID++) {
            JSONObject featureObject = (JSONObject) rootArray.get(featureID);
            String featureName = featureObject.get("name").toString();
            JSONArray elementObject = (JSONArray) featureObject.get("elements");
            for (int scenarioID = 0; scenarioID < elementObject.size(); ) {
                boolean backgroundValidationStatus = true;
                boolean scenarioStatus = true;
                boolean finalValidationStatus = true;
                JSONObject scenarioObject = (JSONObject) elementObject.get(scenarioID);
                String type = scenarioObject.get("type").toString();
                String scenarioName = scenarioObject.get("name").toString();
                if (type != null && type.equalsIgnoreCase("background")) {
                    backgroundValidationStatus = getScenarioStatus((JSONArray) scenarioObject.get("steps"),featureName,scenarioName);
                    scenarioObject = (JSONObject) elementObject.get((scenarioID + 1));
                    scenarioStatus = getScenarioStatus((JSONArray) scenarioObject.get("steps"),featureName,scenarioName);
                    scenarioID = scenarioID + 2;
                } else {
                    scenarioStatus = getScenarioStatus((JSONArray) scenarioObject.get("steps"),featureName,scenarioName);
                    scenarioID = scenarioID + 1;
                }
                finalValidationStatus = backgroundValidationStatus && scenarioStatus;
                //check if feature name already exists in the map
                if (summaryMap.get(featureName) != null) {
                    int passCount = summaryMap.get(featureName).get("pass");
                    int failCount = summaryMap.get(featureName).get("fail");
                    if (finalValidationStatus) {
                        passCount = passCount + 1;
                    } else {
                        failCount = failCount + 1;
                    }
                    HashMap<String, Integer> temp = new HashMap<>();
                    temp.put("pass", passCount);
                    temp.put("fail", failCount);
                    summaryMap.put(featureName, temp);
                } else {
                    HashMap<String, Integer> temp = new HashMap<>();
                    if (finalValidationStatus) {
                        temp.put("pass", 1);
                        temp.put("fail", 0);
                    } else {
                        temp.put("pass", 0);
                        temp.put("fail", 1);
                    }
                    summaryMap.put(featureName, temp);
                }
            }
        }
        List<HashMap<String, String>> consolidatedDataFroReport = getReportData(summaryMap);
        generateHTMLReport(consolidatedDataFroReport);
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();
        // Define the format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Format the current date and time
        String executionDateTime = now.format(formatter);
        int totalScenario = Integer.parseInt(consolidatedDataFroReport.get
                (consolidatedDataFroReport.size()-1).get("total_scenario"));
        int totalPass = Integer.parseInt(consolidatedDataFroReport.get
                (consolidatedDataFroReport.size()-1).get("pass"));
        int totalFail = Integer.parseInt(consolidatedDataFroReport.get
                (consolidatedDataFroReport.size()-1).get("fail"));
        float passPercentage = Float.parseFloat(consolidatedDataFroReport.get
                (consolidatedDataFroReport.size()-1).get("pass_percentage"));
        float failPercentage = Float.parseFloat(consolidatedDataFroReport.get
                (consolidatedDataFroReport.size()-1).get("fail_percentage"));
        insertExecutionSummary(executionDateTime,totalScenario,totalPass,totalFail,passPercentage,failPercentage);
        //System.out.println(failedScenarioDetails);
    }


    public static boolean getScenarioStatus(JSONArray stepArray,String featureName,String scenarioName) {
        try {
            for (int i = 0; i < stepArray.size(); i++) {
                JSONObject stepObject = (JSONObject) stepArray.get(i);
                JSONObject resultObject = (JSONObject) stepObject.get("result");
                if (!(resultObject.get("status").toString().equalsIgnoreCase("passed"))) {
                    HashMap<String,String> temp = new HashMap<>();
                    temp.put("Feature_Name",featureName);
                    temp.put("Scenario_Name",scenarioName);
                    temp.put("Error_Message",getErrorMessage(resultObject.get("error_message").toString()));
                    failedScenarioDetails.add(temp);
                    failureFlag = true;
                    return false;
                }
            }
            return true;

        } catch (Exception e) {
            System.out.println("Exception Occurred while getting scenario status : " + e);
            return false;
        }
    }

    public static List<HashMap<String, String>> getReportData(HashMap<String, HashMap<String, Integer>> summaryMap) {
        try {
            List<HashMap<String, String>> consolidatedDataFroReport = new ArrayList<>();
            int totalScenarioLastRow = 0;
            int totalPassLastRow = 0;
            int totalFailLastRow = 0;

            for (String featureName : summaryMap.keySet()) {
                HashMap<String, String> tempValue = new HashMap<>();
                int pasCount = summaryMap.get(featureName).get("pass");
                int failCount = summaryMap.get(featureName).get("fail");
                int totalScenario = pasCount + failCount;
                double passPercentage = ((double) pasCount / (double) totalScenario) * 100;
                double failPercentage = ((double) failCount / (double) totalScenario) * 100;
                tempValue.put("feature_name", featureName);
                tempValue.put("total_scenario", "" + totalScenario);
                tempValue.put("pass", "" + pasCount);
                tempValue.put("fail", "" + failCount);
                tempValue.put("pass_percentage", String.format("%.2f", passPercentage));
                tempValue.put("fail_percentage", String.format("%.2f", failPercentage));
                consolidatedDataFroReport.add(tempValue);
                totalScenarioLastRow = totalScenarioLastRow + totalScenario;
                totalPassLastRow = totalPassLastRow + pasCount;
                totalFailLastRow = totalFailLastRow + failCount;
            }

            double passPercentageLastRow = ((double) totalPassLastRow / (double) totalScenarioLastRow) * 100;
            double failPercentageLastRow = ((double) totalFailLastRow / (double) totalScenarioLastRow) * 100;
            HashMap<String, String> tempValue = new HashMap<>();
            tempValue.put("feature_name", "Total");
            tempValue.put("total_scenario", "" + totalScenarioLastRow);
            tempValue.put("pass", "" + totalPassLastRow);
            tempValue.put("fail", "" + totalFailLastRow);
            tempValue.put("pass_percentage", String.format("%.2f", passPercentageLastRow));
            tempValue.put("fail_percentage", String.format("%.2f", failPercentageLastRow));
            consolidatedDataFroReport.add(tempValue);
            return consolidatedDataFroReport;

        } catch (Exception e) {
            System.out.println("Exception Occurred while getting the summary report : " + e);
            return null;
        }

    }

    public static void generateHTMLReport(List<HashMap<String, String>> consolidatedDataFroReport) {
        try {
            String htmlReportString = "<html>\n" +
                    "<head>\n" +
                    "\t<style>\n" +
                    "\ttable,\n" +
                    "\tth,\n" +
                    "\ttd {\n" +
                    "\t\tborder: 1px solid black;\n" +
                    "\t\tborder-collapse: collapse;\n" +
                    "\t}\n" +
                    "\n" +
                    "\tth {\n" +
                    "\t\ttext-align: center;\n" +
                    "\t}\n" +
                    "\t</style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\t<table style=\"width:100%\">\n" +
                    "\t <caption style=\"background-color:#000000;color:white\">Demo to generate HTML Report</caption>\n" +
                    "\t\t<caption style=\"background-color:#4287f5;color:white\">Test Automation Summary Report</caption>\n" +
                    "\t\t<tr>\n" +
                    "\t\t\t<th>Feature Name</th>\n" +
                    "\t\t\t<th>Total Scenario</th>\n" +
                    "\t\t\t<th>Pass</th>\n" +
                    "\t\t\t<th>Fail</th>\n" +
                    "\t\t\t<th>Pass Percentage</th>\n" +
                    "\t\t\t<th>Fail Percentage</th>\t\n" +
                    "\t\t</tr>";

            for (int i = 0; i < consolidatedDataFroReport.size(); i++) {
                htmlReportString = htmlReportString + "<tr>\n" +
                        "\t\t    <td>" + consolidatedDataFroReport.get(i).get("feature_name") + "</td>\n" +
                        "\t\t\t<td>" + consolidatedDataFroReport.get(i).get("total_scenario") + "</td>\n" +
                        "\t\t\t<td>" + consolidatedDataFroReport.get(i).get("pass") + "</td>\n" +
                        "\t\t\t<td>" + consolidatedDataFroReport.get(i).get("fail") + "</td>\n" +
                        "\t\t\t<td>" + consolidatedDataFroReport.get(i).get("pass_percentage") + "</td>\n" +
                        "\t\t\t<td>" + consolidatedDataFroReport.get(i).get("fail_percentage") + "</td>\n" +
                        "\t\t</tr>";
            }
            htmlReportString = htmlReportString + "\t</table>";

            if(failureFlag == true)
            {
                htmlReportString = htmlReportString + "<br></br>\n" +
                        "\t<table style=\"width:100%\">\n" +
                        "\t<caption style=\"background-color:#4287f5;color:white\">Failed Scenario Details</caption>\n" +
                        "\t<tr>\n" +
                        "\t\t\t<th>Feature Name</th>\n" +
                        "\t\t\t<th>Scenario Name</th>\n" +
                        "\t\t\t<th>Failure Reason</th>\t\n" +
                        "\t</tr>";
                for(HashMap<String,String> failedScenario : failedScenarioDetails)
                {
                    htmlReportString = htmlReportString + "<tr>\n" +
                            "\t\t    <td>"+failedScenario.get("Feature_Name")+"</td>\n" +
                            "\t\t\t<td>"+failedScenario.get("Scenario_Name")+"</td>\n" +
                            "\t\t\t<td>"+failedScenario.get("Error_Message")+"</td>\n" +
                            "\t</tr>";
                }
                htmlReportString = htmlReportString + "</table>\n";
            }

            htmlReportString = htmlReportString +
                    "</body>\n" +
                    "</html>";

            //Generate Report
            try {
                Writer fileWriter = new FileWriter(new File(System.getProperty("user.dir") + "/target/htmlReport.html"));
                fileWriter.write(htmlReportString);
                fileWriter.close();
                System.out.println("Report Generated Successfully");

            } catch (Exception e) {
                System.out.println("Exception Occurred while generating the report : " + e);
            }

        } catch (Exception e) {
            System.out.println("Exception Occurred while generating the report string : " + e);
        }
    }

    public static String getErrorMessage(String errorMessage) {
        try {
            int startIndex = errorMessage.indexOf(":");
            int endIndex = errorMessage.indexOf("at");
            String errorMessageCalculated = errorMessage.substring((startIndex+1), endIndex);
            return errorMessageCalculated;
        } catch (Exception e) {
            return "Error Message not found";
        }
    }

    public static void insertExecutionSummary(String executionTime,int totalScenario, int totalPass,
                                              int totalFail, double passPercentage,double failPercentage) {
        // Database connection parameters
        String jdbcURL = "jdbc:mysql://localhost:3306/automationstat";
        String username = "root";
        String password = "Mysql@2024";

        // SQL INSERT statement
        String sql = "INSERT INTO automationstat.execution_summary " +
                "(Execution_Time, Total_Scenarios, Total_Pass, Total_Fail, Pass_Percentage, Fail_Percentage ) " +
                "VALUES ('" + executionTime + "', " + totalScenario + ", " + totalPass + ", " + totalFail + ", " + passPercentage + ", " + failPercentage + ")";
        try {
            // Establish connection to the database
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            // Create a PreparedStatement for executing SQL queries
            PreparedStatement statement = connection.prepareStatement(sql);
            // Execute the SQL statement (INSERT)
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Execution Summary inserted successfully!");
            }
            // Close the statement and the connection
            statement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
