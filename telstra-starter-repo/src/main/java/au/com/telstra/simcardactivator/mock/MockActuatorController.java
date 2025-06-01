package au.com.telstra.simcardactivator.mock;

import au.com.telstra.simcardactivator.model.ActuatorRequest;
import au.com.telstra.simcardactivator.model.ActuatorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Mock controller to simulate the SimCardActuator service
 * This is for testing purposes only
 */
@RestController
public class MockActuatorController {
    
    private static final Logger logger = LoggerFactory.getLogger(MockActuatorController.class);
    
    @PostMapping("/actuate")
    public ActuatorResponse actuate(@RequestBody ActuatorRequest request) {
        logger.info("Mock actuator received request for ICCID: {}", request.getIccid());
        
        // Simple logic: activate if ICCID starts with '8931'
        boolean success = request.getIccid().startsWith("8931");
        
        logger.info("Mock actuator returning success={} for ICCID: {}", success, request.getIccid());
        return new ActuatorResponse(success);
    }
}
