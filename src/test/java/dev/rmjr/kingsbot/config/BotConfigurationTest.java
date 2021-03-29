package dev.rmjr.kingsbot.config;

import dev.rmjr.kingsbot.commands.Command;
import dev.rmjr.kingsbot.commands.utils.PingCommand;
import dev.rmjr.kingsbot.listeners.EventListener;
import dev.rmjr.kingsbot.listeners.MessageCreateListener;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.Event;
import discord4j.rest.request.RouterOptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotConfigurationTest {

    @InjectMocks
    BotConfiguration botConfiguration;

    @Test
    void gatewayDiscordClientNullReturnTest() {
        try(MockedStatic<DiscordClientBuilder> clientBuilder = mockStatic(DiscordClientBuilder.class)) {
            DiscordClientBuilder<DiscordClient, RouterOptions> builderMock = mock(DiscordClientBuilder.class);
            DiscordClient clientMock = mock(DiscordClient.class);

            ReflectionTestUtils.setField(botConfiguration, "token", "");
            clientBuilder.when(() -> DiscordClientBuilder.create(anyString())).thenReturn(builderMock);
            when(builderMock.build()).thenReturn(clientMock);
            when(clientMock.login()).thenReturn(Mono.empty());

            GatewayDiscordClient actualClient = botConfiguration.gatewayDiscordClient(List.of());

            assertNull(actualClient);
        }
    }

    @Test
    void gatewayDiscordClientWithEmptyCommandsTest() {
        try(MockedStatic<DiscordClientBuilder> clientBuilder = mockStatic(DiscordClientBuilder.class)) {
            DiscordClientBuilder<DiscordClient, RouterOptions> builderMock = mock(DiscordClientBuilder.class);
            DiscordClient clientMock = mock(DiscordClient.class);
            GatewayDiscordClient expectedGateway = mock(GatewayDiscordClient.class);

            ReflectionTestUtils.setField(botConfiguration, "token", "");
            clientBuilder.when(() -> DiscordClientBuilder.create(anyString())).thenReturn(builderMock);
            when(builderMock.build()).thenReturn(clientMock);
            when(clientMock.login()).thenReturn(Mono.just(expectedGateway));

            GatewayDiscordClient actualClient = botConfiguration.gatewayDiscordClient(List.of());

            assertEquals(expectedGateway, actualClient);
        }
    }

    @Test
    void gatewayDiscordClientTest() {
        try(MockedStatic<DiscordClientBuilder> clientBuilder = mockStatic(DiscordClientBuilder.class)) {
            DiscordClientBuilder<DiscordClient, RouterOptions> builderMock = mock(DiscordClientBuilder.class);
            DiscordClient clientMock = mock(DiscordClient.class);
            GatewayDiscordClient expectedGateway = mock(GatewayDiscordClient.class);
            EventDispatcher eventDispatcherMock = mock(EventDispatcher.class);

            MessageCreateListener messageListener = new MessageCreateListener(List.of(new PingCommand()));
            List<EventListener<? extends Event>> eventListeners = List.of(messageListener);

            ReflectionTestUtils.setField(botConfiguration, "token", "");
            clientBuilder.when(() -> DiscordClientBuilder.create(anyString())).thenReturn(builderMock);
            when(builderMock.build()).thenReturn(clientMock);
            when(clientMock.login()).thenReturn(Mono.just(expectedGateway));

            when(expectedGateway.getEventDispatcher()).thenReturn(eventDispatcherMock);
            when(eventDispatcherMock.on(ArgumentMatchers.<Class<Event>>any())).thenReturn(Flux.empty());

            GatewayDiscordClient actualClient = botConfiguration.gatewayDiscordClient(eventListeners);

            verify(eventDispatcherMock, times(1)).on(eq(messageListener.getEventType()));
            assertEquals(expectedGateway, actualClient);
        }
    }

    @Test
    void getAllCommandsTest() {
        List<Command> expectedCommands = List.of(new PingCommand());

        List<Command> actualCommands = botConfiguration.getAllCommands(expectedCommands);

        assertEquals(expectedCommands, actualCommands);
    }
}
