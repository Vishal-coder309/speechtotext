package com.stt.speechtotext.model;

import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AudioFormat {
    LINEAR16(AudioEncoding.LINEAR16, "LINEAR16"),
    FLAC(AudioEncoding.FLAC, "FLAC"),
    MP3(AudioEncoding.MP3, "MP3"),
    OGG_OPUS(AudioEncoding.OGG_OPUS, "OGG_OPUS");

    private final AudioEncoding encoding;
    private final String name;

    public static AudioFormat fromString(String format) {
        for (AudioFormat audioFormat : AudioFormat.values()) {
            if (audioFormat.name.equalsIgnoreCase(format)) {
                return audioFormat;
            }
        }
        throw new IllegalArgumentException("Unknown audio format: " + format);
    }
}