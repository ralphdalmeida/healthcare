package techtest.ehealthinnovation.org.healthapp.utilities.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import techtest.ehealthinnovation.org.healthapp.PatientDetailActivity;
import techtest.ehealthinnovation.org.healthapp.R;
import techtest.ehealthinnovation.org.healthapp.fragments.PatientDetailFragment;
import techtest.ehealthinnovation.org.healthapp.utilities.AppUtils;
import techtest.ehealthinnovation.org.healthapp.utilities.TasksFactory;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.IExecutor;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.ITask;

/**
 * Created by Ralph on 2017-12-14.
 *
 * RecyclerView adapter for list of Patients
 */
public class SimplePatientRecyclerViewAdapter extends RecyclerView.Adapter<SimplePatientRecyclerViewAdapter.PatientViewHolder> implements IExecutor {

    private final FragmentManager fragmentManager;
    private final List<Patient> adapterPatients = new ArrayList<>();
    private final boolean isTwoPane;
    private final SwipeRefreshLayout patientLayout;
    private final View.OnClickListener patientViewOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final String patientId = (String) view.getTag();
            if (isTwoPane) {
                final Bundle arguments = new Bundle();
                arguments.putString(AppUtils.ARG_PATIENT_ID, patientId);
                final PatientDetailFragment fragment = new PatientDetailFragment();
                fragment.setArguments(arguments);
                fragmentManager.beginTransaction()
                        .replace(R.id.patient_detail_container, fragment)
                        .commit();
            } else {
                final Context context = view.getContext();
                final Intent intent = new Intent(context, PatientDetailActivity.class);
                intent.putExtra(AppUtils.ARG_PATIENT_ID, patientId);
                context.startActivity(intent);
            }
        }
    };

    // Constructor
    public SimplePatientRecyclerViewAdapter(@NonNull FragmentManager fragmentManager, @NonNull SwipeRefreshLayout layout, boolean twoPane) {
        this.fragmentManager = fragmentManager;
        this.isTwoPane = twoPane;
        this.patientLayout = layout;
    }

    public void deletePatient(int position) {
        // Read the patient by id or instance from the server
        final ITask delete = (ITask) TasksFactory.getTasksFactoryInstance().getObject(AppUtils.DELETE, this);
        final Patient patientToDelete = adapterPatients.get(position);
        if (patientToDelete.hasId()) {
            delete.executeTask((TextUtils.split(patientToDelete.getId(), "/"))[5]);
        } else {
            delete.executeTask(adapterPatients.get(position));
        }
        // Update the recycler view
        adapterPatients.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void loadAdapterPatients() {
        // Download 10 most recently updated patients from the server
        final ITask search = (ITask) TasksFactory.getTasksFactoryInstance().getObject(AppUtils.SEARCH, this);
        search.executeTask(null, AppUtils.FHIR_UPDATED, AppUtils.NUMBER_PATIENTS_DOWNLOADED);
    }

    public void loadLatestAdapterPatients() {
        // Fetch the new latest 10 patients
        final ITask search = (ITask) TasksFactory.getTasksFactoryInstance().getObject(AppUtils.SEARCH, this);
        search.executeTask(null, AppUtils.FHIR_LATEST, AppUtils.NUMBER_PATIENTS_DOWNLOADED);
    }

    public void sortAdapterPatientsByName() {
        Collections.sort(adapterPatients, AppUtils.BY_NAME_COMPARATOR);
        // sorting by name from the server
        //ITask search = (ITask) TasksFactory.getTasksFactoryInstance().getObject(AppUtils.SEARCH, this);
        //search.executeTask(AppUtils.FHIR_SORTED, AppUtils.FHIR_LATEST, AppUtils.FHIR_SORTED_BY_NAME, AppUtils.NUMBER_PATIENTS_DOWNLOADED);
        notifyDataSetChanged();
    }

    public void sortAdapterPatientsByBirthday() {
        Collections.sort(adapterPatients, AppUtils.BY_BIRTHDAY_COMPARATOR);
        // sorting by birth date from the server
        //ITask search = (ITask) TasksFactory.getTasksFactoryInstance().getObject(AppUtils.SEARCH, this);
        //search.executeTask(AppUtils.FHIR_SORTED, AppUtils.FHIR_LATEST, AppUtils.FHIR_SORTED_BY_BIRTHDAY, AppUtils.NUMBER_PATIENTS_DOWNLOADED);
        notifyDataSetChanged();
    }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_content, parent, false);
        return new SimplePatientRecyclerViewAdapter.PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        final Patient patient = adapterPatients.get(position);
        if (patient != null) {
            if (patient.hasPhoto() && patient.getPhotoFirstRep().getData() != null) {
                //assume patient photo is small enough; otherwise use Picasso lib
                holder.imgView.setImageBitmap(BitmapFactory.decodeByteArray(patient.getPhotoFirstRep().getData(), 0, patient.getPhotoFirstRep().getData().length));
            }
            if (patient.hasName() && !TextUtils.isEmpty(patient.getNameFirstRep().getNameAsSingleString())) {
                holder.txtView.setText(patient.getNameFirstRep().getNameAsSingleString());
            }

            holder.crdView.setTag(patient.getId());
            holder.crdView.setOnClickListener(patientViewOnClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return adapterPatients.size();
    }

    @Override
    public void setExecutorId(String id) {}

    @Override
    public String getExecutorId() {
        return String.valueOf(hashCode());
    }

    @Override
    public void notifyTaskEvent(ITask task, Object event) {
        // Get search and delete async task results
        if (patientLayout.isRefreshing()) patientLayout.setRefreshing(false);
        if (event instanceof Exception) {
            Snackbar.make(patientLayout, TextUtils.concat("Unable to communicate with FHIR server. ", ((Exception) event).getMessage()), Snackbar.LENGTH_LONG)
                    .setAction(R.string.snackbar_action, null).show();
        } else if (event instanceof List) {
            adapterPatients.clear();
            adapterPatients.addAll((List<Patient>)event);
        } else if (event instanceof IBaseOperationOutcome) {
            final IBaseOperationOutcome deleteResponse = (IBaseOperationOutcome)event;
            final OperationOutcome deleteOutcome = (OperationOutcome)deleteResponse;
            if (deleteOutcome.hasIssue() && deleteOutcome.getIssueFirstRep().hasDetails()) {
                Snackbar.make(patientLayout, TextUtils.concat("Unable to delete patient on FHIR server. Error code : ", deleteOutcome.getIssueFirstRep().getDetails().getText()), Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action, null).show();
            }
            loadLatestAdapterPatients();
        }
        notifyDataSetChanged();
    }

    class PatientViewHolder extends RecyclerView.ViewHolder {
        final CardView crdView;
        final ImageView imgView;
        final TextView txtView;

        PatientViewHolder(View view) {
            super(view);
            crdView = view.findViewById(R.id.patient_card);
            imgView = crdView.findViewById(R.id.patient_photo);
            txtView = crdView.findViewById(R.id.patient_name);
        }
    }
}
