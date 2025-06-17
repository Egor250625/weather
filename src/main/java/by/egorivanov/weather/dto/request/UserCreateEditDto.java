package by.egorivanov.weather.dto.request;

import by.egorivanov.weather.validation.UserInfo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@UserInfo
public class UserCreateEditDto {
    @NotNull
    @Size(min = 5, max = 27)
    private String username;

    @NotNull
    @Size(min = 7, max = 30)
    private String password;


    private String repeatPassword;
}
