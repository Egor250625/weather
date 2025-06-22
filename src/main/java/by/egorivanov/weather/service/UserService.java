package by.egorivanov.weather.service;

import by.egorivanov.weather.dto.response.UserAuthDto;
import by.egorivanov.weather.dto.request.UserCreateEditDto;
import by.egorivanov.weather.dto.response.UserReadDto;
import by.egorivanov.weather.exception.SignUpException;
import by.egorivanov.weather.exception.UsernameNotFoundException;
import by.egorivanov.weather.exception.WrongIdException;
import by.egorivanov.weather.mapper.UserMapper;
import by.egorivanov.weather.model.entity.Users;
import by.egorivanov.weather.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @Transactional
    public UserReadDto create(UserCreateEditDto userDto) {
        return Optional.of(userDto)
                .filter(
                        dto -> !usersRepository.existsByUsername(dto.getUsername()))
                .map(
                        dto -> {
                            Users user = userMapper.toEntity(dto);
                            user.setPassword(passwordEncoder.encode(dto.getPassword()));
                            return usersRepository.save(user);
                        })
                .map(userMapper::toDto)
                .orElseThrow(() -> new SignUpException("Account with this email already exists.Or it is incorrect"));
    }


    public UserAuthDto findByUsername(String username) {
        return usersRepository.findByUsername(username)
                .map(userMapper::toAuthDto)
                .orElseThrow(() -> new UsernameNotFoundException("User with this Username was not found"));
    }


    public UserReadDto findById(Integer id) {
        return usersRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new WrongIdException("User with this Id was not found"));
    }

}
