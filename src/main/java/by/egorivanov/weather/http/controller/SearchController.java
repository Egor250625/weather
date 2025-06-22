package by.egorivanov.weather.http.controller;

import by.egorivanov.weather.dto.request.LocationRequestDto;
import by.egorivanov.weather.dto.response.WeatherByNameResponseDto;
import by.egorivanov.weather.service.LocationService;
import by.egorivanov.weather.service.SessionService;
import by.egorivanov.weather.service.UserService;
import by.egorivanov.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final SessionService sessionService;
    private final UserService userService;
    private final WeatherService weatherService;
    private final LocationService locationService;

    @GetMapping("/search-results")
    public String searchResult(@CookieValue(value = "SESSION_ID", required = false) String sessionId,
                               @RequestParam("name") String name,
                               Model model
    ) {
        List<WeatherByNameResponseDto> weather = new ArrayList<>();

        if (sessionId == null) {
            return "redirect:/sign-in";
        }
        var session = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(session.getUserId());
        weather = weatherService.getWeatherByCityName(name);
        model.addAttribute("weather", weather);
        model.addAttribute("user", user);
        model.addAttribute("name", name);
        return "weatherPages/search-results";
    }


    @PostMapping("/search-results/add")
    public String addWeather(
            @RequestParam String name,
            @RequestParam String latitude,
            @RequestParam String longitude,
            @CookieValue("SESSION_ID") String sessionId,
            RedirectAttributes redirectAttributes
    ) {
        var session = sessionService.findById(UUID.fromString(sessionId));
        var user = userService.findById(session.getUserId());
        LocationRequestDto dto = new LocationRequestDto(name,
                user.id(),
                Double.parseDouble(latitude),
                Double.parseDouble(longitude));
        if(locationService.existByParameters(dto)){
            redirectAttributes.addFlashAttribute("error","This location is already added.");
            return "redirect:/search-results?name=" + name;
        }
        locationService.save(dto);
        return "redirect:/home";
    }


}
