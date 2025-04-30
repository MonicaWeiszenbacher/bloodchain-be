package utcluj.stiinte.bloodchain.model.enums;

import lombok.Getter;

import java.util.EnumSet;
import java.util.Set;

/**
 * Indicates the possible blood groups and their donors.
 * 
 * @see <a href=https://resources.wfsahq.org/atotw/bood-transfusion-part-1-basics/"></a>
 */
@Getter
public enum BloodGroup {

    O_NEGATIVE, O_POSITIVE, A_NEGATIVE, A_POSITIVE, B_NEGATIVE, B_POSITIVE, AB_NEGATIVE, AB_POSITIVE;

    static {
        O_NEGATIVE.otherThanSelfAllowedDonors = EnumSet.noneOf(BloodGroup.class);
        O_POSITIVE.otherThanSelfAllowedDonors = EnumSet.of(O_NEGATIVE);
        A_NEGATIVE.otherThanSelfAllowedDonors = EnumSet.of(O_NEGATIVE);
        A_POSITIVE.otherThanSelfAllowedDonors = EnumSet.of(O_NEGATIVE, O_POSITIVE, A_NEGATIVE);
        B_NEGATIVE.otherThanSelfAllowedDonors = EnumSet.of(O_NEGATIVE);
        B_POSITIVE.otherThanSelfAllowedDonors = EnumSet.of(O_NEGATIVE, O_POSITIVE, B_NEGATIVE);
        AB_NEGATIVE.otherThanSelfAllowedDonors = EnumSet.of(O_NEGATIVE, A_NEGATIVE, B_NEGATIVE);
        AB_POSITIVE.otherThanSelfAllowedDonors = EnumSet.of(O_NEGATIVE, O_POSITIVE, A_NEGATIVE, A_POSITIVE, B_NEGATIVE, B_POSITIVE, AB_NEGATIVE);
    }
    
    private Set<BloodGroup> otherThanSelfAllowedDonors;

    /**
     * Returns <@code>true</@code> if the recipient can accept a blood transfusion from the donor.
     */
    public static boolean canAccept(BloodGroup recipient, BloodGroup donor) {
        return recipient == donor || recipient.otherThanSelfAllowedDonors.contains(donor);
    }
}
