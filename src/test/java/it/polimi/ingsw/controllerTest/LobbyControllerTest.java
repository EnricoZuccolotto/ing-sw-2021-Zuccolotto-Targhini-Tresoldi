package it.polimi.ingsw.controllerTest;

import it.polimi.ingsw.controller.LobbyController;
import it.polimi.ingsw.exception.controller.LobbyisFullException;
import it.polimi.ingsw.exception.controller.PlayerAlreadyExistsException;
import it.polimi.ingsw.network.messages.LobbyJoinMessage;
import it.polimi.ingsw.network.messages.LobbySetMessage;
import it.polimi.ingsw.exception.controller.LobbyAlreadyCreatedException;
import org.junit.Test;
import static org.junit.Assert.*;

public class LobbyControllerTest {
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
        try{
            l.handle_setLobby(new LobbySetMessage(p, 3));
            fail();
        } catch (LobbyAlreadyCreatedException e){
            assertTrue(true);
        }
        try {
            l.handle_addInLobby(new LobbyJoinMessage(p));
            fail();
        } catch(LobbyisFullException e){
            assertTrue(true);
        }
    }
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
        } catch (LobbyAlreadyCreatedException e){
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
        try{
            l.handle_addInLobby(new LobbyJoinMessage(n));
            fail();
        } catch (LobbyisFullException e){
            assertTrue(true);
        }
        assertTrue(l.isFull());
        assertEquals(l.getPlayers().size(), 4);
    }
}
