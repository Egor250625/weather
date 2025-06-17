package by.egorivanov.weather.integration.service;

import by.egorivanov.weather.dto.request.UserCreateEditDto;
import by.egorivanov.weather.dto.response.UserReadDto;
import by.egorivanov.weather.exception.SignUpException;
import by.egorivanov.weather.integration.annotations.IT;
import by.egorivanov.weather.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IT

public class UserServiceIT {

    @Autowired
    private  UserService userService;


    @Test
    void createTest() {
        UserCreateEditDto dto = new UserCreateEditDto("testuser@gmail.com",
                "123456grrG",
                "123456grrG");
        UserReadDto result = userService.create(dto);
        assertThat(result.username()).isEqualTo("testuser@gmail.com");
         assertThat(result.id()).isPositive();

    }

    @Test
    void createUserError(){
        UserCreateEditDto dto = new UserCreateEditDto("tes",
                "123456grrG",
                "123456grrG");
//        UserReadDto result = userService.create(dto);

        Assertions.assertThrows(SignUpException.class, () -> {
            UserReadDto result = userService.create(dto);
        });

    }

}
