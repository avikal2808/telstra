package au.com.telstra.simcardactivator.service;

import au.com.telstra.simcardactivator.model.ActuatorRequest;
import au.com.telstra.simcardactivator.model.ActuatorResponse;
import au.com.telstra.simcardactivator.model.SimCardActivation;
import au.com.telstra.simcardactivator.repository.SimCardActivationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final SimCardActivationRepository simCardActivationRepository;

    @Autowired
    public SimCardActivationService(RestTemplate restTemplate, SimCardActivationRepository simCardActivationRepository) {
        this.restTemplate = restTemplate;
        this.simCardActivationRepository = simCardActivationRepository;
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
            boolean success = false;
            if (response != null) {
                success = response.isSuccess();
                logger.info("SIM card activation {} for ICCID: {}", success ? "successful" : "failed", iccid);
            } else {
                logger.error("Null response received from actuator for ICCID: {}", iccid);
            }
            
            // Save activation record to database
            SimCardActivation activation = new SimCardActivation(iccid, customerEmail, success);
            simCardActivationRepository.save(activation);
            logger.info("Saved activation record to database for ICCID: {} and email: {}", iccid, customerEmail);
            
            return success;
        } catch (Exception e) {
            logger.error("Error while calling actuator service: {}", e.getMessage(), e);
            
            // Save failed activation record to database
            SimCardActivation activation = new SimCardActivation(iccid, customerEmail, false);
            simCardActivationRepository.save(activation);
            logger.info("Saved failed activation record to database for ICCID: {} and email: {}", iccid, customerEmail);
            
            return false;
        }
    }
    
    public SimCardActivation getSimCardActivationById(Long id) {
        logger.info("Retrieving SIM card activation record with ID: {}", id);
        return simCardActivationRepository.findById(id).orElse(null);
    }
}
