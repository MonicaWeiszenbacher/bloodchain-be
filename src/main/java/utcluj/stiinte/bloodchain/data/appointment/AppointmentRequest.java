package utcluj.stiinte.bloodchain.data.appointment;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;

/**
 * Contains the required details for creating a blood donation appointment.
 * 
 * @param userId blood donor id
 * @param transfusionCenterId blood transfusion center id
 * @param time date and time of the appointment
 */
public record AppointmentRequest(long userId,
                                 long transfusionCenterId,
                                 @NonNull @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime time) {
}
