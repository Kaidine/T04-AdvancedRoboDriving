package com.example.t04_advancedrobodriving.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RobotControlCollectionAdapter extends FragmentStateAdapter {

    public RobotControlCollectionAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return RobotConnectionFragment.newInstance();
            case 1:
                return RobotDrivingFragment.newInstance();
            case 2:
                return RobotMusicPlayerFragment.newInstance();
            case 3:
            default:
                return RobotSensorPageFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
