package dev.rmjr.kingsbot.commands.utils;

import dev.rmjr.kingsbot.commands.CommandCategory;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PingCommandTest {

    @InjectMocks
    PingCommand command;

    @Test
    void getNameTest() {
        String expectedName = "ping";

        String actualName = command.getName();

        assertEquals(expectedName, actualName);
    }

    @Test
    void getAliasesTest() {
        Set<String> expectedAliases = Set.of();

        Set<String> actualAliases = command.getAliases();

        assertEquals(expectedAliases, actualAliases);
    }

    @Test
    void getShortDescriptionTest() {
        String expectedDescription = "A simple Ping command.";

        String actualDescription = command.getShortDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void getLongDescriptionTest() {
        String expectedDescription = "A simple Ping command, to test if the BOT is connected.";

        String actualDescription = command.getLongDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void getCategoryTest() {
        CommandCategory expectedCategory = CommandCategory.UTILS;

        CommandCategory actualCategory = command.getCategory();

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void executeTest() {
        MessageCreateEvent eventMock = mock(MessageCreateEvent.class);
        Message messageMock = mock(Message.class);
        MessageChannel channelMock = mock(MessageChannel.class);
        Mono<MessageChannel> monoChannelMock = Mono.just(channelMock);
        Message sendMessageMock = mock(Message.class);
        Mono<Message> monoMessageMock = Mono.just(sendMessageMock);

        when(eventMock.getMessage()).thenReturn(messageMock);
        when(messageMock.getChannel()).thenReturn(monoChannelMock);
        when(channelMock.createMessage(anyString())).thenReturn(monoMessageMock);

        command.execute(List.of(), eventMock).block();

        verify(channelMock, times(1)).createMessage("Pong!");
    }
}
