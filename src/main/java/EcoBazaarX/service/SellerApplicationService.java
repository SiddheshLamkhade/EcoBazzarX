package EcoBazaarX.service;

import EcoBazaarX.dto.RegisterRequest;
import EcoBazaarX.dto.ReviewSellerApplicationRequest;
import EcoBazaarX.dto.SellerApplicationResponse;
import EcoBazaarX.entity.SellerApplication;
import EcoBazaarX.entity.User;
import EcoBazaarX.repository.SellerApplicationRepository;
import EcoBazaarX.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SellerApplicationService {

    @Autowired
    private SellerApplicationRepository sellerApplicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public SellerApplication createPendingApplication(User user, RegisterRequest request) {
        SellerApplication application = sellerApplicationRepository.findByUserId(user.getId())
            .orElseGet(SellerApplication::new);
        application.setUser(user);
        application.setBusinessName(request.getBusinessName());
        application.setBusinessType(request.getBusinessType());
        application.setGstNumber(request.getGstNumber());
        application.setWebsite(request.getWebsite());
        application.setDescription(request.getDescription());
        if (request.getDocuments() != null) {
            application.setDocuments(new ArrayList<>(request.getDocuments()));
        } else {
            application.setDocuments(new ArrayList<>());
        }
        application.setStatus(SellerApplication.Status.PENDING);
        application.setReviewNotes(null);
        application.setReviewedAt(null);
        return sellerApplicationRepository.save(application);
    }

    public List<SellerApplicationResponse> getAllApplications() {
        return sellerApplicationRepository.findAllByOrderByAppliedAtDesc()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public SellerApplicationResponse reviewApplication(Long applicationId, ReviewSellerApplicationRequest request) {
        SellerApplication application = sellerApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("Application not found"));

        SellerApplication.Status decision;
        try {
            if (request.getDecision() == null) {
                throw new IllegalArgumentException("Decision is required");
            }
            decision = SellerApplication.Status.valueOf(request.getDecision().toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid decision value. Use APPROVED or REJECTED.");
        }

        application.setStatus(decision);
        application.setReviewNotes(request.getNotes());
        application.setReviewedAt(LocalDateTime.now());

        User user = application.getUser();
        if (decision == SellerApplication.Status.APPROVED) {
            user.setSellerStatus(User.SellerStatus.APPROVED);
        } else if (decision == SellerApplication.Status.REJECTED) {
            user.setSellerStatus(User.SellerStatus.REJECTED);
        }

        userRepository.save(user);
        sellerApplicationRepository.save(application);
        return mapToResponse(application);
    }

    private SellerApplicationResponse mapToResponse(SellerApplication application) {
        String name = (application.getUser().getFirstName() != null ? application.getUser().getFirstName() : "")
            + " "
            + (application.getUser().getLastName() != null ? application.getUser().getLastName() : "");
        name = name.trim().isEmpty() ? application.getUser().getUsername() : name.trim();

        return new SellerApplicationResponse(
            application.getId(),
            application.getUser().getId(),
            name,
                application.getUser().getEmail(),
                application.getBusinessName(),
                application.getBusinessType(),
                application.getGstNumber(),
                application.getDocuments() != null ? application.getDocuments() : Collections.emptyList(),
                application.getStatus().name(),
                application.getAppliedAt(),
                application.getReviewedAt(),
                application.getReviewNotes()
        );
    }
}
