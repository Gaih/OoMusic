package com.gaih.oomusic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaih.oomusic.R;

import java.util.List;

/**
 * Created by gaih on 2016/8/5.
 */

public class MusicAdapter extends ArrayAdapter<Music> {

    private int id;
    public MusicAdapter(Context context, int resource, List<Music> objects) {
        super(context, resource,objects);
        id = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music music = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(id,null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView)view.findViewById(R.id.name);
            viewHolder.singer = (TextView)view.findViewById(R.id.singer);
            viewHolder.iv = (ImageView)view.findViewById(R.id.iv);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder= (ViewHolder)view.getTag();
        }

        viewHolder.iv.setImageBitmap(music.getBitmap());
        viewHolder.name.setText(music.getName());
        viewHolder.singer.setText(music.getSinger());

        return view;
    }
}
class ViewHolder{
    ImageView iv;
    TextView name;
    TextView singer;
}
