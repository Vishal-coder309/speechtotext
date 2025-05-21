Spring Boot Speech-to-Text Application

I've created a complete Spring Boot application for speech-to-text conversion using Google's Cloud Speech API. This application follows standard Spring Boot architecture patterns with clear separation of concerns:
Project Structure
├── pom.xml
├── src
    ├── main
        ├── java
        │   └── com
        │       └── example
        │           └── speechtotext
        │               ├── SpeechToTextApplication.java
        │               ├── config
        │               │   └── GoogleCloudConfig.java
        │               ├── controller
        │               │   ├── GlobalExceptionHandler.java
        │               │   └── SpeechToTextController.java
        │               ├── model
        │               │   ├── AudioFormat.java
        │               │   ├── TranscriptionRequest.java
        │               │   └── TranscriptionResponse.java
        │               └── service
        │                   └── SpeechToTextService.java
        └── resources
            ├── application.properties
            └── static
                └── index.html
Key Components

1. Controller Layer
The SpeechToTextController exposes two REST endpoints:

/api/speech/transcribe - For uploading and transcribing local audio files
/api/speech/transcribe/remote - For transcribing audio files stored in Google Cloud Storage

2. Service Layer
The SpeechToTextService handles the business logic for:

Converting uploaded audio files to text
Converting remote GCS audio files to text
Proper error handling and response formatting

3. Model Layer

AudioFormat - Enum that maps user-friendly format names to Google's AudioEncoding values
TranscriptionRequest - DTO for handling transcription requests
TranscriptionResponse - DTO for returning transcription results

4. Configuration

GoogleCloudConfig - Sets up the Google Cloud Speech client as a Spring Bean

5. Web Interface
A responsive Bootstrap-based UI is included that allows users to:

Upload local audio files for transcription
Transcribe remote files stored in Google Cloud Storage
View the transcription results in real-time

How to Use This Application

Prerequisites

Set up a Google Cloud project
Enable the Speech-to-Text API
Create a service account with appropriate permissions
Download the service account key JSON file

Configuration Setup
Set the GOOGLE_APPLICATION_CREDENTIALS environment variable to point to your service account key:
export GOOGLE_APPLICATION_CREDENTIALS="/path/to/your-service-account-key.json"

Running the Application

Clone the repository
Build the project: mvn clean install
Run the application: java -jar target/speech-to-text-0.0.1-SNAPSHOT.jar
Open a browser and navigate to: http://localhost:8080/speech-to-text/

Using the REST API

1. Transcribe a Local Audio File
curl -X POST \
  http://localhost:8080/speech-to-text/api/speech/transcribe \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@/path/to/audio.wav' \
  -F 'languageCode=en-US' \
  -F 'sampleRateHertz=16000' \
  -F 'audioFormat=LINEAR16'


2. Transcribe a Remote Audio File

curl -X POST \
  http://localhost:8080/speech-to-text/api/speech/transcribe/remote \
  -H 'Content-Type: application/json' \
  -d '{
    "gcsUri": "gs://your-bucket/audio.flac",
    "languageCode": "en-
