package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardActivator;
import au.com.telstra.simcardactivator.model.ActivationRequest;
import au.com.telstra.simcardactivator.model.SimCardActivationDTO;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.cucumber.spring.CucumberContextConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration(classes = SimCardActivator.class, loader = SpringBootContextLoader.class)
public class SimCardActivatorStepDefinitions {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private String iccid;
    private String customerEmail;
    private boolean activationResult;
    private SimCardActivationDTO queryResult;
    
    @Given("I have a SIM card with ICCID {string} and customer email {string}")
    public void iHaveASimCardWithIccidAndCustomerEmail(String iccid, String customerEmail) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
        System.out.println("Given a SIM card with ICCID: " + iccid + " and email: " + customerEmail);
    }
    
    @When("I submit a request to activate the SIM card")
    public void iSubmitARequestToActivateTheSimCard() {
        // Create activation request
        ActivationRequest request = new ActivationRequest();
        request.setIccid(iccid);
        request.setCustomerEmail(customerEmail);
        
        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ActivationRequest> entity = new HttpEntity<>(request, headers);
        
        // Send request to activation endpoint
        System.out.println("Submitting activation request for ICCID: " + iccid);
        ResponseEntity<Boolean> response = restTemplate.postForEntity(
                "http://localhost:8080/api/sim/activate",
                entity,
                Boolean.class);
        
        // Safely handle null response body
        Boolean result = response.getBody();
        activationResult = result != null && result;
        System.out.println("Activation result: " + activationResult);
    }
    
    @Then("the activation should be successful")
    public void theActivationShouldBeSuccessful() {
        assertEquals(true, activationResult, "SIM card activation should be successful");
        System.out.println("Verified activation was successful");
    }
    
    @Then("the activation should fail")
    public void theActivationShouldFail() {
        assertEquals(false, activationResult, "SIM card activation should fail");
        System.out.println("Verified activation failed as expected");
    }
    
    @And("when I query the database for record with ID {int}")
    public void whenIQueryTheDatabaseForRecordWithId(int id) {
        // Query the database for the record
        System.out.println("Querying database for record with ID: " + id);
        ResponseEntity<SimCardActivationDTO> response = restTemplate.getForEntity(
                "http://localhost:8080/api/sim/query?simCardId=" + id,
                SimCardActivationDTO.class);
        
        queryResult = response.getBody();
        assertNotNull(queryResult, "Query result should not be null");
        System.out.println("Retrieved record: ICCID=" + queryResult.getIccid() + 
                ", Email=" + queryResult.getCustomerEmail() + 
                ", Active=" + queryResult.isActive());
    }
    
    @Then("the record should show the SIM card is active")
    public void theRecordShouldShowTheSimCardIsActive() {
        assertNotNull(queryResult, "Query result should not be null");
        assertEquals(true, queryResult.isActive(), "SIM card should be marked as active");
        assertEquals(iccid, queryResult.getIccid(), "ICCID in the record should match");
        assertEquals(customerEmail, queryResult.getCustomerEmail(), "Customer email in the record should match");
        System.out.println("Verified record shows SIM card is active");
    }
    
    @Then("the record should show the SIM card is not active")
    public void theRecordShouldShowTheSimCardIsNotActive() {
        assertNotNull(queryResult, "Query result should not be null");
        assertEquals(false, queryResult.isActive(), "SIM card should be marked as not active");
        assertEquals(iccid, queryResult.getIccid(), "ICCID in the record should match");
        assertEquals(customerEmail, queryResult.getCustomerEmail(), "Customer email in the record should match");
        System.out.println("Verified record shows SIM card is not active");
    }
}