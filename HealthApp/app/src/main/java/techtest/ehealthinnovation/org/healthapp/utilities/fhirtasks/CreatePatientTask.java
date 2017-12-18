package techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks;

import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import techtest.ehealthinnovation.org.healthapp.utilities.AppUtils;

/**
 * Created by Ralph on 2017-12-12.
 *
 */
public class CreatePatientTask extends Task {

    // MethodOutcome or Exception object to send
    private Object taskResult;

    @Override
    protected Void doInBackground(Object... params) {
        try {
            // Execute Create Patient task
            final IGenericClient client = AppUtils.FHIR_CONTEXT.newRestfulGenericClient(AppUtils.FHIR_SERVER_BASE);
            taskResult = client.create().resource((Patient)params[0])
                    .prettyPrint()
                    .encodedJson()
                    .execute();
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
