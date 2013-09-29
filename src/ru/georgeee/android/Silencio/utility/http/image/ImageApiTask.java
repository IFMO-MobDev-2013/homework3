package ru.georgeee.android.Silencio.utility.http.image;

import ru.georgeee.android.Silencio.utility.http.HttpUtility;
import ru.georgeee.android.Silencio.utility.http.JsonResponseHttpTask;

import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 22:03
 * To change this template use File | Settings | File Templates.
 */
public abstract class ImageApiTask extends JsonResponseHttpTask<ImageApiResult> {
    protected Integer pageNumber = null;
    protected String searchText = null;

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public String getSearchText() {
        return searchText;
    }

    @Override
    protected Executor getExecutor() {
        return HttpUtility.getInstance().getImageApiExecutor();
    }
}
