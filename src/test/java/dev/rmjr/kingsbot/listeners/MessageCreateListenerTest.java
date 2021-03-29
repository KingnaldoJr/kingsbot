package dev.rmjr.kingsbot.listeners;

import dev.rmjr.kingsbot.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class MessageCreateListenerTest {

    @Mock
    List<Command> commands;

    @Spy
    @InjectMocks
    MessageCreateListener listener;

    @BeforeEach
    void mockPrefix() {
        ReflectionTestUtils.setField(listener, "prefix", ".");
    }

    @Test
    void getEventTypeTest() {
        Class<MessageCreateEvent> actualEventType = listener.getEventType();

        assertEquals(MessageCreateEvent.class, actualEventType);
    }
}
