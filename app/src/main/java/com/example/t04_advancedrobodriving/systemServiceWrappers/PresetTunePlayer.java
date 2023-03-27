package com.example.t04_advancedrobodriving.systemServiceWrappers;

import android.os.SystemClock;

import com.example.t04_advancedrobodriving.Note;
import com.example.t04_advancedrobodriving.services.EV3ControllerService;

public class PresetTunePlayer {
    private PresetTunePlayer() {
    }

    public static void playTwoSpanishDancesOstransky(int volume, EV3ControllerService robotControllerService) {
        //60 bpm, 4/4 time
        int quarterNote = 1000;
        int dottedHalfNote = quarterNote * 3;
        int dottedQuarterNote = (int) Math.floor(quarterNote * 1.5);
        int eighthNote = quarterNote / 2;
        int sixteenthNote = quarterNote / 4;

        //bar 1
        playNote(Note.D_5, volume, robotControllerService);
        SystemClock.sleep(dottedQuarterNote);
        playNote(Note.C_5, volume, robotControllerService);
        SystemClock.sleep(sixteenthNote);
        playNote(Note.A_SHARP_4, volume, robotControllerService);
        SystemClock.sleep(sixteenthNote);
        playNote(Note.A_4, volume, robotControllerService);
        SystemClock.sleep(quarterNote);
        playNote(Note.D_4, volume, robotControllerService);
        SystemClock.sleep(quarterNote);

        //bar 2
        playNote(Note.D_5, volume, robotControllerService);
        SystemClock.sleep(dottedQuarterNote);
        playNote(Note.C_5, volume, robotControllerService);
        SystemClock.sleep(sixteenthNote);
        playNote(Note.A_SHARP_4, volume, robotControllerService);
        SystemClock.sleep(sixteenthNote);
        playNote(Note.A_4, volume, robotControllerService);
        SystemClock.sleep(quarterNote);
        playNote(Note.F_4, volume, robotControllerService);
        SystemClock.sleep(quarterNote);

        //bar 3
        playNote(Note.G_4, volume, robotControllerService);
        SystemClock.sleep(quarterNote);
        playNote(Note.A_4, volume, robotControllerService);
        SystemClock.sleep(eighthNote);
        playNote(Note.A_SHARP_4, volume, robotControllerService);
        SystemClock.sleep(eighthNote);
        playNote(Note.D_4, volume, robotControllerService);
        SystemClock.sleep(dottedQuarterNote - 10);
        robotControllerService.stopPlayingTone();
        SystemClock.sleep(10); //a tiny interruption to make notes distinct
        playNote(Note.D_4, volume, robotControllerService);
        SystemClock.sleep(eighthNote);

        //bar 4
        playNote(Note.C_SHARP_4, volume, robotControllerService);
        SystemClock.sleep(dottedHalfNote);
        robotControllerService.stopPlayingTone();
        SystemClock.sleep(quarterNote); //quarter rest


    }

    private static void playNote(Note note, int volume, EV3ControllerService ev3ControllerService) {
        ev3ControllerService.startPlayingTone(note.getFrequency(), volume);

    }

}
