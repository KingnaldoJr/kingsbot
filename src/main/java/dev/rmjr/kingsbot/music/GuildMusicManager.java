package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import discord4j.voice.VoiceConnection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Getter
@RequiredArgsConstructor
public class GuildMusicManager {
    private final AudioPlayer player;
    private final LavaPlayerAudioProvider provider;

    private VoiceConnection voiceConnection;

    public Mono<Void> setVoiceConnection(Mono<VoiceConnection> voiceConnection) {
        return voiceConnection
                .doOnNext(connection -> this.voiceConnection = connection)
                .then();
    }
}
