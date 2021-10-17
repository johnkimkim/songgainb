package com.tistory.starcue.songgainb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    private ViewHolder viewHolder;
    private SparseBooleanArray mSelectedItemsIds;

    Context mContext;
    LayoutInflater layoutInflater;
    List<SongModel> songList;
    ArrayList<SongModel> mList;
    TextView _url;
    TextView _name;
    TextView _time;
    ImageView _img;

    public ListAdapter(Context mContext, ArrayList<SongModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //position = 행의 index, convertView = 행 전체를 나타내는 뷰를 의미, parent = 어댑터를 가지고있는 부모의 뷰를 의미
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.listview_item, null);
            viewHolder.relativeLayout = (RelativeLayout) view.findViewById(R.id.listview_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

//        View view = View.inflate(mContext, R.layout.listview_item, null);
        _url = (TextView) view.findViewById(R.id.song_url);
        _name = (TextView) view.findViewById(R.id.song_name);
        _time = (TextView) view.findViewById(R.id.song_time);
        _img = (ImageView) view.findViewById(R.id.song_thumbnail);

        _url.setText(mList.get(position).get_url());
        _name.setText(mList.get(position).get_name());
        _time.setText(mList.get(position).get_time());

        Bitmap bitmap = BitmapFactory.decodeByteArray(mList.get(position).get_img(), 0, mList.get(position).get_img().length);
        _img.setImageBitmap(bitmap);

        final SongModel listViewItem = (SongModel) getItem(position);

        return view;
    }





    public class ViewHolder{
        private RelativeLayout relativeLayout = null;
    }

}
