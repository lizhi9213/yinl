package com.desjf.dsjr.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by lwp940118 on 2016/11/25.
 */
public class TabFragmentShouYeAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragments;
    private List<String> strings;
    private FragmentManager fragmentManager;



    public TabFragmentShouYeAdapter(List<Fragment> fragments, List<String> strings, FragmentManager fragmentManager, Context context){
        super(fragmentManager);
        this.strings = strings;
        this.context = context;
        this.fragmentManager=fragmentManager;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
//        Fragment f[]=new Fragment[fragments.size()];
//
//        //在这里判断如果Fragmnet[position]当前页面没有加载的话我们再次在上面加载
//        if(f[position]==null){
//            //这里只是写一个构造方法把值传到fragment里面
//            Fragment my = fragments.get(position);
//            f[position]=my;
//        }
//        return f[position];
        return fragments.get(position);
    }


    @Override
    public int getCount() {
        return strings.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strings.get(position);
    }
}
