package com.gaih.oomusic.Adapter;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gaih on 2016/8/4.
 */
public class Music implements Parcelable {
    private String name;
    private String singer;
    private Bitmap bitmap;
    private String uri;
    private long album;

    public Music(){

    }

    public Music(String name, String singer, String uri, Bitmap bitmap, long album) {
        this.name = name;
        this.singer = singer;
        this.bitmap = bitmap;
        this.uri = uri;
        this.album = album;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAlbum() {
        return album;
    }

    public void setAlbum(long album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public String getSinger() {
        return singer;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(singer);
        dest.writeString(uri);
        dest.writeLong(album);
        bitmap.writeToParcel(dest, 0);


    }

    public static final Parcelable.Creator<Music> CREATOR = new Creator<Music>(){

        @Override
        public Music createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            Music p = new Music();
            p.setName(source.readString());
            p.setSinger(source.readString());
            p.setUri(source.readString());
            p.setAlbum(source.readLong());
            p.bitmap = Bitmap.CREATOR.createFromParcel(source);

            return p;
        }

        @Override
        public Music[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Music[size];
        }
    };
}
