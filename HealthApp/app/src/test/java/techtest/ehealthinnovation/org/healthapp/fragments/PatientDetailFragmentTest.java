package techtest.ehealthinnovation.org.healthapp.fragments;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Ralph on 2017-12-17.
 *
 * Simple Fragment local unit testing
 */
public class PatientDetailFragmentTest {

    private PatientDetailFragment fragmentTest;
    @Before
    public void setUp() throws Exception {
        fragmentTest = new PatientDetailFragment();
    }

    @Test
    public void onCreate() throws Exception {
        assertNotNull(fragmentTest);
    }

    @Test
    public void onCreateView() throws Exception {
        assertNull(fragmentTest.getView());
    }

    @Test
    public void setExecutorId() throws Exception {
        fragmentTest.setExecutorId("TestFragment");
        assertNotEquals("TestFragment", fragmentTest.getExecutorId());
    }

    @Test
    public void getExecutorId() throws Exception {
        assertEquals(fragmentTest.getExecutorId(), String.valueOf(fragmentTest.hashCode()));
    }

}