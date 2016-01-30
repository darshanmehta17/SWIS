package co.swisapp.swis.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v13.app.FragmentCompat;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import co.swisapp.swis.R;
import co.swisapp.swis.fragment.MainDiscoverFragment;
import co.swisapp.swis.fragment.MainUserFragment;
import co.swisapp.swis.fragment.MainVideoFragment;

public class MainSessionActivity extends FragmentActivity{

    ViewPager viewPager ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_session);

        viewPager = (ViewPager) findViewById(R.id.main_pager) ;

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public android.app.Fragment getItem(int position) {
                switch (position){
                    case 0: return new MainDiscoverFragment() ;
                    case 1: return new MainVideoFragment() ;
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
}
