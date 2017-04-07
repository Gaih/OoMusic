package com.gaih.oomusic.Adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;

import android.support.v13.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.gaih.oomusic.Fragment.Fragment01;
import com.gaih.oomusic.Fragment.Fragment02;
import com.gaih.oomusic.Fragment.Fragment03;
import com.gaih.oomusic.Fragment.FragmentFM;

import java.util.ArrayList;

/**
 * Created by gaih on 2016/8/21.
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private static final String[] mTitles = {"豆瓣FM", "播放列表", "本地音乐"};
    private ArrayList<Fragment> datas;

    public FragmentAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
        datas = new ArrayList<>();
        Fragment01 tab01 = new Fragment01();
        Fragment02 tab02 = new Fragment02();
        FragmentFM tab03 = new FragmentFM();
        datas.add(tab03);
        datas.add(tab02);
        datas.add(tab01);
    }

    @Override
    public Fragment getItem(int position) {
        return datas.get(position);
    }


    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        //super.destroyItem(container, position, object);
    }
}
