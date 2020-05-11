package com.csulb.tessuro.views.dashboard.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.csulb.tessuro.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private ViewPager profile_viewPager;
    private TabLayout profile_tabLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_viewPager = view.findViewById(R.id.profile_viewPager);
        profile_tabLayout = view.findViewById(R.id.profile_tabLayout);

        // fragments for each tab
        ProfileInfoFragment profileInfoFragment = new ProfileInfoFragment();
        ProfileUpdateFragment profileUpdateFragment = new ProfileUpdateFragment();

        // combine tab layout and view pager together
        profile_tabLayout.setupWithViewPager(profile_viewPager);

        // create the view page adapter and attach the child frag manager
        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getChildFragmentManager(), 0);
        viewPageAdapter.addFragment(profileInfoFragment, "Info");
        viewPageAdapter.addFragment(profileUpdateFragment, "Update");
        profile_viewPager.setAdapter(viewPageAdapter);

        return view;
    }


    private static class ViewPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList = new ArrayList<>();
        private List<String> fragmentTitles = new ArrayList<>();

        ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }
    }
}
