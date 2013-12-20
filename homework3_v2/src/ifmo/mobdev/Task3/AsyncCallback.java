package ifmo.mobdev.Task3;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 25.10.13
 * Time: 2:08
 * To change this template use File | Settings | File Templates.
 */
public interface AsyncCallback<Result, Exception> {
    void onSuccess(Result result);
    void onFailure(Exception cause);
}