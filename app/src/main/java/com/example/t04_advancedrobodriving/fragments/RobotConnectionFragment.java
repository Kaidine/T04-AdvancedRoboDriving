package com.example.t04_advancedrobodriving.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.t04_advancedrobodriving.R;
import com.example.t04_advancedrobodriving.databinding.FragmentRobotConnectionBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RobotConnectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotConnectionFragment extends Fragment {

    FragmentRobotConnectionBinding binding;
    public RobotConnectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RobotConnectionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotConnectionFragment newInstance() {
        return new RobotConnectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRobotConnectionBinding.inflate(inflater, container, false);
        return binding.getRoot();`
    }
}