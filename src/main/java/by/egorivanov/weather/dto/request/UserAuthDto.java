package by.egorivanov.weather.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserAuthDto {
    private Integer id;
    //    @NotNull
//    @Size(min = 5, max = 27)
    private String username;
    //    @NotNull
//    @Size(min = 7, max = 30)
    private String password;
}
