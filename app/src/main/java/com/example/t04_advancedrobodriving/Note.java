package com.example.t04_advancedrobodriving;

public enum Note {
    C(262),
    C_SHARP(277),
    D(294),
    D_SHARP(311),
    E(330),
    F(349),
    F_SHARP(370),
    G(392),
    G_SHARP(415),
    A(440),
    A_SHARP(466),
    B(494);

    private final int frequency;

    Note(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return frequency;
    }
}
