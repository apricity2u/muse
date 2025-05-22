package com.example.muse.domain.image;

import com.example.muse.domain.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/jpg");
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public Image uploadImage(MultipartFile imageFile, ImageType imageType) {

        if (imageFile == null || imageFile.isEmpty()) {

            throw new IllegalArgumentException("이미지 파일을 업로드해주세요");
        }

        if (!isCorrectImage(imageFile)) {

            throw new IllegalArgumentException("이미지 파일 형식을 확인해주세요");
        }

        try {
            Map<String, String> s3ImageInfo = s3Service.uploadFile(imageFile);
            String imageUrl = s3ImageInfo.get("imageUrl");
            String s3Key = s3ImageInfo.get("s3Key");
            String originalFileName = imageFile.getOriginalFilename();


            Image image = Image.builder()
                    .imageUrl(imageUrl)
                    .originalFileName(originalFileName)
                    .s3Key(s3Key)
                    .imageType(imageType)
                    .build();

            return imageRepository.save(image);
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
        }
    }

    private boolean isCorrectImage(MultipartFile imageFile) {

        String contentType = imageFile.getContentType();
        String fileName = imageFile.getOriginalFilename();

        if (contentType == null || fileName == null) {

            return false;
        }

        return ALLOWED_IMAGE_TYPES.contains(contentType)
                && fileName.toLowerCase().matches(".*\\.(png|jpe?g)$");
    }
}
