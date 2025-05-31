package au.com.telstra.simcardactivator.model;

public class ActuatorRequest {
    private String iccid;

    public ActuatorRequest() {
    }

    public ActuatorRequest(String iccid) {
        this.iccid = iccid;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }
}
