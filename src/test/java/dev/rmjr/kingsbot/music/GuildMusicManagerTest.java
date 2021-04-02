package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import discord4j.common.util.Snowflake;
import discord4j.core.object.entity.channel.VoiceChannel;
import discord4j.voice.VoiceConnection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GuildMusicManagerTest {

    @Mock
    AudioPlayer player;

    @Mock
    LavaPlayerAudioProvider provider;

    @Mock
    PlayerManager playerManager;

    @Spy
    @InjectMocks
    GuildMusicManager manager;

    @Test
    void constructorTest() {
        try(MockedConstruction<LavaPlayerAudioProvider> providerConstruction =
                    mockConstruction(LavaPlayerAudioProvider.class);
            MockedConstruction<PlayerManager> playerManagerConstruction = mockConstruction(PlayerManager.class)) {

            doNothing().when(player).addListener(any(PlayerManager.class));

            GuildMusicManager manager = new GuildMusicManager(player);

            assertEquals(player, manager.getPlayer());
            assertEquals(providerConstruction.constructed().get(0), manager.getProvider());
            assertEquals(playerManagerConstruction.constructed().get(0), manager.getPlayerManager());
            verify(player, times(1)).addListener(playerManagerConstruction.constructed().get(0));
        }
    }

    @Test
    void joinChannelTest() {
        VoiceChannel channelMock = mock(VoiceChannel.class);
        VoiceConnection expectedConnection = mock(VoiceConnection.class);

        when(channelMock.join(any())).thenReturn(Mono.just(expectedConnection));

        VoiceConnection actualConnection = manager.joinChannel(channelMock).block();

        assertEquals(expectedConnection, actualConnection);
    }

    @Test
    void leaveChannelTest() {
        VoiceChannel channelMock = mock(VoiceChannel.class);
        VoiceConnection connectionMock = mock(VoiceConnection.class);

        doReturn(connectionMock).when(manager).getVoiceConnection();
        when(connectionMock.getChannelId()).thenReturn(Mono.just(Snowflake.of("123")));
        when(channelMock.getId()).thenReturn(Snowflake.of("123"));
        when(connectionMock.disconnect()).thenReturn(Mono.empty());

        manager.leaveChannel(channelMock).blockOptional();

        verify(connectionMock, times(1)).disconnect();
    }

    @Test
    void playItemNullVoiceConnectionAndNoArgsTest() {
        VoiceChannel channelMock = mock(VoiceChannel.class);
        VoiceConnection connectionMock = mock(VoiceConnection.class);

        doReturn(null).when(manager).getVoiceConnection();
        doReturn(Mono.just(connectionMock)).when(manager).joinChannel(any(VoiceChannel.class));
        doNothing().when(player).setPaused(anyBoolean());

        manager.playItem(channelMock, List.of());

        verify(manager, times(1)).joinChannel(channelMock);
        verify(player, times(1)).setPaused(false);
        verifyNoInteractions(playerManager);
    }

    @Test
    void playItemWithURLTest() {
        try(MockedStatic<PlayersManager> playersManagerMock = mockStatic(PlayersManager.class)) {
            VoiceChannel channelMock = mock(VoiceChannel.class);
            VoiceConnection connectionMock = mock(VoiceConnection.class);
            AudioPlayerManager playerManagerMock = mock(AudioPlayerManager.class);
            Future<Void> futureMock = mock(Future.class);

            doReturn(connectionMock).when(manager).getVoiceConnection();
            playersManagerMock.when(PlayersManager::getPlayerManager).thenReturn(playerManagerMock);
            when(playerManagerMock.loadItem(anyString(), any(FunctionalResultHandler.class))).thenReturn(futureMock);

            manager.playItem(channelMock, List.of("https://www.youtube.com/watch?v=5qap5aO4i9A")).block();

            verifyNoInteractions(channelMock);
            verify(playerManagerMock, times(1))
                    .loadItem(eq("https://www.youtube.com/watch?v=5qap5aO4i9A"),
                            any(FunctionalResultHandler.class));
        }
    }

    @Test
    void playItemWithSearchTest() {
        try(MockedStatic<PlayersManager> playersManagerMock = mockStatic(PlayersManager.class)) {
            VoiceChannel channelMock = mock(VoiceChannel.class);
            VoiceConnection connectionMock = mock(VoiceConnection.class);
            AudioPlayerManager playerManagerMock = mock(AudioPlayerManager.class);
            Future<Void> futureMock = mock(Future.class);

            doReturn(connectionMock).when(manager).getVoiceConnection();
            playersManagerMock.when(PlayersManager::getPlayerManager).thenReturn(playerManagerMock);
            when(playerManagerMock.loadItem(anyString(), any(FunctionalResultHandler.class))).thenReturn(futureMock);

            manager.playItem(channelMock,
                    List.of("lofi", "hip", "hop", "radio", "-", "beats", "to", "relax/study", "to")).block();

            verifyNoInteractions(channelMock);
            verify(playerManagerMock, times(1))
                    .loadItem(eq("ytsearch:lofi hip hop radio - beats to relax/study to"),
                            any(FunctionalResultHandler.class));
        }
    }
}
