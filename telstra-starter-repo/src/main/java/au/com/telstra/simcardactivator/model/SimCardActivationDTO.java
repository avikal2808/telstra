package au.com.telstra.simcardactivator.model;

public class SimCardActivationDTO {
    private String iccid;
    private String customerEmail;
    private boolean active;
    
    public SimCardActivationDTO(String iccid, String customerEmail, boolean active) {
        this.iccid = iccid;
        this.customerEmail = customerEmail;
        this.active = active;
    }
    
    public String getIccid() {
        return iccid;
    }
    
    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}
