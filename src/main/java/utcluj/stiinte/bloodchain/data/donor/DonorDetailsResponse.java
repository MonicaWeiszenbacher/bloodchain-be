package utcluj.stiinte.bloodchain.data.donor;

import java.time.LocalDate;

public record DonorDetailsResponse(String name,
                                   String email,
                                   int age,
                                   String gender,
                                   String bloodGroup,
                                   int numberOfTokens,
                                   LocalDate nextDonationDate,
                                   String medicalFileUrl) {
}
