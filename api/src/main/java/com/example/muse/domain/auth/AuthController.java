package com.example.muse.domain.auth;

import com.example.muse.domain.auth.dto.LoginResponseDto;
import com.example.muse.domain.member.MemberRepository;
import com.example.muse.global.common.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler; // DEMO
    private final MemberRepository memberRepository;


    @GetMapping("/success")
    public ResponseEntity<ApiResponse<LoginResponseDto>> loginSuccess(@AuthenticationPrincipal UUID memberId) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "로그인에 성공했습니다.", "SUCCESS",
                        authService.getLoginWithData(memberId)
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@CookieValue(name = TokenResponseWriter.REFRESH_COOKIE_NAME) String refreshToken, HttpServletResponse response) {

        authService.logout(refreshToken, response);
        return ResponseEntity.ok(
                ApiResponse.ok(
                        "로그아웃에 성공했습니다.", "SUCCESS", null
                )
        );
    }


    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<LoginResponseDto>> reissue(
            @CookieValue(name = TokenResponseWriter.REFRESH_COOKIE_NAME) String refreshToken,
            HttpServletResponse response) {

        return ResponseEntity.ok(
                ApiResponse.ok(
                        "토큰을 재발급했습니다.", "SUCCESS", authService.reissue(refreshToken, response)
                )
        );
    }

    @SneakyThrows
    @GetMapping("/demoLogin")
    public void demoLogin(HttpServletRequest request,
                          HttpServletResponse response) {

        String providerKey = "demo-google-key-123";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", providerKey);
        attributes.put("nickname", "demo");

        OAuth2User oAuth2User = new DefaultOAuth2User(List.of(), attributes, "sub");
        OAuth2AuthenticationToken demoToken = new OAuth2AuthenticationToken(oAuth2User, oAuth2User.getAuthorities(), "GOOGLE");
        oAuth2SuccessHandler.onAuthenticationSuccess(request, response, demoToken);
    }
}
