package by.egorivanov.weather.http.interceptor;

import by.egorivanov.weather.dto.response.SessionDto;
import by.egorivanov.weather.exception.SessionException;
import by.egorivanov.weather.service.SessionService;
import by.egorivanov.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class SessionInterceptor implements HandlerInterceptor {
    private final SessionService sessionService;
    private final UserService userService;

    private static final long SESSION_TIMEOUT_MINUTES = 30;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return true;
        }

        for (Cookie cookie : cookies) {
            if ("SESSION_ID".equals(cookie.getName())) {
                log.info("SESSION ID is correct.");
                try {
                    UUID sessionId = UUID.fromString(cookie.getValue());
                    SessionDto session = sessionService.findById(sessionId);

                    if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
                        sessionService.delete(sessionId);
                        clearSessionCookieAndRedirect(response, cookie);
                        return false;
                    }

                    session.setExpiresAt(LocalDateTime.now().plusMinutes(30));
                    sessionService.update(session);

                    request.setAttribute("userId", session.getUserId());
                } catch (IllegalArgumentException | SessionException e) {
                    clearSessionCookieAndRedirect(response, cookie);
                    return false;
                }
                break;
            }
        }

        return true;
    }


    private void clearSessionCookieAndRedirect(HttpServletResponse response, Cookie cookie) throws IOException {
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.sendRedirect("/sign-in");
    }

}
