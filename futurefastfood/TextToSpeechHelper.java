package com.example.futurefastfood;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechHelper implements TextToSpeech.OnInitListener {

    private final TextToSpeech tts;
    private boolean initialized;

    public TextToSpeechHelper(Context context) {
        tts = new TextToSpeech(context, this);
        initialized = false;
    }

    public void speak(String text) {
        if (initialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            Log.e("TextToSpeechHelper", "Text-to-speech not initialized");
        }
    }

    public void shutdown() {
        tts.stop();
        tts.shutdown();
        initialized = false;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.getDefault());
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeechHelper", "Language not supported");
            } else {
                initialized = true;
            }
        } else {
            Log.e("TextToSpeechHelper", "Text-to-speech initialization failed");
        }
    }
}
