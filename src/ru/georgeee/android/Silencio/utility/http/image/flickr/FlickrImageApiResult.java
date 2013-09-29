package ru.georgeee.android.Silencio.utility.http.image.flickr;

import org.json.JSONException;
import org.json.JSONObject;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiResult;

/**
 * Created with IntelliJ IDEA.
 * User: georgeee
 * Date: 29.09.13
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public class FlickrImageApiResult extends ImageApiResult {

    public static class FlickrImage extends Image {
        protected String id;
        protected String owner;
        protected String secret;
        protected String server;
        protected long farm;
        protected String title;
        protected boolean isPublic;
        protected boolean isFriend;
        protected boolean isFamily;

        public String getTitle() {
            return title;
        }

        public void parseFromJSON(JSONObject params) throws JSONException {
            id = params.getString("id");
            owner = params.getString("owner");
            secret = params.getString("secret");
            server = params.getString("server");
            farm = params.getLong("farm");
            title = params.getString("title");
            isPublic = params.getInt("isPublic") == 1;
            isFriend = params.getInt("isFriend") == 1;
            isFamily = params.getInt("isFamily") == 1;
        }

        protected String getImageUrl(String sizeMarker){
            return "http://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+"_"+sizeMarker+".jpg";
        }

        @Override
        public String getSmallImageUrl() {
            return getImageUrl("q");
        }

        @Override
        public String getLargeImageUrl() {
            return getImageUrl("b");
        }
    }
}
