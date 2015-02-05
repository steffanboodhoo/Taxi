package main.code.taxi.taxi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import main.code.ui.SlidingTabLayout;

/**
 * Created by Steffan on 05/02/2015.
 */
public class FragmentSlidingTabsMain extends Fragment{


    private ViewPager mViewPager;
    protected ArrayList<FragItem> fragments;
    private SlidingTabLayout mSlidingTabLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(false);

        fragments=new ArrayList<FragItem>();
        populateList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("Fragment", "Successfully created View");
        return inflater.inflate(R.layout.fragment_main_sliding, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("Fragment", "On View Created");
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_tabs_main);
        mViewPager.setAdapter(new PageAdapter(getChildFragmentManager()));
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs_main);
        mSlidingTabLayout.setViewPager(mViewPager);

        // BEGIN_INCLUDE (tab_colorizer)
        // Set a TabColorizer to customize the indicator and divider colors. Here we just retrieve
        // the tab at the position, and return it's set color
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return fragments.get(position).getColour();
            }

            @Override
            public int getDividerColor(int position) {
                return fragments.get(position).getColour();
            }

        });
    }
    private void populateList(){
        Fragment frag1=null;
        Fragment frag2=null;

//        fragments.add(new FragItem(frag1, Color.BLUE,"Frag1"));
//        fragments.add(new FragItem(frag2,Color.GREEN,"Frag2"));
    }

    protected class PageAdapter extends FragmentPagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position).getFrag();
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }
    }
    protected class FragItem{
        private Fragment frag;
        private int colour;
        private String title;
        public FragItem(Fragment frag,int colour,String title){
            this.frag=frag;
            this.colour=colour;
            this.title=title;
        }
        public Fragment getFrag() {
            return frag;
        }
        public int getColour() {
            return colour;
        }
        public String getTitle(){
            return title;
        }
    }
}

