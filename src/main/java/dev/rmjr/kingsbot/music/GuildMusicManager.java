package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GuildMusicManager {
    private final AudioPlayer player;
    private final LavaPlayerAudioProvider provider;
}
