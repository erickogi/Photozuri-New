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

public class CardFragmentPagerAdapter extends FragmentStatePagerAdapter implements CardAdapter {

    private List<CardFragment> fragments;
    private float baseElevation;
    private ArrayList<MyImage> myImages;
    private int no;

    public CardFragmentPagerAdapter(FragmentManager fm, float baseElevation, ArrayList<MyImage> myImages, int no) {
        super(fm);
        fragments = new ArrayList<>();
        this.baseElevation = baseElevation;
        this.myImages = myImages;
        this.no = no;

        int noOfCardsF = no / 4;
        // x%y
        int remainder = no % 4;
        if (remainder > 0) {
            noOfCardsF = noOfCardsF + 1;
        }


        for (int i = 0; i < noOfCardsF; i++) {
            Log.d("imagesasof", " adapter" + myImages.get(i).getPath());
            addCardFragment(new CardFragment());
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
        int positionq = position * 4;

        ArrayList<MyImage> myImagess = new ArrayList<>();
        // MyImage[] images=new MyImage[4];
        for (int a = 0; a < 4; a++) {
            try {
                // images[a] = myImages.get(positionq + a);
                myImagess.add(this.myImages.get(positionq + a));
            } catch (Exception nm) {
                nm.printStackTrace();
            }
        }


        return CardFragment.getInstance(position, myImagess, no);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object fragment = super.instantiateItem(container, position);
        fragments.set(position, (CardFragment) fragment);
        return fragment;
    }

    public void addCardFragment(CardFragment fragment) {
        fragments.add(fragment);
    }

}
