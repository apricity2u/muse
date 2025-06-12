package com.example.muse.domain.image.dto;

import com.example.muse.domain.image.Image;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private String imageUrl;
    private String originalFileName;

    public static ImageDto from(Image image) {

        if (image == null) {
            return new ImageDto(null, null);
        }

        return new ImageDto(
                image.getImageUrl(),
                image.getOriginalFileName()
        );
    }
}
