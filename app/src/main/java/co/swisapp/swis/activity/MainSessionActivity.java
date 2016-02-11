package co.swisapp.swis.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import co.swisapp.swis.R;
import co.swisapp.swis.fragment.MainDiscoverFragment;
import co.swisapp.swis.fragment.MainUserFragment;
import co.swisapp.swis.fragment.MainVideoFragment;
import co.swisapp.swis.fragment.MainVideoFragmentCompat;
import co.swisapp.swis.utility.CameraHelper;
import co.swisapp.swis.utility.Constants;

/**
 * OnPageChange listener to assign specific action for each fragment of viewpage
 */
public class MainSessionActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    /**
     * Main activity which contains the viewpager.
     * Function is used to initialize adapter and setup activity.
     *
     * @param savedInstanceState -
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_session);

        initialize();

        viewPager.addOnPageChangeListener(this);

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public android.app.Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new MainDiscoverFragment();
                    case 1:
                        if (Constants.API_LEVEL >= 21) {
                            return new MainVideoFragment();
                        } else {
                            return new MainVideoFragmentCompat();
                        }
                    case 2:
                        return new MainUserFragment();
                    default:
                        return null;
                }
            }
            @Override
            public int getCount() {
                return 3;
            }
        };

        viewPager.setAdapter(adapter);

    }

    private void initialize(){

        viewPager = (ViewPager) findViewById(R.id.main_pager);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
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
        if (position == 1) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
        if(position!=1 && CameraHelper.isRecordingGetter()){
            MainVideoFragment.FragmentChangeHandler();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
