package com.test.test.Controller;

import com.test.test.Entity.*;
import com.test.test.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class PrimaryUserController {

    @Autowired
    private PrimaryUserService primaryUserService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ContactInformationService contactInformationService;

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    @Autowired
    private HealthInformationService healthInformationService;

    @Autowired
    private PaymentInformationService paymentInformationService;

    @Autowired
    private SecurityInformationService securityInformationService;

    @Autowired
    private DependentInformationService dependentInformationService;

    @PostMapping("/dependent-information")
    public ResponseEntity<String> saveDependentInformation(@RequestBody DependentInformation dependentInformation) {
        try {
            // Save the dependent information to the database
            dependentInformationService.saveDependentInformation(dependentInformation);
            return ResponseEntity.ok("Dependent Information saved successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to save dependent information.");
        }
    }

    // Endpoint to save security information
    @PostMapping("/security-information")
    public String saveSecurityInformation(@RequestBody SecurityInformation securityInformation) {
        // Save the security information and hash the password
        SecurityInformation savedInfo = securityInformationService.saveSecurityInformation(securityInformation);

        // Return a success message
        return "Security information saved successfully!";
    }

    @PostMapping("/payment-information")
    public ResponseEntity<String> submitPaymentInformation(@RequestBody PaymentInformation paymentInformation) {
        try {
            // Save the payment information in the database
            paymentInformationService.savePaymentInformation(paymentInformation);
            return new ResponseEntity<>("Payment information submitted successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error submitting payment information.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/health-information")
    public String submitHealthInformation(@RequestBody HealthInformation healthInformation) {
        if (healthInformationService.saveHealthInformation(healthInformation)) {
            return "Health information submitted successfully!";
        } else {
            return "Failed to submit health information.";
        }
    }

    @PostMapping("/delivery-submit")
    public ResponseEntity<String> submitAddress(@RequestBody DeliveryAddressInformation deliveryAddress) {
        boolean isSaved = deliveryAddressService.saveDeliveryAddress(deliveryAddress);

        if (isSaved) {
            return ResponseEntity.ok("Address submitted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to submit address.");
        }
    }

    @PostMapping("/contact")
    public String submitContactForm(@RequestBody ContactInformation contactInformation) {
        contactInformationService.saveContactInfo(contactInformation);
        return "Contact information saved successfully!";
    }

    private final Map<String, String> otpStore = new HashMap<>(); // To store OTPs temporarily

    @PostMapping("/users")
    public ResponseEntity<PrimaryUser> createUser(@RequestBody PrimaryUser primaryUser) {
        PrimaryUser savedUser = primaryUserService.saveUser(primaryUser);
        String membershipId = savedUser.getMembershipId();

        // Generate OTP and store it in otpStore
        String otp = generateOtp();
        otpStore.put(membershipId, otp);

        // Send OTP email
        String profileSetupLink = "http://localhost:5173/account-creation?membershipid=" + membershipId;
        String emailSubject = "Complete Your Account Setup";
        String emailBody = "Hello,\n\nYour Membership ID is: " + membershipId + "\n\n" +
                "Please use the following OTP to complete your profile setup: " + otp + "\n\n" +
                "To complete your profile setup, visit the following link:\n" +
                profileSetupLink;

        emailService.sendEmail(primaryUser.getEmail(), emailSubject, emailBody);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/account-creation")
    public ResponseEntity<String> requestOtp(@RequestParam String membershipid) {
        if (otpStore.containsKey(membershipid)) {
            return ResponseEntity.ok("OTP has been sent to your registered email.");
        }
        return ResponseEntity.badRequest().body("Invalid membership ID.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest otpRequest) {
        String membershipId = otpRequest.getMembershipId();
        String otp = otpRequest.getOtp();

        String storedOtp = otpStore.get(membershipId);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(membershipId); // Clear OTP after successful verification
            return ResponseEntity.ok("OTP verified successfully! Proceed with profile setup.");
        }
        return ResponseEntity.status(400).body("Invalid OTP. Please try again.");
    }

    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }


}

// Helper class for OTP verification request
class OtpRequest {
    private String membershipId;
    private String otp;

    // Getters and setters

    public String getMembershipId() {
        return membershipId;
    }

    public void setMembershipId(String membershipId) {
        this.membershipId = membershipId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
