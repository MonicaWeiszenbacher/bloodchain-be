package utcluj.stiinte.bloodchain.data.appointment;

import java.time.OffsetDateTime;

public record DonationHistory(long donationId,
                              OffsetDateTime time,
                              String transfusionCenter) {
}
