package CloudProject.A_meet.infra.service;

import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor

public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.input-bucket}")
    private String inputBucket;

    @Value("${cloud.aws.s3.output-bucket")
    private String outputBucket;

    public URL generatePresignedUrl(String key, Duration duration) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + duration.toMillis());

            GeneratePresignedUrlRequest presignedUrlRequest = new GeneratePresignedUrlRequest(inputBucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

            return amazonS3.generatePresignedUrl(presignedUrlRequest);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.S3_PreSignedURL_GENERATION_FAILED);
        }
    }

    public String getTranscriptionResult(String botId) {
        String objectKey = botId + ".json";
        System.out.println(outputBucket + objectKey);
        try {
            S3Object s3Object = amazonS3.getObject("transcribe-output-cp", objectKey);
            InputStream inputStream = s3Object.getObjectContent();
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (AmazonS3Exception e) {
            // 객체가 존재하지 않는 경우
            if (e.getStatusCode() == 404) {
                System.err.println("S3 Object not found: " + objectKey);
                throw new CustomException(ErrorCode.S3_OBJECT_NOT_FOUND);
            }
            throw new CustomException(ErrorCode.S3_FILE_READ_ERROR);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.S3_FILE_READ_ERROR);
        }
    }

}
