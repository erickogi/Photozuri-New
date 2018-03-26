package com.photozuri.photozuri.Views.V1.Preview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.ViewGroup;

import com.photozuri.photozuri.Data.Models.MyImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eric on 1/26/2018.
 */

public class WmtsCardFragmentPageAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<WmtsCardFrament> fragments;
    private float baseElevation;
    private ArrayList<MyImage> myImages;
    private int no;

    public WmtsCardFragmentPageAdapter(FragmentManager fm, float baseElevation, ArrayList<MyImage> myImages, int no) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        this.myImages = myImages;
        this.no = no;


        for (int i = 0; i < no; i++) {
            Log.d("imagesasof", " adapter" + myImages.get(i).getPath());
            addCardFragment(new WmtsCardFrament());
        }
    }

    @Override
    public float getBaseElevation() {
        return baseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return fragments.get(position).getCardView();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {


        return WmtsCardFrament.getInstance(position, myImages.get(position), no);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (WmtsCardFrament) fragment);
        return fragment;
    }

    public void addCardFragment(WmtsCardFrament fragment) {
        fragments.add(fragment);
    }

}
