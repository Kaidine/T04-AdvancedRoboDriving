package com.example.t04_advancedrobodriving.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.t04_advancedrobodriving.R;
import com.example.t04_advancedrobodriving.databinding.FragmentCollectionRobotControlBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CollectionRobotControlFragment extends Fragment {

    RobotControlCollectionAdapter collectionAdapter;
    ViewPager2 viewPager;
    FragmentCollectionRobotControlBinding binding;
    private List<String> tabNames;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCollectionRobotControlBinding.inflate(inflater, container, false);

        tabNames = List.of(
                getString(R.string.connection_tab_label),
                getString(R.string.drive_tab_label),
                getString(R.string.sound_tab_label),
                getString(R.string.sensor_tab_label)
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        collectionAdapter = new RobotControlCollectionAdapter(this);
        binding.viewPager.setAdapter(collectionAdapter);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(tabNames.get(position))
        ).attach();
    }

}