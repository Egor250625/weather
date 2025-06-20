package by.egorivanov.weather;

import by.egorivanov.weather.service.SessionService;
import by.egorivanov.weather.service.UserService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public UserService userService() {
        return Mockito.mock(UserService.class);
    }

    @Bean
    public SessionService sessionService() {
        return Mockito.mock(SessionService.class);
    }


}
