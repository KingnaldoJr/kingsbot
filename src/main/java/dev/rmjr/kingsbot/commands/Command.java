package dev.rmjr.kingsbot.commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface Command {
    String getName();
    Set<String> getAliases();
    String getShortDescription();

    default String getLongDescription() {
        return getShortDescription();
    }

    CommandCategory getCategory();

    default CommandPermission requiredPermission() {
        return CommandPermission.ALL;
    }

    Mono<Void> execute(List<String> args, MessageCreateEvent event);
}
