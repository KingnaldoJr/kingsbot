package dev.rmjr.kingsbot.commands.music;

import dev.rmjr.kingsbot.commands.Command;
import dev.rmjr.kingsbot.commands.CommandCategory;
import dev.rmjr.kingsbot.music.PlayersManager;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.entity.Member;
import discord4j.voice.VoiceConnection;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@Component
public class JoinCommand implements Command {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public Set<String> getAliases() {
        return Set.of("j");
    }

    @Override
    public String getShortDescription() {
        return "Makes the bot join your voice channel.";
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
                .flatMap(channel -> PlayersManager.getMusicManager(event.getGuild())
                        .flatMap(musicManager -> {
                            Mono<VoiceConnection> connection =
                                    channel.join(spec -> spec.setProvider(musicManager.getProvider()));
                            musicManager.setVoiceConnection(connection);
                            return connection;
                        }))
                .then();
    }
}
