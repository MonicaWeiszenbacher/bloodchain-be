package utcluj.stiinte.bloodchain.data.donor;

import java.time.LocalDateTime;

public record PeriodicDonationRequest(long transfusionCenterId,
                                      LocalDateTime startDate,
                                      int frequency) {
}
