package techtest.ehealthinnovation.org.healthapp.utilities.interfaces;

/**
 * Created by Ralph on 2017-12-13.
 *
 * Task interface for executing an executor task
 */
public interface ITask {

    // access factory
    void setFactory(IFactory factory);

    // perform task
    void executeTask(Object... params);

    // access executor
    void setExecutor(IExecutor executor);

    // task id - can be useful for cancel
    void setTaskId(String id);
    String getTaskId();
    // cancel task - not used
    void cancel();
}
