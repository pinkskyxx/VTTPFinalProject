package sg.com.Shange.repositories;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ImageRepository {

    private final AmazonS3 s3;
    @Value("${s3.bucket.name}")
    private String bucketName;

    // private final String BUCKET_NAME = "csf-assessment";

    public void putObject(MultipartFile file, String imageName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        PutObjectRequest req = new PutObjectRequest(bucketName, imageName, file.getInputStream(), objectMetadata);
        req.withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(req);
    }
}
