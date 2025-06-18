package utcluj.stiinte.bloodchain.data.donor;


import java.time.LocalDate;

public record BloodRequestData(String bloodGroup,
                               int units,
                               LocalDate takeoverDate) {
}
