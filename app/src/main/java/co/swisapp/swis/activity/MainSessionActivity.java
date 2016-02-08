package co.swisapp.swis.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import co.swisapp.swis.R;
import co.swisapp.swis.fragment.MainDiscoverFragment;
import co.swisapp.swis.fragment.MainUserFragment;
import co.swisapp.swis.fragment.MainVideoFragment;
import co.swisapp.swis.fragment.MainVideoFragmentCompat;
import co.swisapp.swis.utility.Constants;

/**
 * Onpagechange listener of viewpager is implemented so as to switch between fullscreen mode
 * and normal mode i.e. show status bar mode, while scrolling in the viewpager.
 */
public class MainSessionActivity extends FragmentActivity implements ViewPager.OnPageChangeListener{

    private ViewPager viewPager ;
    private int mPosition ;

    /**
     * Main activity which contains the viewpager.
     * Function is used to initialize adapter and setup activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.PACKAGE_NAME = getApplicationContext().getPackageName() ;
        setContentView(R.layout.activity_main_session);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        viewPager = (ViewPager) findViewById(R.id.main_pager) ;

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public android.app.Fragment getItem(int position) {
                switch (position){
                    case 0: return new MainDiscoverFragment() ;
                    case 1: return new MainVideoFragmentCompat();
                        /*if (Constants.API_LEVEL >= 21) { return new MainVideoFragment(); }
                        else { return new MainVideoFragmentCompat(); }*/
                    case 2: return new MainUserFragment() ;
                    default: return null ;
                }

            }

            @Override
            public int getCount() {
                return 3;
            }
        };

        viewPager.setAdapter(adapter);
    }

    /**
     * The app resumes with the camera fragment always.
     */
    @Override
    protected void onResume() {
        super.onResume();
        viewPager.setCurrentItem(1, true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewPager.setCurrentItem(1, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mPosition = position;
    }

    /**
     * Starts immersive mode as soon as the scroll state of the viewpager is idle, the statusbar is displayed
     * when other fragments are opened.
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        switch(state) {
            case ViewPager.SCROLL_STATE_IDLE:
                if(mPosition == 1) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    WindowManager.LayoutParams params = getWindow().getAttributes();
                    params.flags |= WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                }
        }
    }
}
