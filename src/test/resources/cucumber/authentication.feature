#Feature: Hello
#  Scenario: Open hello page from index page
#    Given I am on the index page
#    When I click the link 'Click me'
#    Then I should see header 'Hello world!'
#
#  Scenario: Open other page from index page
#    Given I am on the index page
#    When I click the link 'Or click me'
#    Then I should see text 'Hello servlet'
Feature: Authentication

  Scenario: Signup New Account
    Given I am on the signup page
    And I fill out my credentials
    And I click on the button at the bottom of the form
    Then I should see no errors

  Scenario: Log In
    Given I am on the login page
    And I fill out my credentials
    And I click on the button at the bottom of the form
    Then I should see no errors

  Scenario:  Blank SignUp
    Given I am on the signup page
    When I click on the button at the bottom of the form
    Then I should see errors under the username and password field

  Scenario:  Blank Login
    Given I am on the login page
    When I click on the button at the bottom of the form
    Then I should see errors under the username and password field

  Scenario: Invalid SignUp -- Existing Account
    Given I am on the signup page
    And I fill out my credentials
    And I click on the button at the bottom of the form
    Then I should see an error at the top of the screen

  Scenario: Invalid Login Wrong Password
    Given I am on the login page
    And I fill out the wrong password
    And I click on the button at the bottom of the form
    Then I should see an error at the top of the screen

  Scenario: Invalid Login Wrong Username
    Given I am on the login page
    And I fill out the wrong username
    And I click on the button at the bottom of the form
    Then I should see an error at the top of the screen
