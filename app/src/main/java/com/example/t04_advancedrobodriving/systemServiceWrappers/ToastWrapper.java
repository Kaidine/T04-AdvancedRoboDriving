package com.example.t04_advancedrobodriving.systemServiceWrappers;

import android.content.Context;
import android.widget.Toast;

public class ToastWrapper {
    public Toast makeText(Context context, CharSequence text, int duration){
        return Toast.makeText(context, text, duration);
    }
}
