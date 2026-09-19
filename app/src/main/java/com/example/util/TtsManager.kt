package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.audio.SoundEffectManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.UUID

class TtsManager(
    private val context: Context,
    private val soundEffectManager: SoundEffectManager? = null
) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private var pendingWordToSpeak: String? = null

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _speakingWord = MutableStateFlow<String?>(null)
    val speakingWord: StateFlow<String?> = _speakingWord.asStateFlow()

    init {
        initTts()
    }

    private fun initTts() {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Ensure audio is directed to the media/music stream so it's always audible
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build()
                tts?.setAudioAttributes(audioAttributes)

                // Try US English first, with UK/English or default locale fallback
                var result = tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    result = tts?.setLanguage(Locale.ENGLISH)
                }
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    result = tts?.setLanguage(Locale.getDefault())
                }

                isInitialized = true
                tts?.setSpeechRate(0.88f) // Slightly clearer and friendlier for elementary learners
                tts?.setPitch(1.05f)

                Log.d("TtsManager", "TTS successfully initialized with result code: $result")

                // If a word was requested to be spoken before TTS finished initializing, speak it now
                pendingWordToSpeak?.let { pendingWord ->
                    pendingWordToSpeak = null
                    speak(pendingWord)
                }
            } else {
                Log.w("TtsManager", "TTS initialization returned status: $status")
            }
        }

        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                mainHandler.post {
                    _isSpeaking.value = true
                }
            }

            override fun onDone(utteranceId: String?) {
                mainHandler.post {
                    _isSpeaking.value = false
                    _speakingWord.value = null
                    currentOnDoneCallback?.invoke()
                    currentOnDoneCallback = null
                }
            }

            override fun onError(utteranceId: String?) {
                mainHandler.post {
                    _isSpeaking.value = false
                    _speakingWord.value = null
                    // Fallback to auditory cue so user always gets sound feedback even if TTS engine lacks voices
                    soundEffectManager?.playTap()
                    currentOnDoneCallback?.invoke()
                    currentOnDoneCallback = null
                }
            }
        })
    }

    private var currentOnDoneCallback: (() -> Unit)? = null

    fun speak(word: String, onStart: () -> Unit = {}, onDone: () -> Unit = {}) {
        _speakingWord.value = word
        _isSpeaking.value = true
        onStart()

        currentOnDoneCallback = onDone

        val utteranceId = UUID.randomUUID().toString()

        if (isInitialized && tts != null) {
            val params = Bundle().apply {
                putInt(TextToSpeech.Engine.KEY_PARAM_STREAM, AudioManager.STREAM_MUSIC)
                putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f)
            }
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
        } else {
            // Store as pending to speak as soon as initialization succeeds
            pendingWordToSpeak = word
            // Also provide instant tap tone so user hears audio response immediately
            soundEffectManager?.playTap()
        }

        // Safety fallback timer for emulators or devices with delayed/silent TTS
        mainHandler.postDelayed({
            if (_isSpeaking.value && _speakingWord.value == word) {
                _isSpeaking.value = false
                _speakingWord.value = null
                currentOnDoneCallback?.invoke()
                currentOnDoneCallback = null
            }
        }, 1800)
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _speakingWord.value = null
        currentOnDoneCallback = null
    }

    fun destroy() {
        stop()
        tts?.shutdown()
        tts = null
    }
}

