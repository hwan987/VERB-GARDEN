package com.example.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import java.util.UUID

class TtsManager(context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private val mainHandler = Handler(Looper.getMainLooper())

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _speakingWord = MutableStateFlow<String?>(null)
    val speakingWord: StateFlow<String?> = _speakingWord.asStateFlow()

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isInitialized = true
                    tts?.setSpeechRate(0.88f) // Slightly clearer and friendlier for elementary learners
                    tts?.setPitch(1.05f)
                }
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
            tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
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
