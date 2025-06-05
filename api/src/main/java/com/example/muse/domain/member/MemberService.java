package com.example.muse.domain.member;

import com.example.muse.domain.image.Image;
import com.example.muse.domain.image.ImageRepository;
import com.example.muse.domain.image.ImageService;
import com.example.muse.domain.image.ImageType;
import com.example.muse.domain.member.dto.GetProfileResponseDto;
import com.example.muse.domain.member.dto.MemberProfileDto;
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

    public GetProfileResponseDto getProfile(UUID memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        Image profileImage = imageRepository.findLastProfileImageByMemberId(memberId.toString())
                .orElse(null);

        return GetProfileResponseDto.from(member, profileImage);
    }

    @Transactional
    public MemberProfileDto updateProfile(MultipartFile imageFile, UUID memberId, String nickname, Member authMember) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        if (!member.getId().equals(authMember.getId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        Image image = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            image = imageService.uploadImage(imageFile, ImageType.PROFILE, member);
        }
        
        member.update(nickname, image);
        return MemberProfileDto.from(member);
    }
}
