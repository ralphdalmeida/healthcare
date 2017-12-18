package techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks;

import android.text.TextUtils;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import techtest.ehealthinnovation.org.healthapp.utilities.AppUtils;

/**
 * Created by Ralph on 2017-12-12.
 *
 */
public class SearchPatientsTask extends Task {

    // List<Patient> or Exception object to send
    private Object taskResult;

    @Override
    protected Void doInBackground(Object... params) {
        try {
            // Execute Search Patient task with sorting (by family name or birthday)
            final IGenericClient client = AppUtils.FHIR_CONTEXT.newRestfulGenericClient(AppUtils.FHIR_SERVER_BASE);
            final Bundle bundle;
            if (TextUtils.equals(AppUtils.FHIR_SORTED, (String)params[0])) {
                bundle = client.search().forResource(Patient.class)
                        .sort().descending((String) params[1])
                        .sort().descending((String) params[2])
                        .count((int) params[3])
                        .returnBundle(Bundle.class)
                        .execute();
            } else {
                bundle = client.search().forResource(Patient.class)
                        .sort().descending((String) params[1])
                        .count((int) params[2])
                        .returnBundle(Bundle.class)
                        .execute();
            }
            taskResult = BundleUtil.toListOfResourcesOfType(AppUtils.FHIR_CONTEXT, bundle, Patient.class);
        } catch (Exception ex) {
            taskResult = ex;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Send result to the executor
        getExecutor().notifyTaskEvent(this, taskResult);
    }

}
