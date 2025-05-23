package com.example.muse.domain.member;

import com.example.muse.domain.image.Image;
import com.example.muse.domain.image.ImageRepository;
import com.example.muse.domain.member.dto.GetProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;

    public GetProfileResponseDto getProfile(UUID memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        Image profileImage = imageRepository.findLastProfileImageByMemberId(memberId.toString())
                .orElse(null);

        return GetProfileResponseDto.from(member, profileImage);
    }
}
