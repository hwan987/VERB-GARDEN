package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * 초등학생 눈높이에 맞춘 아기자기하고 귀여운 효과음(SFX) 합성 재생기입니다.
 * 외부 파일이나 네트워크 다운로드 없이 Android AudioTrack을 통해
 * 마림바, 오르골, 물방울 뾱, 짜잔 팡파르 소리를 맑고 지연 없이 재생합니다.
 */
class SoundEffectManager {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val sampleRate = 44100

    private val audioAttributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
        .build()

    private val audioFormat = AudioFormat.Builder()
        .setSampleRate(sampleRate)
        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
        .build()

    // 귀여운 사운드 버퍼 캐시 (한 번 생성 후 재사용)
    private val popSoundBuffer: ShortArray by lazy { generatePopSound() }
    private val correctChimeBuffer: ShortArray by lazy { generateCorrectChime() }
    private val tadaBloomBuffer: ShortArray by lazy { generateTadaBloomSound() }
    private val wrongBoingBuffer: ShortArray by lazy { generateWrongBoingSound() }
    private val tapClickBuffer: ShortArray by lazy { generateTapSound() }

    /**
     * 효과음 제거 요청에 따라 모든 효과음 출력을 무음 처리합니다.
     */
    fun playPop() {
        // Muted (효과음 제거)
    }

    fun playCorrect() {
        // Muted (효과음 제거)
    }

    fun playTadaBloom() {
        // Muted (효과음 제거)
    }

    fun playWrong() {
        // Muted (효과음 제거)
    }

    fun playTap() {
        // Muted (효과음 제거)
    }

    private fun playSoundBuffer(buffer: ShortArray) {
        scope.launch {
            try {
                val track = AudioTrack.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(buffer.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(buffer, 0, buffer.size)
                track.play()

                // 재생 완료 후 정리
                val durationMs = (buffer.size.toDouble() / sampleRate * 1000).toLong() + 100
                kotlinx.coroutines.delay(durationMs)
                track.stop()
                track.release()
            } catch (e: Exception) {
                // AudioTrack 재생 오류 시 무시 (앱 중단 방지)
            }
        }
    }

    /**
     * 귀여운 버블 팝 소리 (물방울 뾱!)
     * 350Hz에서 850Hz로 빠르게 솟아오르는 주파수 스윕
     */
    private fun generatePopSound(): ShortArray {
        val duration = 0.09 // 90ms
        val totalSamples = (sampleRate * duration).toInt()
        val buffer = ShortArray(totalSamples)

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / totalSamples
            // 주파수 상승: 380Hz -> 820Hz
            val freq = 380.0 + (820.0 - 380.0) * progress * progress
            // 부드러운 감쇠 곡선
            val envelope = exp(-progress * 6.5) * sin(progress * PI)
            val sample = sin(2.0 * PI * freq * t) * envelope
            buffer[i] = (sample * 24000).toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    /**
     * 맑은 오르골 띠로롱~ 정답 효과음 (C5 -> E5 -> G5)
     */
    private fun generateCorrectChime(): ShortArray {
        val noteDuration = 0.12 // 각 음당 120ms
        val frequencies = doubleArrayOf(523.25, 659.25, 783.99) // C5, E5, G5
        val totalSamples = (sampleRate * (noteDuration * 2 + 0.35)).toInt()
        val buffer = ShortArray(totalSamples)

        for (n in frequencies.indices) {
            val freq = frequencies[n]
            val startSample = (sampleRate * (n * noteDuration)).toInt()
            val noteLength = (sampleRate * 0.35).toInt() // 잔향

            for (i in 0 until noteLength) {
                val idx = startSample + i
                if (idx >= totalSamples) break
                val t = i.toDouble() / sampleRate
                val progress = i.toDouble() / noteLength

                // 배음 섞인 맑은 실로폰/오르골 톤
                val base = sin(2.0 * PI * freq * t)
                val overtone = 0.35 * sin(2.0 * PI * (freq * 2) * t)
                val envelope = exp(-progress * 7.0)

                val sample = (base + overtone) * envelope
                val existing = buffer[idx].toInt()
                val combined = existing + (sample * 16000).toInt()
                buffer[idx] = combined.coerceIn(-32767, 32767).toShort()
            }
        }
        return buffer
    }

    /**
     * "짜잔!" 만개 팡파르 (C5 -> E5 -> G5 -> C6 반짝이는 마법 효과음)
     */
    private fun generateTadaBloomSound(): ShortArray {
        val noteDuration = 0.10
        val frequencies = doubleArrayOf(523.25, 659.25, 783.99, 1046.50) // C5, E5, G5, C6
        val totalSamples = (sampleRate * (noteDuration * 3 + 0.50)).toInt()
        val buffer = ShortArray(totalSamples)

        for (n in frequencies.indices) {
            val freq = frequencies[n]
            val startSample = (sampleRate * (n * noteDuration)).toInt()
            val noteLength = (sampleRate * 0.45).toInt()

            for (i in 0 until noteLength) {
                val idx = startSample + i
                if (idx >= totalSamples) break
                val t = i.toDouble() / sampleRate
                val progress = i.toDouble() / noteLength

                // 반짝이는 벨 소리 + 비브라토
                val vibrato = 1.0 + 0.02 * sin(2.0 * PI * 8.0 * t)
                val base = sin(2.0 * PI * freq * vibrato * t)
                val sparkle = 0.25 * sin(2.0 * PI * (freq * 3) * t)
                val envelope = exp(-progress * 6.0)

                val sample = (base + sparkle) * envelope
                val existing = buffer[idx].toInt()
                val combined = existing + (sample * 15000).toInt()
                buffer[idx] = combined.coerceIn(-32767, 32767).toShort()
            }
        }
        return buffer
    }

    /**
     * 부드럽고 귀여운 오답 음 ("오잉~?")
     */
    private fun generateWrongBoingSound(): ShortArray {
        val duration = 0.25 // 250ms
        val totalSamples = (sampleRate * duration).toInt()
        val buffer = ShortArray(totalSamples)

        for (i in 0 until totalSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / totalSamples
            // 귀여운 하강 음 (420Hz -> 280Hz)
            val freq = 420.0 - (140.0 * progress)
            val envelope = exp(-progress * 4.5) * sin(progress * PI)
            val sample = sin(2.0 * PI * freq * t) * envelope
            buffer[i] = (sample * 19000).toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    /**
     * 귀여운 터치 탭 소리
     */
    private fun generateTapSound(): ShortArray {
        val duration = 0.04
        val totalSamples = (sampleRate * duration).toInt()
        val buffer = ShortArray(totalSamples)

        for (i in 0 until totalSamples) {
            val progress = i.toDouble() / totalSamples
            val t = i.toDouble() / sampleRate
            val sample = sin(2.0 * PI * 680.0 * t) * exp(-progress * 14.0)
            buffer[i] = (sample * 16000).toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }
}
