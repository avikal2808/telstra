Feature: SIM Card Activation and Persistence
  As a telecom service provider
  I want to activate SIM cards and persist the activation records
  So that I can track activation status and customer information

  Scenario: Successful SIM Card Activation
    Given I have a SIM card with ICCID "1255789453849037777" and customer email "customer@example.com"
    When I submit a request to activate the SIM card
    Then the activation should be successful
    And when I query the database for record with ID 1
    Then the record should show the SIM card is active

  Scenario: Failed SIM Card Activation
    Given I have a SIM card with ICCID "8944500102198304826" and customer email "customer@example.com"
    When I submit a request to activate the SIM card
    Then the activation should fail
    And when I query the database for record with ID 2
    Then the record should show the SIM card is not active