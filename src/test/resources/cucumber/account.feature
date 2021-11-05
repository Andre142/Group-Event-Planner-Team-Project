Feature: Account
  Scenario: Get to Account Page
    Given I am logged in
    And I click profile
    Then I should be taken to the account page