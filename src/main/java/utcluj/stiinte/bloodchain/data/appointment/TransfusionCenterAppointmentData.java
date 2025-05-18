package utcluj.stiinte.bloodchain.data.appointment;

import com.fasterxml.jackson.annotation.JsonFormat;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;

import java.time.OffsetDateTime;

/**
 * Contains the details for displaying a blood donation appointment to a transfusion center.
 */
public record TransfusionCenterAppointmentData(long id,
                                               OffsetDateTime time,
                                               @JsonFormat(shape = JsonFormat.Shape.STRING)
                                               BloodGroup donorBloodGroup,
                                               String donorPhone) {
}
