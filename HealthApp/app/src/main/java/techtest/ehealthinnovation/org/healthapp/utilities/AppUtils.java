package techtest.ehealthinnovation.org.healthapp.utilities;

import org.hl7.fhir.dstu3.model.Patient;

import java.util.Comparator;

import ca.uhn.fhir.context.FhirContext;

/**
 * Created by Ralph on 2017-12-14.
 *
 * Application utilities
 */
public class AppUtils {

    // The fragment argument representing the patient ID
    public static final String ARG_PATIENT_ID = "patient_id";
    // To adjust the number of patients that are downloaded at one time
    public static final int NUMBER_PATIENTS_DOWNLOADED = 10;
    // Patient Comparators
    public static final Comparator<Patient> BY_NAME_COMPARATOR = new Comparator<Patient>() {
        public int compare(Patient p1, Patient p2) {
            if ((p1.hasName() && p1.getNameFirstRep().hasFamily()) && (p2.hasName() && p2.getNameFirstRep().hasFamily())) {
                return p1.getNameFirstRep().getFamily().compareTo(p2.getNameFirstRep().getFamily());
            } else if ((!p1.hasName() || !p1.getNameFirstRep().hasFamily()) && (!p2.hasName() || !p2.getNameFirstRep().hasFamily())) {
                return 0;
            } else return p1.hasName() ? 1 : -1;
        }
    };
    public static final Comparator<Patient> BY_BIRTHDAY_COMPARATOR = new Comparator<Patient>() {
            public int compare(Patient p1, Patient p2) {
                if (p1.hasBirthDate() && p2.hasBirthDate()) {
                    return p1.getBirthDate().compareTo(p2.getBirthDate());
                } else if (!p1.hasBirthDate() && !p2.hasBirthDate()) {
                    return 0;
                } else return p1.hasBirthDate() ? 1 : -1;
            }
    };

    // FHIR constants
    public static final String FHIR_SERVER_BASE = "http://fhirtest.uhn.ca/baseDstu3";
    public static final String FHIR_UPDATED = "_lastUpdated";
    public static final String FHIR_LATEST = "_id";
    public static final String FHIR_SORTED = "sorted";
    //public static final String FHIR_SORTED_BY_BIRTHDAY = "birthdate";
    //public static final String FHIR_SORTED_BY_NAME = "name";
    public static volatile FhirContext FHIR_CONTEXT = FhirContext.forDstu3();
    // FHIR operations constants
    public static final int CREATE = 1;
    public static final int READ = 3;
    public static final int UPDATE = 7;
    public static final int DELETE = 13;
    public static final int SEARCH = 27;
    public static final int HISTORY = 53;
}
