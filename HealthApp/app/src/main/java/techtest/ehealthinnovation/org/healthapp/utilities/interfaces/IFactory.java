package techtest.ehealthinnovation.org.healthapp.utilities.interfaces;

/**
 * Created by Ralph on 2017-12-13.
 *
 * Factory interface for an executor to launch tasks
 */
public interface IFactory {

    // Build object
    Object getObject(int id, IExecutor executor);
}
