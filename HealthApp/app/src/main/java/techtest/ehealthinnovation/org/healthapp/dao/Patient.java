package techtest.ehealthinnovation.org.healthapp.dao;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;

import techtest.ehealthinnovation.org.healthapp.dao.datatypes.HumanName;

/**
 * Created by Ralph on 2017-12-12.
 *
 * Class representing the Patient's model to be stored locally
 */
@SuppressWarnings("unused")
public class Patient {

    private final HumanName name;

    public static final String MALE = "male";
    public static final String FEMALE = "female";
    public static final String OTHER = "other";
    public static final String UNKNOWN = "unknown";
    @StringDef({MALE, FEMALE, OTHER, UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Code {}

    private final String gender;
    private final Date birthday;

    public Patient(HumanName name, @Code String gender, Date birthday) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
    }

    public HumanName getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }
}
