package com.gaih.oomusic.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.gaih.oomusic.Fragment.Fragment01;
import com.gaih.oomusic.Fragment.Fragment02;
import com.gaih.oomusic.Fragment.Fragment03;

import java.util.ArrayList;

/**
 * Created by gaih on 2016/8/21.
 */

public class FragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private Context context;
    private static final String[] mTitles = {"本地音乐", "个人中心", "在线音乐"};
    private ArrayList<Fragment> datas;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        datas = new ArrayList<>();
        Fragment01 tab01 = new Fragment01();
        Fragment02 tab02 = new Fragment02();
        Fragment03 tab03 = new Fragment03();
        datas.add(tab01);
        datas.add(tab02);
        datas.add(tab03);
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
}
