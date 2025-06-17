package by.egorivanov.weather.validation;

import by.egorivanov.weather.dto.request.UserCreateEditDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UserInfoValidation implements ConstraintValidator<UserInfo, UserCreateEditDto> {


    @Override
    public boolean isValid(UserCreateEditDto dto, ConstraintValidatorContext context) {
        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
            return true;
        }

        boolean isValid = !dto.getUsername().equals(dto.getPassword());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("password")
                    .addConstraintViolation();
        }

        return isValid;
    }



}
