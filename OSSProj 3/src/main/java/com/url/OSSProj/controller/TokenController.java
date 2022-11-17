package com.url.OSSProj.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.url.OSSProj.domain.constants.AuthConstants;
import com.url.OSSProj.domain.dto.SuccessLoginMemberDto;
import com.url.OSSProj.domain.dto.Token;
import com.url.OSSProj.domain.entity.Member;
import com.url.OSSProj.domain.enums.UserRole;
import com.url.OSSProj.repository.MemberRepository;
import com.url.OSSProj.utils.CookieUtils;
import com.url.OSSProj.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2

@RequiredArgsConstructor
@Controller
public class TokenController {

    private final TokenUtils tokenUtils;
    private final CookieUtils cookieUtils;
    private final ObjectMapper objectMapper;
    private final MemberRepository memberRepository;

    @GetMapping("/token/expired")
    public String auth(){
        throw new RuntimeException();
    }

    @ResponseBody
    @PostMapping("/token/refresh")
    public SuccessLoginMemberDto refreshAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie refreshCookie = cookieUtils.getCookie(request, AuthConstants.REFRESH_HEADER);
        String refreshToken = refreshCookie.getValue();

        if(refreshToken != null && tokenUtils.isValidToken(refreshToken)) {
            String email = tokenUtils.getUid(refreshToken);
            Token newToken = tokenUtils.generateToken(email, UserRole.USER.getKey());
            Cookie refreshTokenCookie = cookieUtils.createCookie(AuthConstants.REFRESH_HEADER, newToken.getRefreshToken());

            response.addCookie(refreshTokenCookie);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("no such data"));
            SuccessLoginMemberDto successLoginMemberDto = getSuccessLoginMemberDto(newToken, member);

            log.info("Re Generate memberName : " + successLoginMemberDto.getName());
            log.info("Re Generate accessToken : " + successLoginMemberDto.getAccessToken());

            return successLoginMemberDto;
        }
        throw new RuntimeException();
    }

    private SuccessLoginMemberDto getSuccessLoginMemberDto(Token newToken, Member member) {
        final SuccessLoginMemberDto successLoginMemberDto = new SuccessLoginMemberDto();
        successLoginMemberDto.setName(member.getName());
        successLoginMemberDto.setAccessToken(newToken.getAccessToken());

        return successLoginMemberDto;
    }
}
