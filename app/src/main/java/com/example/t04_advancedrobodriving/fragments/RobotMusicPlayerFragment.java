package com.example.t04_advancedrobodriving.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.t04_advancedrobodriving.MainNavigationActivity;
import com.example.t04_advancedrobodriving.Note;
import com.example.t04_advancedrobodriving.R;
import com.example.t04_advancedrobodriving.databinding.FragmentMusicPlayerBinding;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;
import com.example.t04_advancedrobodriving.systemServiceWrappers.PresetTunePlayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RobotMusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotMusicPlayerFragment extends Fragment {

    FragmentMusicPlayerBinding binding;
    private EV3ControllerService robotControllerService;
    private int volume;

    public RobotMusicPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment musicPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotMusicPlayerFragment newInstance() {
        RobotMusicPlayerFragment fragment = new RobotMusicPlayerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result ->
                        robotControllerService = EV3ControllerService.instance()
        );
        activityResultLauncher.launch(new String[]{Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT});

        binding = FragmentMusicPlayerBinding.inflate(inflater, container, false);

        this.volume = 50;
        binding.volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volume = i;
                binding.volumeSliderLabel.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.playSoundFileButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                robotControllerService.playSoundFile(volume);
            }
            return false;
        });

        binding.playPreProgrammedTuneButton.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
                executor.submit(() -> PresetTunePlayer.playTwoSpanishDancesOstransky(volume, robotControllerService));

            }
            return false;
        });

        initializeKeyboardKey(binding.cKey, Note.C_4, R.color.white);
        initializeKeyboardKey(binding.cSharpKey, Note.C_SHARP_4, R.color.black);
        initializeKeyboardKey(binding.dKey, Note.D_4, R.color.white);
        initializeKeyboardKey(binding.dSharpKey, Note.D_SHARP_4, R.color.black);
        initializeKeyboardKey(binding.eKey, Note.E_4, R.color.white);
        initializeKeyboardKey(binding.fKey, Note.F_4, R.color.white);
        initializeKeyboardKey(binding.fSharpKey, Note.F_SHARP_4, R.color.black);
        initializeKeyboardKey(binding.gKey, Note.G_4, R.color.white);
        initializeKeyboardKey(binding.gSharpKey, Note.G_SHARP_4, R.color.black);
        initializeKeyboardKey(binding.aKey, Note.A_4, R.color.white);
        initializeKeyboardKey(binding.aSharpKey, Note.A_SHARP_4, R.color.black);
        initializeKeyboardKey(binding.bKey, Note.B_4, R.color.white);

        return binding.getRoot();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeKeyboardKey(AppCompatButton button, Note frequency, int initialColor) {
        button.setBackgroundColor(getResources().getColor(initialColor));


        button.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                button.setBackgroundColor(getResources().getColor(R.color.teal_700));
                robotControllerService.startPlayingTone(frequency.getFrequency(), volume);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                if (initialColor == R.color.black) {
                    button.bringToFront();
                }
                button.setBackgroundColor(getResources().getColor(initialColor));
                robotControllerService.stopPlayingTone();
            }
            fixButtonLayering();
            return false;
        });
    }

    public void fixButtonLayering() {
        binding.cKey.setZ(2);
        binding.cSharpKey.setZ(20);
        binding.dKey.setZ(2);
        binding.dSharpKey.setZ(20);
        binding.eKey.setZ(2);
        binding.fKey.setZ(2);
        binding.fSharpKey.setZ(20);
        binding.gKey.setZ(2);
        binding.gSharpKey.setZ(20);
        binding.aKey.setZ(2);
        binding.aSharpKey.setZ(20);
        binding.bKey.setZ(2);

    }
}