package dev.rmjr.kingsbot.config;

import dev.rmjr.kingsbot.commands.Command;
import dev.rmjr.kingsbot.listeners.EventListener;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class BotConfiguration {

    @Value("${discord.bot.token}")
    private String token;

    @Bean
    public GatewayDiscordClient gatewayDiscordClient(List<EventListener<? extends Event>> eventListeners) {
        GatewayDiscordClient client = DiscordClientBuilder.create(token)
                .build()
                .login()
                .block();

        if(client == null) {
            log.error("Discord Client fails to start.");
            return null;
        }

        eventListeners.forEach(listener ->
                client.getEventDispatcher().on(listener.getEventType())
                        .flatMap(listener::execute)
                        .onErrorResume(listener::handleError)
                        .subscribe());

        return client;
    }

    @Bean
    public List<Command> getAllCommands(List<Command> commands) {
        return commands;
    }
}
