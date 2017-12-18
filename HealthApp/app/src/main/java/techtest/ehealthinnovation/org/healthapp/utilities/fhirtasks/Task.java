package techtest.ehealthinnovation.org.healthapp.utilities.fhirtasks;

import android.os.AsyncTask;

import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.IExecutor;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.IFactory;
import techtest.ehealthinnovation.org.healthapp.utilities.interfaces.ITask;

/**
 * Created by Ralph on 2017-12-12.
 *
 */
public abstract class Task extends AsyncTask<Object, Object, Void> implements ITask {

    // id
    private String taskId;
    // executor
    private IExecutor executor;
    // factory
    private IFactory factory;

    // cancel the task
    public void cancel() {
        cancel(true);
    }

    // perform the task
    public void executeTask(Object... params) {
        this.executeOnExecutor(THREAD_POOL_EXECUTOR, params);
    }

    // getters and setters
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String id) {
        this.taskId = id;
    }

    public IExecutor getExecutor() {
        return executor;
    }
    @Override
    public void setExecutor(IExecutor executor) {
        this.executor = executor;
    }

    public IFactory getFactory() {
        return factory;
    }
    @Override
    public void setFactory(IFactory factory) {
        this.factory = factory;
    }

}
