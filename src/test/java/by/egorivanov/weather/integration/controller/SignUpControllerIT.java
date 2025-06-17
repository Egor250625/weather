package by.egorivanov.weather.integration.controller;

import by.egorivanov.weather.TestConfig;
import by.egorivanov.weather.dto.request.UserCreateEditDto;
import by.egorivanov.weather.dto.response.UserReadDto;
import by.egorivanov.weather.http.controller.RegistrationController;
import by.egorivanov.weather.service.SessionService;
import by.egorivanov.weather.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistrationController.class)
@ContextConfiguration(classes = {RegistrationController.class, TestConfig.class, SignUpControllerIT.class})

public class SignUpControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;

    UserCreateEditDto dto = UserCreateEditDto.builder()
            .username("test123")
            .password("password123")
            .repeatPassword("password123").build();


    @Test
    void testSuccessfulRegistration() throws Exception {


        Mockito.when(userService.create(Mockito.any(UserCreateEditDto.class)))
                .thenReturn(new UserReadDto(1, "testuser"));

        mockMvc.perform(post("/sign-up")
                        .param("username", dto.getUsername())
                        .param("password", dto.getPassword())
                        .param("repeatPassword", dto.getRepeatPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign-in"));
    }

    @Test
    void testPasswordMismatch() throws Exception {

        mockMvc.perform(post("/sign-up")
                        .param("username", dto.getUsername())
                        .param("password", dto.getPassword())
                        .param("repeatPassword", dto.getRepeatPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign-up-with-errors"))
                .andExpect(flash().attributeExists("passwordsError"))
                .andExpect(flash().attributeExists("user"));

    }

    @Test
    void testValidationError() throws Exception{ // Username < 5 char

        mockMvc.perform(post("/sign-up")
                        .param("username", dto.getUsername())
                        .param("password", dto.getPassword())
                        .param("repeatPassword", dto.getRepeatPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign-up-with-errors"))
                .andExpect(flash().attributeExists("errors"))
                .andExpect(flash().attributeExists("user"));
    }


}
