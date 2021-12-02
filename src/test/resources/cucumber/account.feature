Feature: Account
  Scenario: Get to Account Page
    Given I am logged in
    And I click profile
    Then I should be taken to the account page

  Scenario: Inputting unavailability
    Given I am on the profile page
    And I input a start date
    And I input an end date
    When I click on the submit button
    Then I should see the alert Unavailability has been set!