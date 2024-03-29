package it.polimi.ingsw.test.controllerTest;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exceptions.LobbyException;
import it.polimi.ingsw.exceptions.PlayerAlreadyExistsException;
import it.polimi.ingsw.network.messages.LobbyJoinMessage;
import it.polimi.ingsw.network.messages.LobbySetMessage;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Check that the lobby controller is working
 */
public class LobbyControllerTest {
    /**
     * Check if the lobby correctly sets if the first player asks and fails to set when the second player tries to create another one.
     */
    @Test
    public void LobbyControllerTest1(){
        LobbyController l=new LobbyController();
        String s="Giorgio";
        try {
            l.handle_setLobby(new LobbySetMessage(s, 0));
            fail();
        } catch (IndexOutOfBoundsException e){
            assertTrue(true);
        }
        l.handle_setLobby(new LobbySetMessage(s, 1));
        assertTrue(l.isFull());
        String p="Paolo";
        try {
            l.handle_setLobby(new LobbySetMessage(p, 3));
            fail();
        } catch (LobbyException e) {
            assertTrue(true);
        }
        try {
            l.handle_addInLobby(new LobbyJoinMessage(p));
            fail();
        } catch (LobbyException e) {
            assertTrue(true);
        }
    }

    /**
     * Check if the lobby full checker works.
     */
    @Test
    public void LobbyControllerTestFull(){
        LobbyController l=new LobbyController();
        String s="Giorgio";
        try {
            l.handle_setLobby(new LobbySetMessage(s, 5));
            fail();
        } catch (IndexOutOfBoundsException e){
            assertTrue(true);
        }
        l.handle_setLobby(new LobbySetMessage(s, 4));
        assertFalse(l.isFull());
        String p="Paolo";
        try {
            l.handle_setLobby(new LobbySetMessage(p, 4));
            fail();
        } catch (LobbyException e) {
            assertTrue(true);
        }
        l.handle_addInLobby(new LobbyJoinMessage(p));
        assertEquals(l.getPlayers().size(), 2);
        try {
            l.handle_addInLobby(new LobbyJoinMessage(p));
            fail();
        } catch (PlayerAlreadyExistsException e){
            assertTrue(true);
        }
        String g="Giorgia";
        String a="Aurora";
        l.handle_addInLobby(new LobbyJoinMessage(g));
        assertEquals(l.getPlayers().size(), 3);
        l.handle_addInLobby(new LobbyJoinMessage(a));
        assertEquals(l.getPlayers().size(), 4);
        try{
            l.handle_addInLobby(new LobbyJoinMessage(p));
            fail();
        } catch (PlayerAlreadyExistsException e){
            assertTrue(true);
        }
        String n="Nicola";
        try {
            l.handle_addInLobby(new LobbyJoinMessage(n));
            fail();
        } catch (LobbyException e) {
            assertTrue(true);
        }
        assertTrue(l.isFull());
        assertEquals(l.getPlayers().size(), 4);
    }
}
