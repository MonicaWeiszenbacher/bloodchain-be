package utcluj.stiinte.bloodchain.data.appointment;

import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;

/**
 * Contains the required details for creating a blood donation appointment.
 * 
 * @param userId blood donor id
 * @param transfusionCenterId blood transfusion center id
 * @param time date and time of the appointment
 */
public record AppointmentData(long userId,
                              long transfusionCenterId,
                              @NonNull OffsetDateTime time) {
}
