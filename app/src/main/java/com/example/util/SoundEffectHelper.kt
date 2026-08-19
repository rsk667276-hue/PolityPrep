package com.example.util

import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object SoundEffectHelper {
    private const val TAG = "SoundEffectHelper"
    private var toneGenerator: ToneGenerator? = null

    init {
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 85)
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing ToneGenerator", e)
        }
    }

    fun playCorrectSound(enabled: Boolean = true) {
        if (!enabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 180)
        } catch (e: Exception) {
            Log.e(TAG, "Error playing correct tone", e)
        }
    }

    fun playWrongSound(enabled: Boolean = true) {
        if (!enabled) return
        try {
            toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 220)
        } catch (e: Exception) {
            Log.e(TAG, "Error playing wrong tone", e)
        }
    }

    fun playComboSound(comboCount: Int, enabled: Boolean = true) {
        if (!enabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
                delay(100)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 160)
            } catch (e: Exception) {
                Log.e(TAG, "Error playing combo sound", e)
            }
        }
    }

    fun playVictoryFanfare(enabled: Boolean = true) {
        if (!enabled) return
        CoroutineScope(Dispatchers.Default).launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                delay(160)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 180)
                delay(180)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 350)
            } catch (e: Exception) {
                Log.e(TAG, "Error playing victory fanfare", e)
            }
        }
    }
}
