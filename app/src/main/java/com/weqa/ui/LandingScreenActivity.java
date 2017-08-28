package com.weqa.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.weqa.R;

import org.w3c.dom.Text;

public class LandingScreenActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ItemTypePagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private final static String[] itemTypeHeadings = {"Desks", "Meeting Rooms", "Conference Rooms", "Focus Rooms"};

    private int previousTabPosition = 0;

    private Typeface fontAwesome, fontLatoReg, fontLatoBlack, fontLatoLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new ItemTypePagerAdapter(getFragmentManager(), itemTypeHeadings);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPagerContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setBackgroundColor(ContextCompat.getColor(this, R.color.colorLighterGrey));
        mViewPager.setCurrentItem(0);

        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        fontAwesome = Typeface.createFromAsset(this.getAssets(), "font/fontawesome-webfont.ttf");
        fontLatoBlack = Typeface.createFromAsset(this.getAssets(), "font/Lato-Black.ttf");
        fontLatoLight = Typeface.createFromAsset(this.getAssets(), "font/Lato-Light.ttf");
        fontLatoReg = Typeface.createFromAsset(this.getAssets(), "font/Lato-Regular.ttf");

        changeTabsFont();

        Button qrButton = (Button) findViewById(R.id.qrButton);
        qrButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) mTabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(fontLatoReg);
                }
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_ITEM_TYPE = "item_type";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int itemTypeNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_ITEM_TYPE, itemTypeNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int itemTypeNumber = getArguments().getInt(ARG_ITEM_TYPE);
            View rootView = inflater.inflate(R.layout.fragment_summary_floorplan, container, false);

            TextView floorNumberText = (TextView) rootView.findViewById(R.id.floorNumber);
            floorNumberText.setText("Floor " + itemTypeNumber);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the itemTypes/tabs/pages.
     */
    public class ItemTypePagerAdapter extends FragmentPagerAdapter {

        String[] itemTypeHeadings;

        public ItemTypePagerAdapter(FragmentManager fm, String[] itemTypeHeadings) {
            super(fm);
            this.itemTypeHeadings = itemTypeHeadings;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return this.itemTypeHeadings.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.itemTypeHeadings[position];
        }
    }

}
