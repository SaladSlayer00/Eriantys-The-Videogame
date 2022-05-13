package it.polimi.ingsw.controller;
import it.polimi.ingsw.client.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the static methods of the {@link ClientController} which checks the server info.
 */

public class ClientControllerTest {
    @BeforeAll
    public void setUp(){}
    @AfterAll
    public void tearDown(){}
    @Test
    public void isValidIpAddress(){
        assertTrue(ClientController.isValidIpAddress("192.168.56.1"));
        assertFalse(ClientController.isValidIpAddress("268.654.321.123"));
    }
    @Test
    public void isValidPort() {
        assertTrue(ClientController.isValidPort("3333"));
        assertTrue(ClientController.isValidPort("16847"));

        assertFalse(ClientController.isValidPort("-1"));
        assertFalse(ClientController.isValidPort("65536"));
        assertFalse(ClientController.isValidPort("string"));
    }

}
