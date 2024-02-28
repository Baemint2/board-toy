package org.example.board.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
        String redirect = request.getParameter("redirect");

        // 로깅 추가
        log.info("Redirect parameter value: {}", redirect);

        if(redirect != null && !redirect.isEmpty()) {
            log.info("Redirecting to: {}", redirect);
            return redirect;
        } else {
            String defaultTargetUrl = super.determineTargetUrl(request, response);
            log.info("Default redirecting to: {}", defaultTargetUrl);
            return defaultTargetUrl;
        }
    }
}
