package by.egorivanov.weather.http.controller;

import by.egorivanov.weather.service.LocationService;
import by.egorivanov.weather.service.SessionService;
import by.egorivanov.weather.service.UserService;
import by.egorivanov.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class HomePageController {
    private final LocationService locationService;
    private final WeatherService weatherService;
    private final UserService userService;
    private final SessionService sessionService;


    @GetMapping("/home")
    public String homePage(@CookieValue("SESSION_ID") String sessionId, Model model) {
        var session = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(session.getUserId());
        var weathers = locationService.findLocationsByUserId(user.id());
        model.addAttribute("weathers", weathers);
        model.addAttribute("user", user);
        return "weatherPages/home";
    }
}
