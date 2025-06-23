package com.example.muse.domain.image;

import com.example.muse.domain.member.Member;
import com.example.muse.domain.s3.S3Service;
import com.example.muse.global.common.exception.CustomBadRequestException;
import com.example.muse.global.common.exception.CustomS3Exception;
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
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/jpg", "image/webp");
    private static final String IMAGE_FILE_NAME_PATTERN = ".*\\.(png|jpe?g|webp)$";
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public Image uploadImage(MultipartFile imageFile, ImageType imageType, Member member) {

        if (imageFile == null || imageFile.isEmpty()) {

            throw new CustomBadRequestException("이미지 파일이 존재하지 않습니다.");
        }

        if (!isCorrectImage(imageFile)) {

            throw new CustomBadRequestException("유효한 이미지 파일이 아닙니다.");
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
                    .member(member)
                    .build();

            return imageRepository.save(image);
        } catch (Exception e) {
            throw new CustomS3Exception();
        }
    }


    @Transactional
    public void deleteImage(Image image) {

        try {
            Member member = image.getMember();
            s3Service.deleteFile(image.getS3Key());
            imageRepository.delete(image);
            member.getImages().remove(image);
        } catch (Exception e) {
            throw new CustomS3Exception();
        }
    }

    private boolean isCorrectImage(MultipartFile imageFile) {

        String contentType = imageFile.getContentType();
        String fileName = imageFile.getOriginalFilename();

        if (contentType == null || fileName == null) {

            return false;
        }

        return ALLOWED_IMAGE_TYPES.contains(contentType)
                && fileName.toLowerCase().matches(IMAGE_FILE_NAME_PATTERN);
    }
}
