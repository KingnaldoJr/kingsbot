package dev.rmjr.kingsbot.commands.utils;

import dev.rmjr.kingsbot.commands.Command;
import dev.rmjr.kingsbot.commands.CommandCategory;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
public class PingCommand implements Command {

    @Override
    public String getName() { return "ping"; }

    @Override
    public Set<String> getAliases() { return Set.of(); }

    @Override
    public String getShortDescription() {
        return "A simple Ping command.";
    }

    @Override
    public String getLongDescription() {
        return "A simple Ping command, to test if the BOT is connected.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

    @Override
    public Mono<Void> execute(List<String> args, MessageCreateEvent event) {
        return event.getMessage().getChannel()
                .flatMap(channel -> channel.createMessage("Pong!"))
                .then();
    }
}
