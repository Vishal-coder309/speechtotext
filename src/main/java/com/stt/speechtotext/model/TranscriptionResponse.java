package com.stt.speechtotext.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranscriptionResponse {
    private String transcription;
    private long processingTimeMs;
    private String status;
    private String errorMessage;
}