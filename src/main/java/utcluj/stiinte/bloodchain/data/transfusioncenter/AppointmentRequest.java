package utcluj.stiinte.bloodchain.data.transfusioncenter;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record AppointmentRequest(long donorId,
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time) {
}