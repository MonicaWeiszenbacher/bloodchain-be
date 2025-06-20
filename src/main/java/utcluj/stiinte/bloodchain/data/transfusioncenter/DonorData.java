package utcluj.stiinte.bloodchain.data.transfusioncenter;

import utcluj.stiinte.bloodchain.model.enums.BloodGroup;
import utcluj.stiinte.bloodchain.model.enums.Gender;

public record DonorData(String name,
                        String email,
                        BloodGroup bloodGroup,
                        int age,
                        Gender gender) {
}
