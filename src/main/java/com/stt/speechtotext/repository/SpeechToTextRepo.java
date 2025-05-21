package com.stt.speechtotext.repository;

import com.stt.speechtotext.model.TranscriptionResponse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpeechToTextRepo extends MongoRepository<TranscriptionResponse,String> {
}
