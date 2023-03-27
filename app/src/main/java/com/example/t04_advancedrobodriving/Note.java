package com.example.t04_advancedrobodriving;

public enum Note {
    C_4(262),
    C_SHARP_4(277),
    D_4(294),
    D_SHARP_4(311),
    E_4(330),
    F_4(349),
    F_SHARP_4(370),
    G_4(392),
    G_SHARP_4(415),
    A_4(440),
    A_SHARP_4(466),
    B_4(494),
    C_5(523),
    C_SHARP_5(554),
    D_5(587),
    D_SHARP_5(622),
    E_5(659),
    F_5(698),
    F_SHARP_5(740),
    G_5(784),
    G_SHARP_5(831),
    A_5(880),
    A_SHARP_5(932),
    B_5(988);

    private final int frequency;

    Note(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }
}
