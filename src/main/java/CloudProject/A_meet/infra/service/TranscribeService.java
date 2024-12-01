package CloudProject.A_meet.infra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.transcribe.TranscribeClient;
import software.amazon.awssdk.services.transcribe.model.Media;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobRequest;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJob;
import software.amazon.awssdk.services.transcribe.model.TranscriptionJobStatus;
import software.amazon.awssdk.services.transcribe.model.StartTranscriptionJobResponse;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TranscribeService {

    private final TranscribeClient transcribeClient;

    @Value("${cloud.aws.s3.output-bucket}")
    private String bucketName;

    /**
     * Transcription 작업 시작
     *
     * @param s3Url S3 Pre-signed URL
     * @return Transcription Job ID
     */
    public String startTranscriptionJob(String s3Url, String botId) {
        StartTranscriptionJobRequest transcriptionJobRequest = StartTranscriptionJobRequest.builder()
            .transcriptionJobName(botId)
            .media(Media.builder().mediaFileUri(s3Url).build())
            .identifyLanguage(true)
            .outputBucketName(bucketName)
            .build();

        StartTranscriptionJobResponse response = transcribeClient.startTranscriptionJob(transcriptionJobRequest);
        return response.transcriptionJob().transcriptionJobName();
    }

    /**
     * Transcription 작업 상태 확인
     *
     * @param jobName Transcription Job 이름
     * @return Transcription 작업 상태
     */
    public TranscriptionJobStatus getTranscriptionJobStatus(String jobName) {
        TranscriptionJob job = transcribeClient.getTranscriptionJob(r -> r.transcriptionJobName(jobName))
            .transcriptionJob();
        return job.transcriptionJobStatus();
    }
}