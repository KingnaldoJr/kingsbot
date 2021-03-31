package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RequiredArgsConstructor
public class PlayerManager implements AudioEventListener, AudioLoadResultHandler {
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

    @Override
    public void trackLoaded(AudioTrack track) {
        if(!player.startTrack(track, true)) { queue.add(track); }
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        if(playlist.getTracks().isEmpty()) { return; }

        AudioTrack track = playlist.getSelectedTrack() == null ?
                playlist.getTracks().get(0) :
                playlist.getSelectedTrack();

        if(!player.startTrack(track, true)) { queue.add(track); }
        playlist.getTracks().remove(track);
        queue.addAll(playlist.getTracks());
    }

    @Override
    public void noMatches() {
        // TODO
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        // TODO
    }
}
