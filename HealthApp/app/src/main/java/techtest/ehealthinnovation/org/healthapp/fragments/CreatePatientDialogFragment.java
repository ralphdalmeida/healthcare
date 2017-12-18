package techtest.ehealthinnovation.org.healthapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Patient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import ca.uhn.fhir.rest.api.MethodOutcome;
import techtest.ehealthinnovation.org.healthapp.PatientListActivity;
import techtest.ehealthinnovation.org.healthapp.R;
import techtest.ehealthinnovation.org.healthapp.utilities.AppUtils;
import techtest.ehealthinnovation.org.healthapp.utilities.TasksFactory;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.IExecutor;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.ITask;

/**
 * Created by Ralph on 2017-12-17.
 *
 * A dialog fragment representing a Patient Create Form
 */
public class CreatePatientDialogFragment extends DialogFragment implements IExecutor {

    private TextView birthdayText;
    private final Calendar birthdayCalendar = Calendar.getInstance();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.CANADA);
    private final DatePicker.OnDateChangedListener birthdayListener = new DatePicker.OnDateChangedListener() {
        @Override
        public void onDateChanged(DatePicker datePicker, int y, int m, int d) {
            birthdayCalendar.set(Calendar.YEAR, y);
            birthdayCalendar.set(Calendar.MONTH, m);
            birthdayCalendar.set(Calendar.DAY_OF_MONTH, d);
            birthdayText.setText(simpleDateFormat.format(birthdayCalendar.getTime()));
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CreatePatientDialogFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.patient_create, container, false);

        final TextInputEditText familyText = rootView.findViewById(R.id.patient_create_family);
        final TextInputEditText givenText = rootView.findViewById(R.id.patient_create_given);
        final TextInputEditText genderText = rootView.findViewById(R.id.patient_create_gender);
        birthdayText = rootView.findViewById(R.id.patient_create_birthday);
        final DatePicker datePicker = rootView.findViewById(R.id.patient_create_date_picker);
        datePicker.init(birthdayCalendar.get(Calendar.YEAR)
                , birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DAY_OF_MONTH), birthdayListener);

        final Button createButton = rootView.findViewById(R.id.patient_create_button);
        createButton.setVisibility(View.VISIBLE);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Retrieve and add the patient'information
                    final Patient patient = new Patient();
                    patient.addName().setFamily(familyText.getEditableText().toString()).addGiven(givenText.getEditableText().toString());
                    patient.setBirthDate(simpleDateFormat.parse(birthdayText.getText().toString()));
                    final Enumerations.AdministrativeGender gender = Enumerations.AdministrativeGender.valueOf(genderText.getEditableText().toString().toUpperCase().trim());
                    patient.setGender(gender);

                    // Create the patient on the server
                    final ITask create = (ITask) TasksFactory.getTasksFactoryInstance().getObject(AppUtils.CREATE, CreatePatientDialogFragment.this);
                    create.executeTask(patient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    @Override
    public void setExecutorId(String id) {}

    @Override
    public String getExecutorId() {
        return String.valueOf(hashCode());
    }

    @Override
    public void notifyTaskEvent(ITask task, Object event) {
        // Get create async task results
        if (event instanceof Exception && getView() != null) {
            Snackbar.make(getView(), TextUtils.concat("Unable to communicate with FHIR server. ", ((Exception) event).getMessage()), Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_action, null).show();
        } else if (event instanceof MethodOutcome) {
            final MethodOutcome updateResponse = (MethodOutcome)event;
            if (getActivity() != null && updateResponse.getId().hasResourceType()) {
                ((PatientListActivity)getActivity()).refreshAndFetchPatients();
                dismiss();
            }
        }
    }
}
