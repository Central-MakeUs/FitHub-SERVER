package fithub.app.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import fithub.app.config.AmazonConfig;
import fithub.app.domain.Uuid;
import fithub.app.repository.UuidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AmazonS3Manager{

    private final AmazonS3 amazonS3;

    private final AmazonConfig amazonConfig;

    private final UuidRepository uuidRepository;


    public String uploadFile(String KeyName, MultipartFile file) throws IOException {
        System.out.println(KeyName);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), KeyName,file.getInputStream(), metadata));

        return amazonS3.getUrl(amazonConfig.getBucket(), KeyName).toString();
    }

    public S3Object downloadFile(String keyName) {
        return amazonS3.getObject(new GetObjectRequest(amazonConfig.getBucket(), keyName));
    }

    public void deleteFile(String keyName) {
        System.out.println("KEY NAME : " + keyName);
        amazonS3.deleteObject(amazonConfig.getBucket(), keyName);
    }

    public String generateArticleKeyName(Uuid uuid, String originalFilename) {
        return amazonConfig.getFithubArticle() + '/' + uuid.getUuid() + '_' + originalFilename;
    }

    public String generateRecordKeyName(Uuid uuid, String originalFilename) {
        return amazonConfig.getFithubRecord() + '/' + uuid.getUuid() + '_' + originalFilename;
    }

    public String generateProfileName(Uuid uuid, String originalFilename){
        return amazonConfig.getFithubProfile() + '/' + uuid.getUuid() +'_' + originalFilename;
    }

    public String generateFacilitiesName(Uuid uuid, String orginalFilename){
        return amazonConfig.getFacilities() + '/' + uuid.getUuid() + '_' + orginalFilename;
    }

    // 중복된 UUID가 있다면 중복이 없을때까지 재귀적으로 동작
    public Uuid createUUID() {
        Uuid savedUuid = null;
        String candidate = UUID.randomUUID().toString();
        if (uuidRepository.existsByUuid(candidate)) {
            savedUuid = createUUID();
        }
        savedUuid = uuidRepository.save(Uuid.builder().uuid(candidate).build());
        return savedUuid;
    }

}
