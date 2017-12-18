package techtest.ehealthinnovation.org.healthapp.dao.datatypes;

import android.support.annotation.StringDef;
import android.util.Pair;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.List;

/**
 * Created by Ralph on 2017-12-12.
 *
 * Datatype representing the Patient's name to be stored locally
 */
@SuppressWarnings("unused")
public class HumanName {

    public static final String USUAL = "usual";
    public static final String OFFICIAL = "official";
    public static final String TEMP = "temp";
    public static final String NICKNAME = "nickname";
    public static final String ANONYMOUS = "anonymous";
    public static final String OLD = "old";
    public static final String MAIDEN = "maiden";
    @StringDef({USUAL, OFFICIAL, TEMP, NICKNAME, ANONYMOUS, OLD, MAIDEN})
    @Retention(RetentionPolicy.SOURCE)
    private @interface Code {}

    private final String use;
    private final String text;
    private final String family;
    private final List<String> given;
    private final List<String> prefix;
    private final List<String> suffix;
    private final Pair<Date, Date> period;

    public HumanName(@Code String use, String text, String family, List<String> given, List<String> prefix, List<String> suffix, Pair<Date, Date> period) {
        this.use = use;
        this.text = text;
        this.family = family;
        this.given = given;
        this.prefix = prefix;
        this.suffix = suffix;
        this.period = period;
    }

    public String getUse() {
        return use;
    }

    public String getText() {
        return text;
    }

    public String getFamily() {
        return family;
    }

    public List<String> getGiven() {
        return given;
    }

    public List<String> getPrefix() {
        return prefix;
    }

    public List<String> getSuffix() {
        return suffix;
    }

    public Pair<Date, Date> getPeriod() {
        return period;
    }
}
