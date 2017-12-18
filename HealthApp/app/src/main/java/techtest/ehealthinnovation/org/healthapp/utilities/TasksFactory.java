package techtest.ehealthinnovation.org.healthapp.utilities;

import techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks.CreatePatientTask;
import techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks.DeletePatientTask;
import techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks.HistoryPatientsTask;
import techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks.ReadPatientTask;
import techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks.SearchPatientsTask;
import techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks.UpdatePatientTask;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.IExecutor;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.IFactory;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.ITask;

/**
 * Created by Ralph on 2017-12-15.
 *
 */
public class TasksFactory implements IFactory {

    // Singleton Pattern
    private static volatile TasksFactory tasksFactoryInstance;
    private TasksFactory() {
        // Reject Java Reflexion
        if (tasksFactoryInstance != null) throw new RuntimeException("Please use getTasksFactoryInstance() method!");
    }
    public static synchronized TasksFactory getTasksFactoryInstance() {
        if (tasksFactoryInstance == null) {
            tasksFactoryInstance = new TasksFactory();
        }
        return tasksFactoryInstance;
    }

    @Override
    public Object getObject(int id, IExecutor executor) {
        switch (id) {
            case AppUtils.CREATE:
                return getCreatePatientTask(executor);
            case AppUtils.READ:
                return getReadPatientTask(executor);
            case AppUtils.UPDATE:
                return getUpdatePatientTask(executor);
            case AppUtils.DELETE:
                return getDeletePatientTask(executor);
            case AppUtils.SEARCH:
                return getSearchPatientTask(executor);
            case AppUtils.HISTORY:
                return getHistoryPatientTask(executor);
        }
        return null;
    }

    private Object getCreatePatientTask(IExecutor executor) {
        // Instantiate creating patient task with its executor
        ITask createPatientTask = new CreatePatientTask();
        init(createPatientTask, executor);
        return createPatientTask;
    }

    private Object getReadPatientTask(IExecutor executor) {
        // Instantiate reading patient info task with its executor
        ITask readPatientTask = new ReadPatientTask();
        init(readPatientTask, executor);
        return readPatientTask;
    }

    private Object getUpdatePatientTask(IExecutor executor) {
        // Instantiate creating patient task with its executor
        ITask updatePatientTask = new UpdatePatientTask();
        init(updatePatientTask, executor);
        return updatePatientTask;
    }

    private Object getDeletePatientTask(IExecutor executor) {
        // Instantiate reading patient info task with its executor
        ITask deletePatientTask = new DeletePatientTask();
        init(deletePatientTask, executor);
        return deletePatientTask;
    }

    private Object getSearchPatientTask(IExecutor executor) {
        // Instantiate creating patient task with its executor
        ITask searchPatientTask = new SearchPatientsTask();
        init(searchPatientTask, executor);
        return searchPatientTask;
    }

    private Object getHistoryPatientTask(IExecutor executor) {
        // Instantiate reading patient info task with its executor
        ITask historyPatientTask = new HistoryPatientsTask();
        init(historyPatientTask, executor);
        return historyPatientTask;
    }

    private void init(ITask task, IExecutor executor) {
        // Initialize the task and allow access to this factory
        task.setFactory(this);
        task.setExecutor(executor);
    }
}
