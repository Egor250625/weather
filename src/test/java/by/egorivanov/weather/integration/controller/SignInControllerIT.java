package by.egorivanov.weather.integration.controller;

import by.egorivanov.weather.TestConfig;
import by.egorivanov.weather.dto.response.SessionDto;
import by.egorivanov.weather.dto.response.UserAuthDto;
import by.egorivanov.weather.http.controller.RegistrationController;
import by.egorivanov.weather.mapper.UserMapper;
import by.egorivanov.weather.service.SessionService;
import by.egorivanov.weather.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@ContextConfiguration(classes = {RegistrationController.class, TestConfig.class, SignUpControllerIT.class})
public class SignInControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SessionService sessionService;


    @Test
    void signIn_shouldRedirectToHome_whenCredentialsAreCorrect() throws Exception {

        String rawPassword = "1234";
        String encodedPassword = new BCryptPasswordEncoder().encode(rawPassword);

        UserAuthDto userDto = new UserAuthDto();
        userDto.setId(1);
        userDto.setUsername("john");
        userDto.setPassword(encodedPassword);

        SessionDto session = new SessionDto(UUID.randomUUID(),
                userDto.getId(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30));


        Mockito.when(userService.findByUsername("john")).thenReturn(userDto);
        Mockito.when(sessionService.create(userDto.getId())).thenReturn(session);


        mockMvc.perform(post("/sign-in")
                        .param("username", "john")
                        .param("password", rawPassword)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"))
                .andExpect(cookie().value("SESSION_ID", session.getSessionId().toString()));
    }
}


