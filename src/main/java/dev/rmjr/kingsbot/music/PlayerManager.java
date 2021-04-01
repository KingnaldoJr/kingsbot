package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class PlayerManager implements AudioEventListener {
    private final List<AudioTrack> queue = new CopyOnWriteArrayList<>();
    private final AudioPlayer player;
    private int position = -1;

    public void previousTrack() {
        player.startTrack(queue.get(--position), false);
    }

    public void restartTrack() {
        player.startTrack(queue.get(position), false);
    }

    public void nextTrack() {
        if(position >= queue.size() - 1) { return; }
        player.playTrack(queue.get(++position));
    }

    protected void checkIfPlayingAndPlay() {
        if(player.getPlayingTrack() == null) { nextTrack(); }
    }

    public void addTrack(AudioTrack track) {
        queue.add(track);
        checkIfPlayingAndPlay();
    }

    public void addPlaylist(AudioPlaylist playlist) {
        if(playlist.isSearchResult()) {
            queue.add(playlist.getTracks().get(0));
        }else{ queue.addAll(playlist.getTracks()); }

        checkIfPlayingAndPlay();
    }

    @Override
    public void onEvent(AudioEvent event) {
        if(event instanceof TrackEndEvent) {
            onTrackEnd(((TrackEndEvent) event).endReason);
        }
    }

    private void onTrackEnd(AudioTrackEndReason reason) {
        if(!reason.mayStartNext) { player.stopTrack(); }
        nextTrack();
    }
}
