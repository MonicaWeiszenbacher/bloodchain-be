package utcluj.stiinte.bloodchain.data.transfusioncenter;

import utcluj.stiinte.bloodchain.model.enums.BloodGroup;

import java.time.LocalDate;

public record BloodRequestData(long id,
                               long requesterId,
                               int currentStock,
                               BloodGroup bloodGroup,
                               int units,
                               LocalDate takeoverDate) {
}
