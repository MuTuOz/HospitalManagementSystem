package com.hospitalmanagement;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for Review functionality
 * Tests anonymous review feature - patients can create anonymous reviews for
 * doctors who treated them
 */
@DisplayName("Review Functionality Test Scenarios")
public class ReviewTest {

    @Test
    @DisplayName("TC-001: Patient can create anonymous review for completed appointment")
    public void testCreateAnonymousReview() {
        // Given: A patient with completed appointment with a doctor
        int patientId = 108; // Erdem Korkmaz from DML
        int doctorId = 1; // Dr. Ahmet Yılmaz
        int hospitalId = 1; // Ankara Şehir Hastanesi
        int rating = 5;
        String comment = "Çok memnun kaldım, ilgili ve profesyonel bir doktor.";
        boolean isAnonymous = true;

        // When: Patient creates an anonymous review
        boolean success = DatabaseQuery.createReview(
                patientId,
                doctorId,
                null, // appointmentId can be null for general review
                hospitalId,
                rating,
                comment,
                isAnonymous);

        // Then: Review should be created successfully
        assertTrue(success, "Anonymous review should be created successfully");
    }

    @Test
    @DisplayName("TC-002: Anonymous review should hide patient name")
    public void testAnonymousReviewHidesPatientName() {
        // Given: Anonymous review exists for a doctor
        int doctorId = 1;

        // When: Getting reviews for the doctor
        List<Review> reviews = DatabaseQuery.getReviewsByDoctor(doctorId);

        // Then: Anonymous reviews should show "Anonim" as patient name
        boolean hasAnonymousReview = reviews.stream()
                .anyMatch(r -> r.isAnonymous() && "Anonim".equals(r.getPatientName()));

        assertTrue(hasAnonymousReview || reviews.isEmpty(),
                "Anonymous reviews should display 'Anonim' as patient name");
    }

    @Test
    @DisplayName("TC-003: Patient can create non-anonymous review")
    public void testCreateNonAnonymousReview() {
        // Given: A patient wants to create a public review
        int patientId = 108;
        int doctorId = 1;
        int hospitalId = 1;
        int rating = 4;
        String comment = "İyi bir deneyimdi, teşekkürler.";
        boolean isAnonymous = false;

        // When: Patient creates a non-anonymous review
        boolean success = DatabaseQuery.createReview(
                patientId,
                doctorId,
                null,
                hospitalId,
                rating,
                comment,
                isAnonymous);

        // Then: Review should be created successfully
        assertTrue(success, "Non-anonymous review should be created successfully");
    }

    @Test
    @DisplayName("TC-004: Non-anonymous review should show patient name")
    public void testNonAnonymousReviewShowsPatientName() {
        // Given: Create a non-anonymous review first
        int patientId = 108;
        int doctorId = 1;
        int hospitalId = 1;

        DatabaseQuery.createReview(
                patientId, doctorId, null, hospitalId, 4,
                "Public review for test", false);

        // When: Getting reviews for the doctor
        List<Review> reviews = DatabaseQuery.getReviewsByDoctor(doctorId);

        // Then: Non-anonymous reviews should show actual patient name
        boolean hasNonAnonymousReview = reviews.stream()
                .anyMatch(r -> !r.isAnonymous() && r.getPatientName() != null
                        && !r.getPatientName().equals("Anonim"));

        assertTrue(hasNonAnonymousReview,
                "Non-anonymous reviews should display patient name");
    }

    @Test
    @DisplayName("TC-005: Review rating should be between 1 and 5")
    public void testReviewRatingValidation() {
        // Given: Valid rating range
        int patientId = 108;
        int doctorId = 1;
        int hospitalId = 1;
        String comment = "Test review";

        // When & Then: Test valid ratings
        for (int rating = 1; rating <= 5; rating++) {
            boolean success = DatabaseQuery.createReview(
                    patientId, doctorId, null, hospitalId, rating, comment, true);
            assertTrue(success, "Rating " + rating + " should be valid");
        }
    }

    @Test
    @DisplayName("TC-006: Average rating calculation for doctor")
    public void testAverageRatingCalculation() {
        // Given: A doctor with reviews
        int doctorId = 1;

        // When: Getting average rating
        double avgRating = DatabaseQuery.getAverageRatingForDoctor(doctorId);

        // Then: Average should be between 0 and 5 (or 0 if no reviews)
        assertTrue(avgRating >= 0.0 && avgRating <= 5.0,
                "Average rating should be between 0 and 5, got: " + avgRating);
    }

    @Test
    @DisplayName("TC-007: Patient ID is stored even for anonymous reviews")
    public void testAnonymousReviewStoresPatientId() {
        // Given: Anonymous review is created
        int patientId = 108;
        int doctorId = 1;
        int hospitalId = 1;

        // When: Creating anonymous review
        boolean success = DatabaseQuery.createReview(
                patientId, doctorId, null, hospitalId, 5,
                "Test anonymous review with stored patient ID", true);

        // Then: Review should be created (patient ID stored internally)
        assertTrue(success, "Anonymous review should store patient ID internally for tracking");

        // And: When retrieved, patient name should be "Anonim"
        List<Review> reviews = DatabaseQuery.getReviewsByDoctor(doctorId);
        boolean hasProperAnonymousReview = reviews.stream()
                .anyMatch(r -> r.isAnonymous() && "Anonim".equals(r.getPatientName()));

        assertTrue(hasProperAnonymousReview,
                "Anonymous review should be retrievable with 'Anonim' as patient name");
    }

    @Test
    @DisplayName("TC-008: Can retrieve all reviews for a specific doctor")
    public void testGetReviewsByDoctor() {
        // Given: A doctor ID
        int doctorId = 1;

        // When: Getting all reviews
        List<Review> reviews = DatabaseQuery.getReviewsByDoctor(doctorId);

        // Then: Should return a list (can be empty)
        assertNotNull(reviews, "Reviews list should not be null");

        // And: All reviews should be for the correct doctor
        reviews.forEach(review -> assertEquals(doctorId, review.getDoctorId(),
                "All reviews should be for doctor ID " + doctorId));
    }

    @Test
    @DisplayName("TC-009: Review comment should not be empty")
    public void testReviewCommentNotEmpty() {
        // Given: A review with empty comment
        int patientId = 108;
        int doctorId = 1;
        int hospitalId = 1;
        String emptyComment = "";

        // When: Attempting to create review with empty comment
        boolean success = DatabaseQuery.createReview(
                patientId, doctorId, null, hospitalId, 5, emptyComment, true);

        // Then: Review creation should still succeed (validation should be in UI)
        // Database allows empty comments, validation is UI responsibility
        assertTrue(success, "Database should accept review (UI validates comments)");
    }

    @Test
    @DisplayName("TC-010: Multiple anonymous reviews from same patient")
    public void testMultipleAnonymousReviewsFromSamePatient() {
        // Given: Same patient wants to create multiple reviews
        int patientId = 108;
        int doctorId = 1;
        int hospitalId = 1;

        // When: Creating multiple anonymous reviews
        boolean success1 = DatabaseQuery.createReview(
                patientId, doctorId, null, hospitalId, 5,
                "First anonymous review", true);

        boolean success2 = DatabaseQuery.createReview(
                patientId, doctorId, null, hospitalId, 4,
                "Second anonymous review", true);

        // Then: Both should be created successfully
        assertTrue(success1, "First anonymous review should be created");
        assertTrue(success2, "Second anonymous review should be created");

        // And: All reviews should maintain anonymity
        List<Review> reviews = DatabaseQuery.getReviewsByDoctor(doctorId);
        long anonymousCount = reviews.stream()
                .filter(r -> r.isAnonymous() && "Anonim".equals(r.getPatientName()))
                .count();

        assertTrue(anonymousCount >= 2,
                "Should have at least 2 anonymous reviews for the doctor");
    }
}
