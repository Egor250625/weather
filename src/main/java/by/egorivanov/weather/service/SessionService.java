package by.egorivanov.weather.service;

import by.egorivanov.weather.dto.response.SessionDto;
import by.egorivanov.weather.exception.SessionException;
import by.egorivanov.weather.mapper.SessionMapper;
import by.egorivanov.weather.model.entity.Sessions;
import by.egorivanov.weather.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;


    @Transactional
    public SessionDto create(int userId) {
        Sessions session = Sessions.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
        log.info("Session create but not save in database");
        sessionRepository.saveAndFlush(session);
        log.info("Session create successful");
        return sessionMapper.toDto(session);
    }

    @Transactional
    public void update(SessionDto sessionDto) {
        Sessions session = sessionRepository.findById(sessionDto.getSessionId())
                .orElseThrow(() -> new SessionException("Session not found for update: " + sessionDto.getSessionId()));
        session.setExpiresAt(sessionDto.getExpiresAt());
        sessionRepository.saveAndFlush(session);
    }


    public SessionDto findById(UUID uuid) {
        return sessionRepository.findById(uuid)
                .map(sessionMapper::toDto)
                .orElseThrow(() -> new SessionException("Session was not found by id."));
    }

    @Transactional
    public boolean delete(UUID uuid) {
        return sessionRepository.findById(uuid)
                .map(entity -> {
                    sessionRepository.delete(entity);
                    sessionRepository.flush();
                    return true;
                })
                .orElse(false);
    }



    @Transactional
    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void deleteExpiredSessions() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(30);
        sessionRepository.deleteExpiredSessions(cutoffTime);
    }


    @Transactional
    public boolean invalidateSession(String sessionIdStr, HttpServletResponse response) {
        boolean deleted = false;
        try {
            UUID sessionId = UUID.fromString(sessionIdStr);
            deleted = delete(sessionId);
        } catch (IllegalArgumentException ignored) {
        }
        Cookie cookie = new Cookie("SESSION_ID", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return deleted;
    }

}
