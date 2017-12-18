package techtest.ehealthinnovation.org.healthapp;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Ralph on 2017-12-17.
 *
 * Simple Patient local unit testing
 */
public class PatientTest {

    private Patient patientTest;
    private HumanName patientHumanName;

    @Before
    public void setUp() throws Exception {
        patientTest = new Patient();
        patientHumanName = new HumanName();
    }

    @Test
    public void create() throws Exception {
        assertNotNull(patientTest);
        assertNotNull(patientHumanName);
    }

    @Test
    public void setters() throws Exception {
        patientTest.addName().setFamily("TestFamily").addGiven("TestGiven");
        patientTest.setGender(Enumerations.AdministrativeGender.valueOf("MALE"));
        patientTest.setBirthDate(new Date());
        assertTrue(patientTest.hasName());
        assertTrue(patientTest.hasGender());
        assertTrue(patientTest.hasBirthDate());
        patientHumanName.setFamily("TestFamily").addGiven("TestGiven");
        assertTrue(patientHumanName.hasFamily());
        assertTrue(patientHumanName.hasGiven());
    }

    @Test
    public void getters() throws Exception {
        assertNotNull(patientTest.getNameFirstRep());
        assertNull(patientTest.getNameFirstRep().getFamily());
        patientTest.setName(Collections.singletonList(patientHumanName));
        assertEquals(patientTest.getNameFirstRep(), patientHumanName);
        assertEquals(patientTest.getNameFirstRep().getFamily(), patientHumanName.getFamily());
        assertEquals(patientTest.getNameFirstRep().getNameAsSingleString(), patientHumanName.getNameAsSingleString());
        assertEquals(patientTest.getNameFirstRep().getGivenAsSingleString(), patientHumanName.getGivenAsSingleString());
    }
}