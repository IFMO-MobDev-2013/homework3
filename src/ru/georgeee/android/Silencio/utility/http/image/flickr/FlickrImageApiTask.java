package ru.georgeee.android.Silencio.utility.http.image.flickr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiResult;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiTask;
import ru.georgeee.android.Silencio.utility.http.translate.TranslateResult;

import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 22:41
 * To change this template use File | Settings | File Templates.
 */
public class FlickrImageApiTask extends ImageApiTask {
    protected String apiKey;

    public FlickrImageApiTask(String apiKey, String searchText, int pageNum) {
        this.apiKey = apiKey;
        this.searchText = searchText;
        this.pageNumber = pageNum;
    }

    public FlickrImageApiTask(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected FlickrImageApiResult getResultByJson(JSONObject jObject) {
        FlickrImageApiResult result = new FlickrImageApiResult();
        try {
            JSONObject jsonObject = jObject.getJSONObject("photos");
            result.setPageCount(jsonObject.getInt("pages"));
            result.setPageNumber(jsonObject.getInt("page"));
            result.setTotalImageCount(jsonObject.getInt("total"));
            JSONArray imageJSONArray = jsonObject.getJSONArray("photo");
            FlickrImageApiResult.FlickrImage[] images = new FlickrImageApiResult.FlickrImage[imageJSONArray.length()];
            for(int i=0; i<images.length; ++i){
                images[i] = new FlickrImageApiResult.FlickrImage();
                images[i].parseFromJSON(imageJSONArray.getJSONObject(i));
            }
            result.setImages(images);
        } catch (JSONException e) {
            handleJSONException(e);
            return null;
        }
        return result;
    }

    @Override
    protected String getUrl() {
        String url = null;
        Map<String, String> getParams = new Hashtable<String, String>();
        getParams.put("method", "flickr.photos.search");
        getParams.put("api_key", apiKey);
        getParams.put("text", searchText);
        if(pageNumber != null) getParams.put("page", pageNumber.toString());
        getParams.put("format", "json");
        getParams.put("nojsoncallback", "1");
        try {
            url = composeUrl("http://api.flickr.com/services/rest/", getParams);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
