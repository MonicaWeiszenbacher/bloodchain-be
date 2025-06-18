package utcluj.stiinte.bloodchain.data.appointment;

import java.time.LocalDateTime;

/**
 * Contains the details for displaying a blood donation to a donor.
 */
public record DonorDonation(long id,
                            LocalDateTime time,
                            String transfusionCenter,
                            int units,
                            String token) {
}
