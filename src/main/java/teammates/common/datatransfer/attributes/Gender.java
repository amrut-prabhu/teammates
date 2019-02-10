package teammates.common.datatransfer.attributes;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the gender of a student.
 */
public enum Gender {
    @SerializedName("male")
    MALE,
    @SerializedName("female")
    FEMALE,
    @SerializedName("other")
    OTHER,
    ;

    public static Gender getGenderEnumValue(String gender) {
        try {
            return Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Gender.OTHER;
        }
    }
}
