package by.egorivanov.weather.http.handler;

import by.egorivanov.weather.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistException.class)
    public ModelAndView handleUsernameAlreadyExists(UsernameAlreadyExistException ex) {
        ModelAndView mav = new ModelAndView("sign-up");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ModelAndView handleUsernameNotFound(UsernameNotFoundException ex) {
        ModelAndView mav = new ModelAndView("sign-in");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(SignUpException.class)
    public ModelAndView handleSignUpException(SignUpException ex) {
        ModelAndView mav = new ModelAndView("sign-up");
        mav.addObject("error", ex.getMessage());
        return mav;
    }

    @ExceptionHandler(SessionException.class)
    public ModelAndView handleSessionException(SessionException ex) {
        return new ModelAndView("redirect:/sign-in");
    }

    @ExceptionHandler(WrongIdException.class)
    public ModelAndView handleWrongIdException(WrongIdException ex) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", ex.getMessage());
        return mav;
    }
}

