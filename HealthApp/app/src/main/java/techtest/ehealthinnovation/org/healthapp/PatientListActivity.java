package techtest.ehealthinnovation.org.healthapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import techtest.ehealthinnovation.org.healthapp.fragments.CreatePatientDialogFragment;
import techtest.ehealthinnovation.org.healthapp.utilities.adapters.SimplePatientRecyclerViewAdapter;

/**
 * An activity representing a list of Patients. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PatientDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PatientListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SimplePatientRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CreatePatientDialogFragment dialogFragment = new CreatePatientDialogFragment();
                dialogFragment.show(getSupportFragmentManager(), "CreatePatient");
            }
        });

        if (findViewById(R.id.patient_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // Refresh the list with a pull-down
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.patient_container);
        assert swipeRefreshLayout != null;
        adapter = new SimplePatientRecyclerViewAdapter(getSupportFragmentManager(), swipeRefreshLayout, mTwoPane);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAndFetchPatients();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.patient_list);
        assert recyclerView != null;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.loadAdapterPatients();

        // Delete a patient with an horizontal swipe (left or right)
        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deletePatient(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void refreshAndFetchPatients() {
        adapter.loadLatestAdapterPatients();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item for sorting.
        switch (item.getItemId()) {
            //by name and birthday
            case R.id.action_sort_name:
                adapter.sortAdapterPatientsByName();
                return true;
            case R.id.action_sort_birthday:
                adapter.sortAdapterPatientsByBirthday();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}
