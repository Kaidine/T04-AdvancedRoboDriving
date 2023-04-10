package com.example.t04_advancedrobodriving.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.t04_advancedrobodriving.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RobotSensorPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotSensorPageFragment extends Fragment {

    public RobotSensorPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RobotSensorPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotSensorPageFragment newInstance() {
        return new RobotSensorPageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_robot_sensor_page, container, false);
    }
}