package CloudProject.A_meet.infra.service;

import CloudProject.A_meet.global.common.error.exception.CustomException;
import CloudProject.A_meet.global.common.error.exception.ErrorCode;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;
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

    public String getTranscriptionResult(Long botId) {
        try {
            S3Object s3Object = amazonS3.getObject(outputBucket, botId.toString());
            InputStream inputStream = s3Object.getObjectContent();
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.S3_FILE_READ_ERROR);
        }
    }
}
