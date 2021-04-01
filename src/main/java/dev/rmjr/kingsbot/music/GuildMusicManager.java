package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import dev.rmjr.kingsbot.utils.URLUtils;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.VoiceConnection;
import lombok.Getter;
import reactor.core.publisher.Mono;

import java.util.List;

@Getter
public class GuildMusicManager {
    private final AudioPlayer player;
    private final LavaPlayerAudioProvider provider;
    private final PlayerManager playerManager;

    public GuildMusicManager(AudioPlayer player) {
        this.player = player;
        this.provider = new LavaPlayerAudioProvider(player);
        this.playerManager = new PlayerManager(player);
        this.player.addListener(this.playerManager);
    }

    private VoiceConnection voiceConnection;

    public Mono<VoiceConnection> joinChannel(VoiceChannel channel) {
        return channel.join(spec -> spec.setProvider(getProvider()))
                .doOnNext(connection -> this.voiceConnection = connection);
    }

    public Mono<Void> playItem(VoiceChannel channel, List<String> args) {
        if(voiceConnection == null) { joinChannel(channel).subscribe(); }

        if(args.isEmpty()) {
            player.setPaused(false);
            return Mono.empty();
        }

        String identifier = URLUtils.isValidUrl(args.get(0)) ? args.get(0) :
                "ytsearch:" + String.join(" ", args);

        PlayersManager.getPlayerManager().loadItem(identifier, new FunctionalResultHandler(
                playerManager::addTrack, playerManager::addPlaylist, () -> {}, exception -> {}));
        return Mono.empty();
    }
}
