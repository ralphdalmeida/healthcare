package techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import techtest.ehealthinnovation.org.healthapp.utilities.AppUtils;

/**
 * Created by Ralph on 2017-12-12.
 *
 */
public class HistoryPatientsTask extends Task {

    // List<Patient> or Exception object to send
    private Object taskResult;

    @Override
    protected Void doInBackground(Object... params) {
        try {
            // Execute History Patient task
            final IGenericClient client = AppUtils.FHIR_CONTEXT.newRestfulGenericClient(AppUtils.FHIR_SERVER_BASE);
            final Bundle bundle = client.history().onServer().andReturnBundle(Bundle.class)
                    .count((int)params[0])
                    .execute();
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
