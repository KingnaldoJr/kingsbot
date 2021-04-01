package dev.rmjr.kingsbot.listeners;

import dev.rmjr.kingsbot.commands.Command;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    void executeNormalUserAndPrefixTest() {
        MessageCreateEvent eventMock = mock(MessageCreateEvent.class);
        Message messageMock = mock(Message.class);
        User userMock = mock(User.class);

        when(eventMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getContent()).thenReturn(".ping");
        when(messageMock.getAuthor()).thenReturn(Optional.of(userMock));
        when(userMock.isBot()).thenReturn(false);
        doReturn(Mono.empty()).when(listener).executeCommand(any(Message.class), any(MessageCreateEvent.class));

        listener.execute(eventMock).block();

        verify(eventMock, times(1)).getMessage();
        verify(messageMock, times(1)).getAuthor();
        verify(userMock, times(1)).isBot();
        verify(messageMock, times(1)).getContent();
        verify(listener, times(1)).executeCommand(messageMock, eventMock);
    }

    @Test
    void executeNormalUserAndWithoutPrefixTest() {
        MessageCreateEvent eventMock = mock(MessageCreateEvent.class);
        Message messageMock = mock(Message.class);

        when(eventMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getContent()).thenReturn("!ping");

        listener.execute(eventMock).block();

        verify(eventMock, times(1)).getMessage();
        verify(messageMock, times(1)).getContent();
        verifyNoMoreInteractions(messageMock);
        verify(listener, times(0))
                .executeCommand(any(Message.class), any(MessageCreateEvent.class));
    }

    @Test
    void executeBotUserAndWithPrefixTest() {
        MessageCreateEvent eventMock = mock(MessageCreateEvent.class);
        Message messageMock = mock(Message.class);
        User userMock = mock(User.class);

        when(eventMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getContent()).thenReturn(".ping");
        when(messageMock.getAuthor()).thenReturn(Optional.of(userMock));
        when(userMock.isBot()).thenReturn(true);

        listener.execute(eventMock).block();

        verify(eventMock, times(1)).getMessage();
        verify(messageMock, times(1)).getAuthor();
        verify(userMock, times(1)).isBot();
        verify(messageMock, times(1)).getContent();
        verify(listener, times(0))
                .executeCommand(any(Message.class), any(MessageCreateEvent.class));
    }

    @Test
    void executeCommandWithNameTest() {
        MessageCreateEvent eventMock = mock(MessageCreateEvent.class);
        Message messageMock = mock(Message.class);
        Command commandMock = mock(Command.class);

        when(messageMock.getContent()).thenReturn(".ping");
        when(commands.stream()).thenReturn(Stream.of(commandMock));
        when(commandMock.getName()).thenReturn("ping");

        listener.executeCommand(messageMock, eventMock);

        verify(commandMock, times(1)).execute(List.of(), eventMock);
    }

    @Test
    void executeCommandWithAliasesTest() {
        MessageCreateEvent eventMock = mock(MessageCreateEvent.class);
        Message messageMock = mock(Message.class);
        Command commandMock = mock(Command.class);

        when(messageMock.getContent()).thenReturn(".p");
        when(commands.stream()).thenReturn(Stream.of(commandMock));
        when(commandMock.getName()).thenReturn("ping");
        when(commandMock.getAliases()).thenReturn(Set.of("p"));

        listener.executeCommand(messageMock, eventMock);

        verify(commandMock, times(1)).execute(List.of(), eventMock);
    }

    @Test
    void executeCommandWithNameAndArgsTest() {
        MessageCreateEvent eventMock = mock(MessageCreateEvent.class);
        Message messageMock = mock(Message.class);
        Command commandMock = mock(Command.class);

        when(messageMock.getContent()).thenReturn(".ping rmjr.dev");
        when(commands.stream()).thenReturn(Stream.of(commandMock));
        when(commandMock.getName()).thenReturn("ping");

        listener.executeCommand(messageMock, eventMock);

        verify(commandMock, times(1)).execute(List.of("rmjr.dev"), eventMock);
    }

    @Test
    void executeCommandWithoutMatchesTest() {
        MessageCreateEvent eventMock = mock(MessageCreateEvent.class);
        Message messageMock = mock(Message.class);
        Command commandMock = mock(Command.class);

        when(messageMock.getContent()).thenReturn(".ping");
        when(commands.stream()).thenReturn(Stream.of(commandMock));
        when(commandMock.getName()).thenReturn("pong");

        Mono<Void> actualMono = listener.executeCommand(messageMock, eventMock);

        verify(commandMock, times(0)).execute(List.of(), eventMock);
        assertEquals(Mono.empty(), actualMono);
    }

    @Test
    void handleErrorTest() {
        Mono<Void> expectedMono = Mono.empty();

        doReturn(MessageCreateEvent.class).when(listener).getEventType();

        Mono<Void> actualMono = listener.handleError(new Throwable());

        assertEquals(expectedMono, actualMono);
    }
}
