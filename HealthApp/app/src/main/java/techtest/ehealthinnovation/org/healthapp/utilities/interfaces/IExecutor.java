package techtest.ehealthinnovation.org.healthapp.utilities.interfaces;

/**
 * Created by Ralph on 2017-12-13.
 *
 * Executor interface to receive end of task events
 */
public interface IExecutor {

    // executor id
    void setExecutorId(String id);
    String getExecutorId();

    // receive task events
    void notifyTaskEvent(ITask task, Object event);
}
