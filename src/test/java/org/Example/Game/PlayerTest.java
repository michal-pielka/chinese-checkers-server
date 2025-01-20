package org.Example.Game;
import org.example.Game.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.PrintWriter;

public class PlayerTest {
    private Player player;
    private PrintWriter mockWriter;

    @BeforeEach
    public void setUp() {
        mockWriter = mock(PrintWriter.class);
        player = new Player("Player1", mockWriter);
    }

    @Test
    public void testGetName() {
        assertEquals("Player1", player.getName());
    }

    @Test
    public void testSetName() {
        player.setName("NewName");
        assertEquals("NewName", player.getName());
    }

    @Test
    public void testSendMessage() {
        String message = "Test message";

        player.sendMessage(message);

        verify(mockWriter).println(message);
    }
}
