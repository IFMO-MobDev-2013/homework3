package ru.georgeee.android.Silencio.utility.GUI;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ru.georgeee.android.Silencio.R;

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
    public void init(String searchRequest){
        this.searchRequest = searchRequest;
        list.clear();
        count = 0;
        addItems(10);
    }

    public void addItem(){
        list.add(new TwoPicturesModel(count, searchRequest));
        count++;
    }

    public void addItems(int count) {
        for (int i = 0; i < count; i++){
            addItem();
        }
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
        count = 0;
    }



    static class ViewHolder {
        protected ImageView left_image;
        protected ImageView right_image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.rowlayout, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.left_image = (ImageView)view.findViewById(R.id.icon_left);
            viewHolder.right_image = (ImageView)view.findViewById(R.id.icon_right);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.left_image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher));
        holder.right_image.setImageBitmap(list.get(position).getRightImage());

        //this should be removed
        {
            TextView label = (TextView)view.findViewById(R.id.label);
            label.setText(searchRequest);
        }

        return view;
    }
}
