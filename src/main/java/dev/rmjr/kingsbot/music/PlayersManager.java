package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioConfiguration;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.Guild;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayersManager {
    private PlayersManager() {}

    private static final AudioPlayerManager PLAYER_MANAGER = new DefaultAudioPlayerManager();
    private static final Map<Snowflake, GuildMusicManager> PLAYERS = new ConcurrentHashMap<>();

    static {
        PLAYER_MANAGER.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        PLAYER_MANAGER.getConfiguration().setResamplingQuality(AudioConfiguration.ResamplingQuality.HIGH);
        PLAYER_MANAGER.getConfiguration().setOpusEncodingQuality(AudioConfiguration.OPUS_QUALITY_MAX);
        AudioSourceManagers.registerRemoteSources(PLAYER_MANAGER);
    }

    public static Mono<GuildMusicManager> getMusicManager(Mono<Guild> guild) {
        return guild.flatMap(PlayersManager::getMusicManager);
    }

    public static Mono<GuildMusicManager> getMusicManager(Guild guild) {
        if(!PLAYERS.containsKey(guild.getId())) {
            AudioPlayer player = PLAYER_MANAGER.createPlayer();
            PLAYERS.put(guild.getId(), new GuildMusicManager(player));
        }

        return Mono.just(PLAYERS.get(guild.getId()));
    }

    protected static AudioPlayer createPlayer() {
        AudioPlayer player = PLAYER_MANAGER.createPlayer();
        player.addListener(new PlayerManager(player));
        return player;
    }

    public static Mono<Void> removeMusicManager(Mono<Guild> guildMono) {
        return guildMono.flatMap(guild -> {
            PLAYERS.get(guild.getId()).getPlayer().destroy();
            PLAYERS.remove(guild.getId());
            return Mono.empty();
        });
    }

    protected static AudioPlayerManager getPlayerManager() {
        return PLAYER_MANAGER;
    }
}
