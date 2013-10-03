package ru.georgeee.android.Silencio.utility.GUI;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ru.georgeee.android.Silencio.R;
import ru.georgeee.android.Silencio.utility.cacher.FileCacher;
import ru.georgeee.android.Silencio.utility.http.image.ImageApiResult;
import ru.georgeee.android.Silencio.utility.http.image.flickr.FlickrImageApiTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marsermd
 * Date: 02.10.13
 * Time: 2:13
 * To change this template use File | Settings | File Templates.
 */
public class PicturesAdapter extends BaseAdapter {
    private List<TwoPicturesModel> list;

    private String FLICKR_API_KEY;
    private FlickrImageApiTask imageTask;
    private List<ImageApiResult.Image> images;
    private List<FlickrImageApiTask> imageTasks;
    private int page = 0;
    private final int IMAGES_PER_PAGE = 100;

    public void init(String searchRequest){
        this.searchRequest = searchRequest;
        images.clear();

        for (FlickrImageApiTask task:imageTasks) {
            task.cancel(true);
        }
        imageTasks.clear();

        for (TwoPicturesModel model:list) {
            model.cancel();
        }
        list.clear();

        page = 0;
        count = 0;
        loadMore(10);
    }

    public void loadMore(int position){
        if (page * IMAGES_PER_PAGE / 2 > position)
            return;
        page++;
        Log.e("wow", searchRequest);
        Log.e("wow", FLICKR_API_KEY);
        imageTask = new FlickrImageApiTask(FLICKR_API_KEY, searchRequest, page){
            @Override
            protected void onPostExecute(ImageApiResult imageApiResult) {
                ImageApiResult.Image[] resultImages = imageApiResult.getImages();
                for (int i = 0; i < resultImages.length; i++){
                    images.add(resultImages[i]);
                }
                for (int i = 0; i < resultImages.length/2; i++){
                    addItem();
                }
                imageTasks.remove(this);
                notifyDataSetChanged();
            }
        };
        imageTasks.add(imageTask);
        imageTask.executeOnHttpTaskExecutor();
    }

    public void addItem(){
        TwoPicturesModel item = new TwoPicturesModel(images.get(count * 2), images.get(count * 2 + 1));
        item.download();
        list.add(item);
        count++;
    }

    private Activity context;

    private String searchRequest;

    public void setSearchRequest(String searchRequest) {
        this.searchRequest = searchRequest;
    }

    private int count;

    @Override
    public int getCount() {
        return count;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    public PicturesAdapter(Activity context, String searchRequest) {
        super();
        this.context = context;
        this.searchRequest = searchRequest;
        list = new LinkedList<TwoPicturesModel>();
        images = new ArrayList<ImageApiResult.Image>();
        imageTasks = new LinkedList<FlickrImageApiTask>();
        count = 0;

        FLICKR_API_KEY = context.getString(R.string.flickr_api_key);
    }

    private class ViewHolder{
        ImageView leftView, rightView;
        TwoPicturesModel model;
        public ViewHolder(ImageView leftView, ImageView rightView) {
            this.leftView = leftView;
            this.rightView = rightView;
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        ViewHolder holder;
        if (convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.rowlayout, null);
            ImageView leftView = (ImageView)view.findViewById(R.id.icon_left);
            ImageView rightView = (ImageView)view.findViewById(R.id.icon_right);
            ViewHolder viewHolder = new ViewHolder(leftView, rightView);
            view.setTag(viewHolder);
            holder = viewHolder;
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
            if (holder.model != null)
                holder.model.cancel();
        }
        holder.model = list.get(position);
        holder.model.setViews(holder.leftView, holder.rightView);

        return view;
    }
}
