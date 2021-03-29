package dev.rmjr.kingsbot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KingsbotApplicationTest {

    @Test
    void mainTest() {
        try(MockedStatic<SpringApplication> springApplicationMock = mockStatic(SpringApplication.class)) {
            KingsbotApplication.main(new String[]{});

            springApplicationMock.verify(() -> SpringApplication.run(
                    ArgumentMatchers.<Class<KingsbotApplication>>any(),
                    ArgumentMatchers.<String[]>any()));
        }
    }
}
