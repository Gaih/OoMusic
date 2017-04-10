package com.gaih.oomusic.Adapter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaih on 2016/8/4.
 */
public class MainPager implements Parcelable {
    private String name;
    private String intro;
    private Bitmap cover;
    private String id;


    public MainPager() {

    }


    public MainPager(String id, String name, String intro, Bitmap cover) {
        this.name = name;
        this.intro = intro;
        this.cover = cover;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getIntro() {
        return intro;
    }

    public Bitmap getCover() {
        return cover;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(intro);
        dest.writeString(id);
        cover.writeToParcel(dest, 0);


    }

    public static final Parcelable.Creator<MainPager> CREATOR = new Creator<MainPager>() {

        @Override
        public MainPager createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            MainPager p = new MainPager();
            p.setName(source.readString());
            p.setIntro(source.readString());
            p.setId(source.readString());

            p.cover = Bitmap.CREATOR.createFromParcel(source);

            return p;
        }

        @Override
        public MainPager[] newArray(int size) {
            // TODO Auto-generated method stub
            return new MainPager[size];
        }
    };
}
