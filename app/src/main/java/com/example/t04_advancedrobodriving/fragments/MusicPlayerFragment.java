package com.example.t04_advancedrobodriving.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.t04_advancedrobodriving.MainNavigationActivity;
import com.example.t04_advancedrobodriving.Note;
import com.example.t04_advancedrobodriving.R;
import com.example.t04_advancedrobodriving.databinding.FragmentMusicPlayerBinding;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MusicPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MusicPlayerFragment extends Fragment {

    FragmentMusicPlayerBinding binding;
    private EV3ControllerService robotControllerService;
    private int volume;

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment musicPlayerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MusicPlayerFragment newInstance() {
        MusicPlayerFragment fragment = new MusicPlayerFragment();
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
        String robotName = getString(R.string.robot_name);

        ActivityResultLauncher<String[]> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> robotControllerService = MainNavigationActivity.getRobotService(getContext(), robotName));
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

        initializeKeyboardKey(binding.cKey, Note.C, R.color.white);
        initializeKeyboardKey(binding.cSharpKey, Note.C_SHARP, R.color.black);
        initializeKeyboardKey(binding.dKey, Note.D, R.color.white);
        initializeKeyboardKey(binding.dSharpKey, Note.D_SHARP, R.color.black);
        initializeKeyboardKey(binding.eKey, Note.E, R.color.white);
        initializeKeyboardKey(binding.fKey, Note.F, R.color.white);
        initializeKeyboardKey(binding.fSharpKey, Note.F_SHARP, R.color.black);
        initializeKeyboardKey(binding.gKey, Note.G, R.color.white);
        initializeKeyboardKey(binding.gSharpKey, Note.G_SHARP, R.color.black);
        initializeKeyboardKey(binding.aKey, Note.A, R.color.white);
        initializeKeyboardKey(binding.aSharpKey, Note.A_SHARP, R.color.black);
        initializeKeyboardKey(binding.bKey, Note.B, R.color.white);

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
            return false;
        });
    }
}