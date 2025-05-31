package au.com.telstra.simcardactivator.service;

import au.com.telstra.simcardactivator.model.ActuatorRequest;
import au.com.telstra.simcardactivator.model.ActuatorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SimCardActivationService {
    
    private static final String ACTUATOR_URL = "http://localhost:8444/actuate";
    private static final Logger logger = LoggerFactory.getLogger(SimCardActivationService.class);
    private final RestTemplate restTemplate;

    public SimCardActivationService() {
        this.restTemplate = new RestTemplate();
    }

    public boolean activateSimCard(String iccid, String customerEmail) {
        logger.info("Activating SIM card with ICCID: {} for customer: {}", iccid, customerEmail);
        
        // Create request to actuator
        ActuatorRequest request = new ActuatorRequest(iccid);
        
        // Configure headers for JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        // Create HTTP entity with request and headers
        HttpEntity<ActuatorRequest> entity = new HttpEntity<>(request, headers);
        
        try {
            // Call the actuator service
            ActuatorResponse response = restTemplate.postForObject(ACTUATOR_URL, entity, ActuatorResponse.class);
            
            // Process response
            if (response != null) {
                logger.info("SIM card activation {} for ICCID: {}", response.isSuccess() ? "successful" : "failed", iccid);
                return response.isSuccess();
            } else {
                logger.error("Null response received from actuator for ICCID: {}", iccid);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error while calling actuator service: {}", e.getMessage(), e);
            return false;
        }
    }
}
