package EcoBazaarX.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SellerApplicationResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String email;
    private String businessName;
    private String businessType;
    private String gstNumber;
    private List<String> documents;
    private String status;
    private LocalDateTime appliedAt;
    private LocalDateTime reviewedAt;
    private String reviewNotes;

    public SellerApplicationResponse(Long id, Long userId, String userName, String email, String businessName,
                                     String businessType, String gstNumber, List<String> documents, String status,
                                     LocalDateTime appliedAt, LocalDateTime reviewedAt, String reviewNotes) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.businessName = businessName;
        this.businessType = businessType;
        this.gstNumber = gstNumber;
        this.documents = documents;
        this.status = status;
        this.appliedAt = appliedAt;
        this.reviewedAt = reviewedAt;
        this.reviewNotes = reviewNotes;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public String getGstNumber() {
        return gstNumber;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public String getReviewNotes() {
        return reviewNotes;
    }
}
