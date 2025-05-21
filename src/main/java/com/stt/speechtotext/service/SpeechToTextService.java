package com.stt.speechtotext.service;


import com.stt.speechtotext.model.AudioFormat;
import com.stt.speechtotext.model.TranscriptionRequest;
import com.stt.speechtotext.model.TranscriptionResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;
import com.stt.speechtotext.repository.SpeechToTextRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpeechToTextService {

    private final SpeechClient speechClient;
    private final SpeechToTextRepo speechToTextRepo;
    private final Logger LOGGER= LoggerFactory.getLogger(SpeechToTextService.class);


    public TranscriptionResponse transcribeAudioFile(MultipartFile file, TranscriptionRequest request) {
        long startTime = Instant.now().toEpochMilli();

        try {
            // Convert the request format to Google AudioEncoding
            AudioFormat format = AudioFormat.fromString(request.getAudioFormat());

            // Configure the request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(format.getEncoding())
                    .setSampleRateHertz(request.getSampleRateHertz())
                    .setLanguageCode(request.getLanguageCode())
                    .setEnableAutomaticPunctuation(true)
                    .build();

            // Read the audio file content
            ByteString audioBytes = ByteString.copyFrom(file.getBytes());
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(audioBytes)
                    .build();

            // Perform the speech recognition
            RecognizeResponse response = speechClient.recognize(config, audio);

            // Process the results
            StringBuilder transcription = new StringBuilder();
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcription.append(alternative.getTranscript());
            }

            long processingTime = Instant.now().toEpochMilli() - startTime;

            // Build TranscriptionResponse object with the transcription stored in both 'transcription' and 'result'
            TranscriptionResponse transcriptionResponse = TranscriptionResponse.builder()
                    .transcription(transcription.toString())
                    .processingTimeMs(processingTime)
                    .status("SUCCESS")
                    .build();

            // Save to MongoDB
            speechToTextRepo.save(transcriptionResponse);

            return transcriptionResponse;

        } catch (Exception e) {
            LOGGER.error("Error during speech-to-text conversion", e);

            long processingTime = Instant.now().toEpochMilli() - startTime;

            return TranscriptionResponse.builder()
                    .transcription("")
                    .processingTimeMs(processingTime)
                    .status("ERROR")
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    public TranscriptionResponse transcribeRemoteAudio(TranscriptionRequest request) {
        long startTime = Instant.now().toEpochMilli();

        try {
            if (request.getGcsUri() == null || request.getGcsUri().isEmpty()) {
                throw new IllegalArgumentException("GCS URI is required for remote audio transcription");
            }

            // Convert the request format to Google AudioEncoding
            AudioFormat format = AudioFormat.fromString(request.getAudioFormat());

            // Configure the request
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(format.getEncoding())
                    .setSampleRateHertz(request.getSampleRateHertz())
                    .setLanguageCode(request.getLanguageCode())
                    .setEnableAutomaticPunctuation(true)
                    .build();

            // Create audio with GCS URI
            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setUri(request.getGcsUri())
                    .build();

            // Perform the speech recognition
            RecognizeResponse response = speechClient.recognize(config, audio);

            // Process the results
            StringBuilder transcription = new StringBuilder();
            List<SpeechRecognitionResult> results = response.getResultsList();

            for (SpeechRecognitionResult result : results) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                transcription.append(alternative.getTranscript());
            }

            long processingTime = Instant.now().toEpochMilli() - startTime;

            TranscriptionResponse transcriptionResponse = TranscriptionResponse.builder()
                    .transcription(transcription.toString())
                    .processingTimeMs(processingTime)
                    .status("SUCCESS")
                    .build();
            speechToTextRepo.save(transcriptionResponse);
            return transcriptionResponse;


        } catch (Exception e) {
            LOGGER.error("Error during remote speech-to-text conversion", e);

            long processingTime = Instant.now().toEpochMilli() - startTime;

            return TranscriptionResponse.builder()
                    .transcription("")
                    .processingTimeMs(processingTime)
                    .status("ERROR")
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
}