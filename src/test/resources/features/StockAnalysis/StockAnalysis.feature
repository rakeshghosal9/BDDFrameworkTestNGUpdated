@STOCK_ANALYSIS
Feature: Stock Analysis 1

  Scenario: Stock Analysis of given stocks
    Given we read all the company name from excel "StockName" and sheet name "Nifty_50"
    When landed on the google homepage
    Then capture all the stock statistics


