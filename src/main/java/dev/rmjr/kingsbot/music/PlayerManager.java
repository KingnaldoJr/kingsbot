package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter(AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@RequiredArgsConstructor
public class PlayerManager implements AudioEventListener {
    private final List<AudioTrack> queue = new CopyOnWriteArrayList<>();
    private final AudioPlayer player;
    private int position = -1;

    public void previousTrack() {
        setPosition(getPosition() - 1);
        getPlayer().playTrack(getQueue().get(getPosition()));
    }

    public void restartTrack() {
        getPlayer().playTrack(getQueue().get(getPosition()));
    }

    public void nextTrack() {
        if(getPosition() >= getQueue().size() - 1) { return; }
        setPosition(getPosition() + 1);
        getPlayer().playTrack(getQueue().get(getPosition()));
    }

    protected void checkIfPlayingAndPlay() {
        if(getPlayer().getPlayingTrack() == null) { nextTrack(); }
    }

    public void addTrack(AudioTrack track) {
        getQueue().add(track);
        checkIfPlayingAndPlay();
    }

    public void addPlaylist(AudioPlaylist playlist) {
        if(playlist.isSearchResult()) {
            getQueue().add(playlist.getTracks().get(0));
        }else{ getQueue().addAll(playlist.getTracks()); }

        checkIfPlayingAndPlay();
    }

    @Override
    public void onEvent(AudioEvent event) {
        if(event instanceof TrackEndEvent) {
            onTrackEnd(((TrackEndEvent) event).endReason);
        }
    }

    protected void onTrackEnd(AudioTrackEndReason reason) {
        if(!reason.mayStartNext) {
            getPlayer().stopTrack();
            return;
        }
        nextTrack();
    }
}
