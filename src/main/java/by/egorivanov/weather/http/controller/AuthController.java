package by.egorivanov.weather.http.controller;

import by.egorivanov.weather.dto.request.UserCreateEditDto;
import by.egorivanov.weather.service.SessionService;
import by.egorivanov.weather.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final UserService userService;
    private final SessionService sessionService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @GetMapping("/sign-in")
    public String singInPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserCreateEditDto());
        }
        return "user/sign-in";
    }

    @GetMapping("/sign-in-with-errors")
    public String singInErrorPage(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserCreateEditDto());
        }
        return "user/sign-in-with-errors";
    }

    @PostMapping("/sign-in")
    public String logIn(@Valid @ModelAttribute("user") UserCreateEditDto user,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) {

        log.info("Sign-in attempt for user: {}", user.getUsername());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/sign-in-with-errors";
        }

        try {
            var foundUser = userService.findByUsername(user.getUsername());

            if (foundUser == null) {
                redirectAttributes.addFlashAttribute("loginError", "Wrong username or password");
                return "redirect:/sign-in-with-errors";
            }


            if (!passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                redirectAttributes.addFlashAttribute("loginError", "Wrong username or password");
                return "redirect:/sign-in-with-errors";
            }
            log.info("Password is correct for user: {}", user.getUsername());
            try {
                var sessionDto = sessionService.create(foundUser.getId());
                log.info("Session created with UUID : {}", sessionDto.getSessionId().toString()); //Method threw 'org.hibernate.LazyInitializationException' exception. Cannot evaluate by.egorivanov.weather.model.entity.Users.toString()
                Cookie cookie = new Cookie("SESSION_ID", sessionDto.getSessionId().toString());
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(30 * 60);
                response.addCookie(cookie);
            } catch (Exception e) {
                log.error("Ошибка при создании сессии", e);
                return "redirect:/sign-in-with-errors";
            }
            return "redirect:/home";
        } catch (Exception e) {
            log.error("Login failed", e);
            redirectAttributes.addFlashAttribute("loginError", "Wrong username or password");
            return "redirect:/sign-in-with-errors";
        }
    }
}
