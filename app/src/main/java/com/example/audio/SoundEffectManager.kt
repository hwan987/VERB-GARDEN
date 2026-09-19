package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.ToneGenerator
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Android 시스템 내장 사운드 생성기를 활용한 확실한 효과음 관리자입니다.
 *
 * ToneGenerator는 Android 시스템 코어(MediaServer/AudioFlinger) 레벨에서 직접 신호를 생성하므로,
 * 스트리밍 에뮬레이터, 블루투스 이어폰, 저가형 기기 등 어떤 디바이스 환경에서도
 * 지연이나 버퍼 누락 없이 즉시 또렷하게 재생됩니다.
 */
class SoundEffectManager(context: Context? = null) {

    private val scope = CoroutineScope(Dispatchers.Default)

    // USAGE_MEDIA / STREAM_MUSIC으로 미디어 볼륨과 연동
    private val toneGenerator: ToneGenerator? by lazy {
        try {
            ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        } catch (e: Throwable) {
            Log.e("SoundEffectManager", "ToneGenerator init failed", e)
            null
        }
    }

    /**
     * 경쾌한 팝/버튼 탭 사운드
     */
    fun playPop() {
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP, 80)
            } catch (e: Throwable) {
                Log.e("SoundEffectManager", "playPop failed", e)
            }
        }
    }

    /**
     * 정답 맞혔을 때 맑고 기분 좋은 2음 딩동 차임벨
     */
    fun playCorrect() {
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 180)
            } catch (e: Throwable) {
                Log.e("SoundEffectManager", "playCorrect failed", e)
            }
        }
    }

    /**
     * 꽃이 활짝 피었을 때 / 학습 완료 시 기분 좋은 축하 팡파레 (도-미-솔 3화음)
     */
    fun playTadaBloom() {
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_PROMPT, 100)
                delay(120)
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 250)
            } catch (e: Throwable) {
                Log.e("SoundEffectManager", "playTadaBloom failed", e)
            }
        }
    }

    /**
     * 오답 시 부드러운 저음 비프음
     */
    fun playWrong() {
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_NACK, 220)
            } catch (e: Throwable) {
                Log.e("SoundEffectManager", "playWrong failed", e)
            }
        }
    }

    /**
     * 가벼운 선택/탭 클릭 사운드
     */
    fun playTap() {
        scope.launch {
            try {
                toneGenerator?.startTone(ToneGenerator.TONE_PROP_BEEP2, 50)
            } catch (e: Throwable) {
                Log.e("SoundEffectManager", "playTap failed", e)
            }
        }
    }
}
