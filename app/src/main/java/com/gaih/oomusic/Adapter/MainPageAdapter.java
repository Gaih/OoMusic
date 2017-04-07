package com.gaih.oomusic.Adapter;

import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaih.oomusic.R;

import java.util.ArrayList;

/**
 * Created by gaih on 2016/8/5.
 */

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.ViewHolder> {

    private ArrayList<MainPager> mData;
    public MainPageAdapter(ArrayList<MainPager> mData){
        this.mData = mData;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(mData.get(position).getName());
        holder.singer.setText(mData.get(position).getIntro());
        holder.mImageView.setImageURI(Uri.parse(mData.get(position).getCover()));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private OnItemClickListener itemClickListener;
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView name;
        private TextView singer;
        private ImageView mImageView;
        private CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.cardView);
            name = (TextView) itemView.findViewById(R.id.name);
            singer = (TextView)itemView.findViewById(R.id.singer);
            mImageView = (ImageView) itemView.findViewById(R.id.iv);
            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener !=null){
                itemClickListener.onItemClick(v,getPosition());
            }
        }}
}
