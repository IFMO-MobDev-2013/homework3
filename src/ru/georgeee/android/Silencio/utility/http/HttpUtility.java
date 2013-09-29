package ru.georgeee.android.Silencio.utility.http;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import ru.georgeee.android.Silencio.utility.cacher.FileCacher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtility {
    private static HttpUtility ourInstance = new HttpUtility();
    private HttpClient defaultHttpClient = null;
    private Executor translateApiExecutor = null;
    private Executor imageApiExecutor = null;
    private Executor downloadExecutor = null;

    private HttpUtility() {
    }

    public static HttpUtility getInstance() {
        return ourInstance;
    }

    public Executor getTranslateApiExecutor() {
        if (translateApiExecutor == null) {
            translateApiExecutor = Executors.newCachedThreadPool();
        }
        return translateApiExecutor;
    }

    public Executor getImageApiExecutor() {
        if (imageApiExecutor == null) {
            imageApiExecutor = Executors.newCachedThreadPool();
        }
        return imageApiExecutor;
    }

    public Executor getDownloadExecutor() {
        if (downloadExecutor == null) {
            downloadExecutor = Executors.newCachedThreadPool();
        }
        return downloadExecutor;
    }

    public HttpClient getMultiThreadHttpClient() {
        HttpParams params = new BasicHttpParams();
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, registry);
        if (defaultHttpClient == null) defaultHttpClient = new DefaultHttpClient(connectionManager, params);
        return defaultHttpClient;
    }

    public void readInputStreamIntoFile(InputStream inputStream, File file) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(file);
        try {
            if (!file.exists()) file.createNewFile();
            int read = 0;
            byte[] bytes = new byte[1024 * 1024]; //1MB

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw e;
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    throw e;
                }

            }
        }
    }
}
