package com.example.muse.domain.member;

import com.example.muse.domain.image.Image;
import com.example.muse.domain.image.ImageRepository;
import com.example.muse.domain.image.ImageService;
import com.example.muse.domain.image.ImageType;
import com.example.muse.domain.member.dto.GetProfileResponseDto;
import com.example.muse.domain.member.dto.MemberProfileDto;
import com.example.muse.domain.review.ReviewRepository;
import com.example.muse.global.common.exception.CustomNotFoundException;
import com.example.muse.global.common.exception.CustomUnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final ReviewRepository reviewRepository;

    public GetProfileResponseDto getProfile(UUID memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(CustomNotFoundException::new);

        String profileImageUrl = imageRepository.findProfileImageByMemberId(memberId)
                .map(Image::getImageUrl)
                .orElse(null);
        long reviewCount = reviewRepository.countByMemberId(memberId);

        return GetProfileResponseDto.from(member, profileImageUrl, reviewCount);
    }

    @Transactional
    public MemberProfileDto updateProfile(MultipartFile imageFile, UUID memberId, String nickname, UUID authMemberId) {

        if (!memberId.equals(authMemberId)) {
            throw new CustomUnauthorizedException();
        }

        Member member = authMemberId == null ? null : memberRepository.getReferenceById(authMemberId);

        Image image = null;
        if (imageFile != null && !imageFile.isEmpty()) {

            imageRepository.findProfileImageByMemberId(memberId)
                    .ifPresent(imageService::deleteImage);

            image = imageService.uploadImage(imageFile, ImageType.PROFILE, member);
        }

        member.update(nickname, image);
        return MemberProfileDto.from(member, image == null ? null : image.getImageUrl());
    }
}
