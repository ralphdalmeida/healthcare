package techtest.ehealthinnovation.org.healthapp.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import ca.uhn.fhir.rest.api.MethodOutcome;
import techtest.ehealthinnovation.org.healthapp.PatientDetailActivity;
import techtest.ehealthinnovation.org.healthapp.PatientListActivity;
import techtest.ehealthinnovation.org.healthapp.R;
import techtest.ehealthinnovation.org.healthapp.utilities.AppUtils;
import techtest.ehealthinnovation.org.healthapp.utilities.TasksFactory;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.IExecutor;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.ITask;

/**
 * A fragment representing a single Patient Detail screen.
 * This fragment is either contained in a {@link PatientListActivity}
 * in two-pane mode (on tablets) or a {@link PatientDetailActivity}
 * on handsets.
 */
public class PatientDetailFragment extends Fragment implements IExecutor {

    private Patient patient;
    private TextInputEditText familyText;
    private TextInputEditText givenText;
    private TextInputEditText genderText;
    private TextInputEditText birthdayText;
    private final Calendar birthdayCalendar = Calendar.getInstance();
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.CANADA);
    private final DatePickerDialog.OnDateSetListener birthdateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            birthdayCalendar.set(Calendar.YEAR, y);
            birthdayCalendar.set(Calendar.MONTH, m);
            birthdayCalendar.set(Calendar.DAY_OF_MONTH, d);
            birthdayText.setText(simpleDateFormat.format(birthdayCalendar.getTime()));
        }
    };
    private final View.OnClickListener birthdayViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (getContext() != null) {
                new DatePickerDialog(getContext(), birthdateListener, birthdayCalendar.get(Calendar.YEAR)
                        , birthdayCalendar.get(Calendar.MONTH), birthdayCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(AppUtils.ARG_PATIENT_ID)) {
            // Read the patient content from the server
            final ITask read = (ITask) TasksFactory.getTasksFactoryInstance().getObject(AppUtils.READ, this);
            read.executeTask(getArguments().getString(AppUtils.ARG_PATIENT_ID));
        }
        if (getActivity() != null) {
            final FloatingActionButton updateFab = getActivity().findViewById(R.id.patient_detail_fab);
            updateFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        // Retrieve and set new patient'information
                        final HumanName updatedName = new HumanName();
                        updatedName.setFamily(familyText.getEditableText().toString()).addGiven(givenText.getEditableText().toString());
                        patient.setName(Collections.singletonList(updatedName));
                        patient.setBirthDate(simpleDateFormat.parse(birthdayText.getEditableText().toString()));
                        final Enumerations.AdministrativeGender gender = Enumerations.AdministrativeGender.valueOf(genderText.getEditableText().toString().toUpperCase().trim());
                        patient.setGender(gender);

                        // Update the patient on the server
                        final ITask update = (ITask) TasksFactory.getTasksFactoryInstance().getObject(AppUtils.UPDATE, PatientDetailFragment.this);
                        update.executeTask(patient);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.patient_detail, container, false);

        familyText = rootView.findViewById(R.id.patient_detail_family);
        givenText = rootView.findViewById(R.id.patient_detail_given);
        genderText = rootView.findViewById(R.id.patient_detail_gender);
        birthdayText = rootView.findViewById(R.id.patient_detail_birthday);
        birthdayText.setOnClickListener(birthdayViewOnClickListener);

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
        // Get read and update async task results
        if (event instanceof Exception && getView() != null) {
            Snackbar.make(getView(), TextUtils.concat("Unable to communicate with FHIR server. ", ((Exception) event).getMessage()), Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_action, null).show();
        } else if (event instanceof Patient) {
            patient = (Patient) event;
            updateView();
        } else if (event instanceof MethodOutcome) {
            final MethodOutcome updateResponse = (MethodOutcome)event;
            if (getActivity() != null && updateResponse.getId().hasResourceType()) {
                getActivity().navigateUpTo(new Intent(getContext(), PatientListActivity.class));
            }
        }
    }

    private void updateView() {
        if (getActivity() != null) {
            final AppBarLayout appBarLayout = getActivity().findViewById(R.id.app_bar);
            if (appBarLayout != null && patient != null && patient.hasName()) {
                if (patient.hasPhoto() && patient.getPhotoFirstRep().getData() != null) {
                    //assume patient photo is small enough; otherwise use Picasso lib
                    final ImageView photo = appBarLayout.findViewById(R.id.patient_detail_profile_piture);
                    photo.setImageBitmap(BitmapFactory.decodeByteArray(patient.getPhotoFirstRep().getData(), 0, patient.getPhotoFirstRep().getData().length));
                }
                final HumanName patientNames = patient.getNameFirstRep();
                final TextView name = appBarLayout.findViewById(R.id.patient_detail_profile_name);
                name.setText(patientNames.getNameAsSingleString());

                if (familyText != null && patientNames.hasFamily() && !TextUtils.isEmpty(patientNames.getFamily()))
                    familyText.setText(patientNames.getFamily());
                if (givenText != null && patientNames.hasGiven() && !TextUtils.isEmpty(patientNames.getGivenAsSingleString()))
                    givenText.setText(patientNames.getGivenAsSingleString());
                if (genderText != null && patient.getGender() != null && !TextUtils.isEmpty(patient.getGender().getDisplay()))
                    genderText.setText(patient.getGender().getDisplay());
                if (birthdayText != null && patient.hasBirthDate()) birthdayText.setText(patient.getBirthDate().toString());
            }
        }
    }
}
