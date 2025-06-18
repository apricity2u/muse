package com.example.muse.domain.image;

import com.example.muse.domain.member.Member;
import com.example.muse.domain.review.Review;
import com.example.muse.domain.s3.S3Service;
import com.example.muse.global.common.exception.CustomBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/jpg", "image/webp");
    private final S3Service s3Service;
    private final ImageRepository imageRepository;

    @Transactional
    public Image uploadImage(MultipartFile imageFile, ImageType imageType, Member member) {

        if (imageFile == null || imageFile.isEmpty()) {

            throw new CustomBadRequestException("이미지 파일이 존재하지 않습니다.");
        }

        if (!isCorrectImage(imageFile)) {

            throw new CustomBadRequestException("유효한 이미지 파일이 아닌입니다.");
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
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage());
        }
    }


    @Transactional
    public void deleteImage(Image image) {

        try {
            s3Service.deleteFile(image.getS3Key());

            if (image.getImageType() == ImageType.PROFILE) {
                image.getMember().getImages().remove(image);
            } else {
                Optional<Review> opt = image.getReviews().stream()
                        .filter(review -> review.getImage().equals(image))
                        .findAny();

                opt.ifPresent(review -> {
                    review.setImage(null);
                    image.getReviews().remove(review);
                });

                imageRepository.delete(image);
            }
        } catch (Exception e) {
            throw new RuntimeException("이미지 삭제 실패: " + e.getMessage());
        }
    }

    private boolean isCorrectImage(MultipartFile imageFile) {

        String contentType = imageFile.getContentType();
        String fileName = imageFile.getOriginalFilename();

        if (contentType == null || fileName == null) {

            return false;
        }

        return ALLOWED_IMAGE_TYPES.contains(contentType)
                && fileName.toLowerCase().matches(".*\\.(png|jpe?g|webp)$");
    }

    public Image getImageById(long l) {

        return imageRepository.findById(l).orElse(null);
    }
}
