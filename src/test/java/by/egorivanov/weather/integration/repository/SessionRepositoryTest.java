package by.egorivanov.weather.integration.repository;

import by.egorivanov.weather.dto.response.SessionDto;
import by.egorivanov.weather.mapper.SessionMapper;
import by.egorivanov.weather.model.entity.Sessions;
import by.egorivanov.weather.repository.SessionRepository;
import by.egorivanov.weather.service.SessionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
public class SessionRepositoryTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionService sessionService;


    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        Sessions sessionsEntity = new Sessions();
        sessionsEntity.setId(id);
        sessionsEntity.setUserId(1);
        sessionsEntity.setExpiresAt(LocalDateTime.now().plusHours(1));

        SessionDto sessionDto = new SessionDto(id, 1, sessionsEntity.getExpiresAt(),LocalDateTime.now());


        Mockito.when(sessionRepository.findById(id)).thenReturn(Optional.of(sessionsEntity));
        Mockito.when(sessionMapper.toDto(sessionsEntity)).thenReturn(sessionDto);


        SessionDto result = sessionService.findById(id);


        Assertions.assertEquals(sessionDto, result);


        Mockito.verify(sessionRepository).findById(id);
        Mockito.verify(sessionMapper).toDto(sessionsEntity);
    }

    @Test
    void deleteTest() {
        UUID sessionId = UUID.randomUUID();
        Sessions session = new Sessions();
        session.setId(sessionId);
        session.setUserId(1);
        session.setExpiresAt(LocalDateTime.now().plusHours(1));


        Mockito.when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        boolean result = sessionService.delete(sessionId);

        Assertions.assertTrue(result);
        Mockito.verify(sessionRepository).findById(sessionId);
        Mockito.verify(sessionRepository).delete(session);
        Mockito.verify(sessionRepository).flush();
    }


    @Test
    void testDeleteExpiredSessions() {
        LocalDateTime now = LocalDateTime.of(2025, 6, 12, 12, 0);
        LocalDateTime expectedCutoff = now.minusMinutes(30);

        try (MockedStatic<LocalDateTime> mockedNow = Mockito.mockStatic(LocalDateTime.class,
                Mockito.CALLS_REAL_METHODS)) {
            mockedNow.when(LocalDateTime::now).thenReturn(now);

            sessionService.deleteExpiredSessions();

            Mockito.verify(sessionRepository).deleteExpiredSessions(expectedCutoff);
        }
    }

}
