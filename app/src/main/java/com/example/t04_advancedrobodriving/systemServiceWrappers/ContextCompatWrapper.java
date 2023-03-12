package com.example.t04_advancedrobodriving.systemServiceWrappers;

import android.content.Context;

import androidx.core.content.ContextCompat;

public class ContextCompatWrapper {

    public int checkSelfPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission);
    }
}
