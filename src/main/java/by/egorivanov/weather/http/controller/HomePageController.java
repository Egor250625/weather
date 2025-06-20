package by.egorivanov.weather.http.controller;

import by.egorivanov.weather.dto.request.LocationDeleteRequestDto;
import by.egorivanov.weather.dto.response.WeatherByCoordinateResponseDto;
import by.egorivanov.weather.service.LocationService;
import by.egorivanov.weather.service.SessionService;
import by.egorivanov.weather.service.UserService;
import by.egorivanov.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class HomePageController {
    private final LocationService locationService;
    private final WeatherService weatherService;
    private final UserService userService;
    private final SessionService sessionService;


    @GetMapping("/home")
    public String homePage(@CookieValue(value = "SESSION_ID", required = false) String sessionId, Model model) {
        List<WeatherByCoordinateResponseDto> weathers = new ArrayList<>();
        if (sessionId != null) {
            var session = sessionService.findById(UUID.fromString(sessionId));
            if (session != null) {
                var user = userService.findById(session.getUserId());
                weathers = locationService.findLocationsByUserId(user.id());
                model.addAttribute("user", user);
            }
        }
        model.addAttribute("weathers", weathers);
        return "weatherPages/home";
    }


    @PostMapping("/delete")
    public String deleteLocation(@CookieValue(value = "SESSION_ID", required = false) String sessionId,
                                 String latitude,
                                 String longitude) {
        var session = sessionService.findById(UUID.fromString(sessionId));
        var userId = session.getUserId();

        try {
            locationService.delete(new LocationDeleteRequestDto(userId,
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude)));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Wrong argument of coordinates");
        }
        return "redirect:/home";
    }
}
