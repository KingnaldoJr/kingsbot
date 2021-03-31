package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import discord4j.voice.AudioProvider;

import java.nio.ByteBuffer;

public class LavaPlayerAudioProvider extends AudioProvider {
    private final MutableAudioFrame frame = new MutableAudioFrame();
    private final AudioPlayer player;

    public LavaPlayerAudioProvider(final AudioPlayer player) {
        super(ByteBuffer.allocate(StandardAudioDataFormats.DISCORD_OPUS.maximumChunkSize()));
        frame.setBuffer(getBuffer());
        this.player = player;
    }

    @Override
    public boolean provide() {
        final boolean didProvide = player.provide(frame);
        if(didProvide) { getBuffer().flip(); }
        return didProvide;
    }
}
