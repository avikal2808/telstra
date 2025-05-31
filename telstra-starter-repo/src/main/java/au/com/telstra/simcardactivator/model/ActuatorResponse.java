package au.com.telstra.simcardactivator.model;

public class ActuatorResponse {
    private boolean success;

    public ActuatorResponse() {
    }

    public ActuatorResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
