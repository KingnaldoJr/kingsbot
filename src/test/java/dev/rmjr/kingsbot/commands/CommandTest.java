package dev.rmjr.kingsbot.commands;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommandTest {

    @Mock
    Command command;

    @Test
    void getLongDescriptionTest() {
        String expectedDescription = "Test description";

        doReturn(expectedDescription)
                .when(command).getShortDescription();
        when(command.getLongDescription()).thenCallRealMethod();

        String actualDescription = command.getLongDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void requiredPermissionTest() {
        when(command.requiredPermission()).thenCallRealMethod();

        CommandPermission actualPermission = command.requiredPermission();

        assertEquals(CommandPermission.ALL, actualPermission);
    }
}
