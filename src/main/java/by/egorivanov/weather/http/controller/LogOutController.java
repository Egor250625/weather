package by.egorivanov.weather.http.controller;

import by.egorivanov.weather.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LogOutController {

    private final SessionService sessionService;

    @PostMapping("/logout")
    public String logOut(@CookieValue(value = "SESSION_ID", required = false) String sessionId,
                         HttpServletRequest request,
                         HttpServletResponse response) {
        if (sessionId != null) {
            log.info("session {} delete process.",sessionId);
            sessionService.invalidateSession(sessionId, response);
        }
        String referer = request.getHeader("Referer");
        log.info("Session delete - Complete.");
        return "redirect:/home";
    }

}
