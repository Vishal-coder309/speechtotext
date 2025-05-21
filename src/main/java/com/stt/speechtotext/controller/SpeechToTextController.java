package com.stt.speechtotext.controller;


import com.stt.speechtotext.model.TranscriptionRequest;
import com.stt.speechtotext.model.TranscriptionResponse;
import com.stt.speechtotext.service.SpeechToTextService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/speech")
@RequiredArgsConstructor
@Slf4j
public class SpeechToTextController {

    private final SpeechToTextService speechToTextService;

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TranscriptionResponse> transcribeAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("languageCode") String languageCode,
            @RequestParam("sampleRateHertz") Integer sampleRateHertz,
            @RequestParam("audioFormat") String audioFormat) {

        log.info("Received transcription request for file: {}", file.getOriginalFilename());

        TranscriptionRequest request = new TranscriptionRequest();
        request.setLanguageCode(languageCode);
        request.setSampleRateHertz(sampleRateHertz);
        request.setAudioFormat(audioFormat);

        TranscriptionResponse response = speechToTextService.transcribeAudioFile(file, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transcribe/remote")
    public ResponseEntity<TranscriptionResponse> transcribeRemoteAudio(
            @Valid @RequestBody TranscriptionRequest request) {

        log.info("Received remote transcription request for URI: {}", request.getGcsUri());

        TranscriptionResponse response = speechToTextService.transcribeRemoteAudio(request);
        return ResponseEntity.ok(response);
    }
}