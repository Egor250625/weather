package by.egorivanov.weather.http.controller;

import by.egorivanov.weather.dto.request.UserCreateEditDto;
import by.egorivanov.weather.exception.SignUpException;
import by.egorivanov.weather.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;


    @GetMapping("sign-up")
    public String registration(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserCreateEditDto());

        }
        return "user/sign-up";
    }

    @GetMapping("sign-up-with-errors")
    public String registrationWithErrors(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserCreateEditDto());

        }
        return "user/sign-up-with-errors";
    }

    @PostMapping("/sign-up")
    public String create(@ModelAttribute("user") @Validated UserCreateEditDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        log.info("Sign-up attempt for user: {}", user.getUsername());
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("passwordsError", "Passwords do not match.");
            return "redirect:/sign-up-with-errors";
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/sign-up-with-errors";
        }

        try {
            userService.create(user);
            log.info("User created");
        } catch (SignUpException ex) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("usernameError", ex.getMessage());
            return "redirect:/sign-up-with-errors";
        }

        return "redirect:/sign-in";
    }

}
