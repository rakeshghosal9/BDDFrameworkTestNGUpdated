@FEATURE_EXECUTION
Feature: Login to HRM Application

  @regression
  Scenario: Regression - Login with valid credentials

    When User enters username as "Admin" and password as "admin123"
    Then User should be able to login successfully

  @regression
  Scenario Outline: Regression Scenario - <sl_no> : Login with invalid credentials

    When User enters username as "<username>" and password as "<password>"
    Then User should be able to see error message "<errorMessage>"

    Examples:
      | sl_no | username | password  | errorMessage        |
      | 1     | Admin    | admin12$$ | Invalid credentials |
      | 2     | admin$$  | admin123  | Invalid credentials |
      | 3     | abc123   | xyz$$     | Invalid credentials |

  @smoke
  Scenario: Smoke - Login with valid credentials

    When User enters username as "Admin" and password as "admin123"
    Then User should be able to login successfully

  @smoke
  Scenario Outline: Smoke Scenario - <sl_no> : Login with invalid credentials

    When User enters username as "<username>" and password as "<password>"
    Then User should be able to see error message "<errorMessage>"

    Examples:
      | sl_no | username | password  | errorMessage        |
      | 1     | Admin    | admin12$$ | Invalid credentials |
      | 2     | admin$$  | admin123  | Invalid credentials |
      | 3     | abc123   | xyz$$     | Invalid credentials |