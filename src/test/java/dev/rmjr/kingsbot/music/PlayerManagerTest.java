package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerManagerTest {

    @Mock
    AudioPlayer player;

    @Spy
    @InjectMocks
    PlayerManager manager;

    @Test
    void previousTrackTest() {
        AudioTrack trackMock = mock(AudioTrack.class);

        doReturn(1, 0).when(manager).getPosition();
        doReturn(List.of(trackMock)).when(manager).getQueue();

        manager.previousTrack();

        verify(manager, times(2)).getPosition();
        verify(manager, times(1)).setPosition(0);
        verify(player, times(1)).playTrack(trackMock);
    }

    @Test
    void restartTrackTest() {
        AudioTrack trackMock = mock(AudioTrack.class);

        doReturn(0).when(manager).getPosition();
        doReturn(List.of(trackMock)).when(manager).getQueue();

        manager.restartTrack();

        verify(manager, times(1)).getPosition();
        verify(player, times(1)).playTrack(trackMock);
    }

    @Test
    void nextTrackIfLastTest() {
        AudioTrack trackMock = mock(AudioTrack.class);

        doReturn(0).when(manager).getPosition();
        doReturn(List.of(trackMock)).when(manager).getQueue();

        manager.nextTrack();

        verify(manager, times(1)).getPosition();
        verifyNoInteractions(player);
    }

    @Test
    void nextTrackIfNotLastTest() {
        AudioTrack trackMock = mock(AudioTrack.class);

        doReturn(-1, -1, 0).when(manager).getPosition();
        doReturn(List.of(trackMock)).when(manager).getQueue();

        manager.nextTrack();

        verify(manager, times(3)).getPosition();
        verify(manager, times(1)).setPosition(0);
        verify(player, times(1)).playTrack(trackMock);
    }

    @Test
    void checkIfPlayingAndPlayWhenNotPlayingTest() {
        when(player.getPlayingTrack()).thenReturn(null);

        manager.checkIfPlayingAndPlay();

        verify(manager, times(1)).nextTrack();
    }

    @Test
    void checkIfPlayingAndPlayWhenPlayingTest() {
        when(player.getPlayingTrack()).thenReturn(mock(AudioTrack.class));

        manager.checkIfPlayingAndPlay();

        verify(manager, times(0)).nextTrack();
    }

    @Test
    void addTrackTest() {
        List<AudioTrack> queueMock = new ArrayList<>();
        AudioTrack trackMock = mock(AudioTrack.class);

        doReturn(queueMock).when(manager).getQueue();

        manager.addTrack(trackMock);

        assertEquals(trackMock, queueMock.get(0));
        verify(manager, times(1)).checkIfPlayingAndPlay();
    }

    @Test
    void addPlaylistWithResultSearchTest() {
        AudioPlaylist playlistMock = mock(AudioPlaylist.class);
        AudioTrack expectedAudioTrack = mock(AudioTrack.class);

        when(playlistMock.isSearchResult()).thenReturn(true);
        when(playlistMock.getTracks()).thenReturn(List.of(expectedAudioTrack, mock(AudioTrack.class)));

        manager.addPlaylist(playlistMock);

        assertEquals(expectedAudioTrack, manager.getQueue().get(0));
        verify(manager, times(1)).checkIfPlayingAndPlay();
    }

    @Test
    void addPlaylistWithoutResultSearchTest() {
        AudioPlaylist playlistMock = mock(AudioPlaylist.class);
        List<AudioTrack> expectedAudioTracks = List.of(mock(AudioTrack.class), mock(AudioTrack.class));

        when(playlistMock.isSearchResult()).thenReturn(false);
        when(playlistMock.getTracks()).thenReturn(expectedAudioTracks);

        manager.addPlaylist(playlistMock);

        assertEquals(expectedAudioTracks, manager.getQueue());
        verify(manager, times(1)).checkIfPlayingAndPlay();
    }

    @Test
    void onEventInstanceOfTrackEndEventTest() {
        TrackEndEvent eventMock = new TrackEndEvent(player, mock(AudioTrack.class), AudioTrackEndReason.FINISHED);

        doNothing().when(manager).onTrackEnd(any(AudioTrackEndReason.class));

        manager.onEvent(eventMock);

        verify(manager, times(1)).onTrackEnd(any(AudioTrackEndReason.class));
    }

    @Test
    void onTrackEndMayStartNextTest() {
        manager.onTrackEnd(AudioTrackEndReason.FINISHED);

        verify(manager, times(1)).nextTrack();
        verifyNoInteractions(player);
    }

    @Test
    void onTrackEndMayNotStartNextTest() {
        manager.onTrackEnd(AudioTrackEndReason.STOPPED);

        verify(player, times(1)).stopTrack();
        verify(manager, times(0)).nextTrack();
    }
}
