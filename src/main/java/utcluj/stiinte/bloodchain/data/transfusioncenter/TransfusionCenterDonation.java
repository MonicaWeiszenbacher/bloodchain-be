package utcluj.stiinte.bloodchain.data.transfusioncenter;

import com.fasterxml.jackson.annotation.JsonFormat;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;

import java.time.LocalDateTime;

/**
 * Contains the details for displaying a blood donation appointment to a transfusion center.
 */
public record TransfusionCenterDonation(long id,
                                        LocalDateTime time,
                                        long donorId,
                                        @JsonFormat(shape = JsonFormat.Shape.STRING) BloodGroup donorBloodGroup,
                                        int units,
                                        String token) {
}
