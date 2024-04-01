package com.rabbit.rabbit_mq.global.security.handle;

import com.rabbit.rabbit_mq.domain.member.service.AuthTokenService;
import com.rabbit.rabbit_mq.global.https.ReqData;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final ReqData rq;
    private final AuthTokenService authTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        String accessToken = authTokenService.genAccessToken(rq.getMember());
        String refreshToken = rq.getMember().getRefreshToken();

        String redirectUrlAfterSocialLogin = rq.getCookieValue("redirectUrlAfterSocialLogin", "");

        if (rq.isFrontUrl(redirectUrlAfterSocialLogin)) {
            rq.destroySecurityContextSession(); // 세션방식이 아닌 쿠키+JWT 방식으로 인증하기 때문에, 세션에 저장된 인증정보 삭제
            rq.setCrossDomainCookie("accessToken", accessToken);
            rq.setCrossDomainCookie("refreshToken", refreshToken);
            rq.removeCookie("redirectUrlAfterSocialLogin");
            response.sendRedirect(redirectUrlAfterSocialLogin);
            return;
        } else {
            rq.destroySecurityContextSession(); // 세션방식이 아닌 쿠키+JWT 방식으로 인증하기 때문에, 세션에 저장된 인증정보 삭제
            rq.setCrossDomainCookie("accessToken", accessToken);
            rq.setCrossDomainCookie("refreshToken", refreshToken);
            response.sendRedirect("/chat/RoomList");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}