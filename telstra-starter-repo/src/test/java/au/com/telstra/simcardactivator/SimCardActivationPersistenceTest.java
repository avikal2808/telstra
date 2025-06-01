package au.com.telstra.simcardactivator;

import au.com.telstra.simcardactivator.model.SimCardActivation;
import au.com.telstra.simcardactivator.repository.SimCardActivationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SimCardActivationPersistenceTest {

    @Autowired
    private SimCardActivationRepository repository;

    @Test
    public void testSimCardActivationPersistence() {
        // Arrange
        String iccid = "89314404000055555555";
        String email = "test@example.com";
        boolean active = true;
        
        // Create a new activation record
        SimCardActivation activation = new SimCardActivation(iccid, email, active);
        
        // Act
        SimCardActivation savedActivation = repository.save(activation);
        
        // Assert
        assertNotNull(savedActivation.getId());
        
        // Verify retrieval
        Optional<SimCardActivation> retrievedActivation = repository.findById(savedActivation.getId());
        assertTrue(retrievedActivation.isPresent());
        assertEquals(iccid, retrievedActivation.get().getIccid());
        assertEquals(email, retrievedActivation.get().getCustomerEmail());
        assertEquals(active, retrievedActivation.get().isActive());
        
        System.out.println("Test passed: SimCardActivation record was successfully persisted and retrieved!");
        System.out.println("Record ID: " + savedActivation.getId());
        System.out.println("ICCID: " + retrievedActivation.get().getIccid());
        System.out.println("Customer Email: " + retrievedActivation.get().getCustomerEmail());
        System.out.println("Active: " + retrievedActivation.get().isActive());
    }
    
    @TestConfiguration
    static class TestConfig {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }
}
