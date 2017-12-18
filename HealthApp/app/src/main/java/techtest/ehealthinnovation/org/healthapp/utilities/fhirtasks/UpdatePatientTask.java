package techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks;

import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import techtest.ehealthinnovation.org.healthapp.utilities.AppUtils;

/**
 * Created by Ralph on 2017-12-12.
 *
 */
public class UpdatePatientTask extends Task {

    // MethodOutcome or Exception object to notify
    private Object taskResult;

    @Override
    protected Void doInBackground(Object... params) {
        try {
            // Execute Update Patient task
            final IGenericClient client = AppUtils.FHIR_CONTEXT.newRestfulGenericClient(AppUtils.FHIR_SERVER_BASE);
            taskResult = client.update().resource((Patient)params[0]).execute();
        } catch (Exception ex) {
            taskResult = ex;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // Send outcome to the executor
        getExecutor().notifyTaskEvent(this, taskResult);
    }

}
