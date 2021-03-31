package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.*;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class PlayerManager implements AudioEventListener {
    private final List<AudioTrack> queue = new CopyOnWriteArrayList<>();
    private final AudioPlayer player;
    private int position = 0;

    public void previousTrack() {
        player.startTrack(queue.get(--position), false);
    }

    public void restartTrack() {
        player.startTrack(queue.get(position), false);
    }

    public void nextTrack() {
        player.startTrack(queue.get(++position), false);
    }

    public void queueTrack(AudioTrack track) {
        if(!player.startTrack(track, true)) { queue.add(track); }
    }

    private void onTrackEnd(AudioPlayer player, AudioTrackEndReason reason) {
        if(!reason.mayStartNext) {
            player.stopTrack();
            return;
        }
        nextTrack();
    }

    @Override
    public void onEvent(AudioEvent event) {
        if(event instanceof TrackEndEvent) {
            onTrackEnd(event.player, ((TrackEndEvent) event).endReason);
        }
    }
}
