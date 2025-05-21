package com.stt.speechtotext.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TranscriptionRequest {
    @NotBlank(message = "Language code is required (e.g., 'en-US')")
    private String languageCode;

    @NotNull(message = "Sample rate in Hertz is required (e.g., 16000)")
    private Integer sampleRateHertz;

    @NotBlank(message = "Audio format is required (e.g., 'LINEAR16', 'FLAC', 'MP3', 'OGG_OPUS')")
    private String audioFormat;

    // For remote file transcription
    private String gcsUri;
}