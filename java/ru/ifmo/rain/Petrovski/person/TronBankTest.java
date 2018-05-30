package ru.ifmo.rain.Petrovski.person;

import net.java.quickcheck.collection.Pair;
import org.junit.Test;
import static org.junit.Assert.*;

import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

public class TronBankTest {

    private final Map<String, Pair<String, String>> users = new HashMap<>();
    private final Map<String, Map<String, Account>> accounts = new HashMap<>();

    @Test
    public void test() throws Exception{
        LocateRegistry.createRegistry(1099);
        Server server = new Server(1099);
        server.start();
        Client client = new Client();
        Client client2 = new Client();
        assertEquals(1, client.run("1", "2", "3", "4", 1));
        assertEquals(3, client2.run("1", "2", "3", "4", 2, 1));
        assertEquals(7, client.run("1", "2", "3", "4", 4));
        assertEquals(15, client2.run("1", "2", "3", "4", 8, 1));
    }

}
