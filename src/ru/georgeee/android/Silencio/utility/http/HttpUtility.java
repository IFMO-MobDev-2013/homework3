package ru.georgeee.android.Silencio.utility.http;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

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

    public static HttpUtility getInstance() {
        return ourInstance;
    }

    private HttpUtility() {
    }

    private Executor translateExecutor = null;

    public Executor getTranslateExecutor() {
        if (translateExecutor == null) {
            translateExecutor = Executors.newCachedThreadPool();
        }
        return translateExecutor;
    }

    public HttpClient getMultiThreadHttpClient() {
        HttpParams params = new BasicHttpParams();
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager connectionManager = new ThreadSafeClientConnManager(params, registry);
        if (defaultHttpClient == null) defaultHttpClient = new DefaultHttpClient(connectionManager, params);
        return defaultHttpClient;
    }

}
