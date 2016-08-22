package com.gaih.oomusic.Service;

import com.gaih.oomusic.Adapter.Music;

import java.util.ArrayList;

/**
 * Created by gaih on 2016/7/27.
 */

public interface Iservice {
    public void callPlayMusic(int position, ArrayList<Music> mList);
    public void callPauseMusic();
    public void callReplayMusic();
    public void callSeekToPosition(int position);



    }
