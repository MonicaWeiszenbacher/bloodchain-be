package utcluj.stiinte.bloodchain.data.appointment;

import java.time.OffsetDateTime;

/**
 * Contains the details for displaying a blood donation appointment to a donor.
 * 
 * @param id
 * @param time
 * @param transfusionCenter
 */
public record DonorAppointmentData(long id,
                                   OffsetDateTime time,
                                   String transfusionCenter) {
}
