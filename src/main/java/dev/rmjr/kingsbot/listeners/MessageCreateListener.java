package dev.rmjr.kingsbot.listeners;

import dev.rmjr.kingsbot.commands.Command;
import discord4j.core.event.domain.Event;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class MessageCreateListener implements EventListener<MessageCreateEvent> {
    private final List<Command> commands;

    @Value("${discord.bot.prefix}")
    private String prefix;

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public <E extends Event> Mono<Void> execute(E event) {
        MessageCreateEvent messageCreateEvent = (MessageCreateEvent) event;
        return Mono.just(messageCreateEvent.getMessage())
                .filter(message -> message.getAuthor().map(author -> !author.isBot()).orElse(false))
                .filter(message -> message.getContent().startsWith(prefix))
                .flatMap(message -> this.executeCommand(message, messageCreateEvent));
    }

    private Mono<Void> executeCommand(Message message, MessageCreateEvent event) {
        String[] messageSplit = message.getContent().substring(1).split(" ", 2);

        return commands.stream()
                .filter(command ->
                        command.getName().equals(messageSplit[0]) ||
                                command.getAliases().contains(messageSplit[0]))
                .findFirst()
                .map(command -> {
                    List<String> args = messageSplit.length == 2 ?
                            List.of(messageSplit[1].split(" ")) :
                            List.of();
                    return command.execute(args, event);
                })
                .orElse(Mono.empty());
    }

    @Override
    public Mono<Void> handleError(Throwable error) {
        log.error("Error handling " + getEventType() + ":", error);
        return Mono.empty();
    }
}
