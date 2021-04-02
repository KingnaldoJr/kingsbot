package dev.rmjr.kingsbot.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LavaPlayerAudioProviderTest {

    @Mock
    AudioPlayer player;

    @Spy
    @InjectMocks
    LavaPlayerAudioProvider provider;

    @Test
    void constructorTest() {
        try(MockedConstruction<MutableAudioFrame> frameConstruction = mockConstruction(MutableAudioFrame.class)) {
            LavaPlayerAudioProvider createdProvider = new LavaPlayerAudioProvider(player);

            verify(frameConstruction.constructed().get(0), times(1))
                    .setBuffer(createdProvider.getBuffer());
        }
    }

    @Test
    void provideIfProvidedTest() {
        ByteBuffer buffer = mock(ByteBuffer.class);

        doReturn(buffer).when(provider).getBuffer();
        when(player.provide(any(MutableAudioFrame.class))).thenReturn(true);

        boolean actualDidProvide = provider.provide();

        verify(buffer, times(1)).flip();
        assertTrue(actualDidProvide);
    }

    @Test
    void provideIfNotProvidedTest() {
        when(player.provide(any(MutableAudioFrame.class))).thenReturn(false);

        boolean actualDidProvide = provider.provide();

        assertFalse(actualDidProvide);
    }
}
