package EcoBazaarX.dto;

public class ReviewSellerApplicationRequest {
    private String decision;
    private String notes;

    public ReviewSellerApplicationRequest() {
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
