package dev.rmjr.kingsbot.commands.music;

import dev.rmjr.kingsbot.commands.Command;
import dev.rmjr.kingsbot.commands.CommandCategory;
import dev.rmjr.kingsbot.music.PlayersManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
public class PlayCommand implements Command {

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public Set<String> getAliases() {
        return Set.of("p");
    }

    @Override
    public String getShortDescription() {
        return "Plays or add the input to queue.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MUSIC;
    }

    @Override
    public Mono<Void> execute(List<String> args, MessageCreateEvent event) {
        return Mono.justOrEmpty(event.getMember())
                .flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> PlayersManager.getMusicManager(channel.getGuild())
                        .flatMap(manager -> manager.playItem(channel, args)))
                .then();
    }
}
