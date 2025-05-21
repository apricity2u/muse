package com.example.muse.domain.image;

import com.example.muse.domain.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    public Image uploadImage(MultipartFile imageFile) {

        if (imageFile == null || imageFile.isEmpty()) {

            return null;
        }
        Map<String, String> s3ImageInfo = s3Service.uploadFile(imageFile);
        String imageUrl = s3ImageInfo.get("imageUrl");
        String s3Key = s3ImageInfo.get("s3Key");
        String originalFileName = imageFile.getOriginalFilename();


        Image image = Image.builder()
                .imageUrl(imageUrl)
                .originalFileName(originalFileName)
                .s3Key(s3Key)
                .imageType(ImageType.REVIEW)
                .build();

        return imageRepository.save(image);
    }
}
