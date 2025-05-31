package au.com.telstra.simcardactivator.controller;

import au.com.telstra.simcardactivator.model.ActivationRequest;
import au.com.telstra.simcardactivator.service.SimCardActivationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sim")
public class SimCardActivationController {

    private static final Logger logger = LoggerFactory.getLogger(SimCardActivationController.class);
    private final SimCardActivationService activationService;

    @Autowired
    public SimCardActivationController(SimCardActivationService activationService) {
        this.activationService = activationService;
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateSim(@RequestBody ActivationRequest request) {
        logger.info("Received activation request for ICCID: {}, customer: {}", 
                request.getIccid(), request.getCustomerEmail());
        
        // Validate the request
        if (request.getIccid() == null || request.getIccid().trim().isEmpty()) {
            logger.error("Invalid request: ICCID is required");
            return ResponseEntity.badRequest().body("ICCID is required");
        }
        
        if (request.getCustomerEmail() == null || request.getCustomerEmail().trim().isEmpty()) {
            logger.error("Invalid request: Customer email is required");
            return ResponseEntity.badRequest().body("Customer email is required");
        }
        
        // Process the activation
        boolean activationSuccess = activationService.activateSimCard(
                request.getIccid(), request.getCustomerEmail());
        
        // Log the result
        logger.info("Activation result for ICCID {}: {}", 
                request.getIccid(), activationSuccess ? "SUCCESS" : "FAILURE");
        
        // Return the response
        if (activationSuccess) {
            return ResponseEntity.ok("SIM card activation successful");
        } else {
            return ResponseEntity.ok("SIM card activation failed");
        }
    }
}
