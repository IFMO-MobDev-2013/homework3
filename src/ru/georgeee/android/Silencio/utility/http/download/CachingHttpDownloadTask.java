package ru.georgeee.android.Silencio.utility.http.download;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import ru.georgeee.android.Silencio.utility.cacher.FileCacher;
import ru.georgeee.android.Silencio.utility.http.HttpTask;
import ru.georgeee.android.Silencio.utility.http.HttpUtility;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class CachingHttpDownloadTask extends HttpTask<File> {

    protected abstract String getUrl();


    @Override
    protected HttpRequestBase getHttpRequestBase() {
        return new HttpGet(getUrl());
    }

    @Override
    protected File doInBackground(Object... params) {
        String url = getUrl();
        FileCacher fileCacher = FileCacher.getInstance();
        File file = fileCacher.registerFile(url);
        if (file.exists()) {
            return file;
        }
        return super.doInBackground(params);
    }

    @Override
    protected File getResult(HttpResponse httpResponse) throws IOException {
        String url = getUrl();
        FileCacher fileCacher = FileCacher.getInstance();
        File file = fileCacher.registerFile(url);
        if (file.exists()) {
            return file;
        }
        HttpUtility.getInstance().readInputStreamIntoFile(httpResponse.getEntity().getContent(), file);
        fileCacher.updateSize(url, file.length());
        return file;
    }
}
