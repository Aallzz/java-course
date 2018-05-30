package ru.ifmo.rain.Petrovski.person;

import net.java.quickcheck.collection.Pair;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.Map;

public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public static void main(final String... args) {
        if (args == null || args.length != 1) {
            System.out.println("Usage: server <port>");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NullPointerException | NumberFormatException e) {
            System.out.println("Expected number as port");
            return;
        }

        try {
            new Server(port).start();
        } catch (final RemoteException e) {
            System.out.println("Cannot export object: " + e.getMessage());
            e.printStackTrace();
        } catch (final MalformedURLException e) {
            System.out.println("Malformed URL");
        }
        System.out.println("Server started");
    }

    public void start() throws RemoteException, MalformedURLException {
        Bank bank = new TronBank(port);
        UnicastRemoteObject.exportObject(bank, port);
        Naming.rebind("//localhost/bank", bank);
    }

    public void close() throws RemoteException, NotBoundException, MalformedURLException {
        Naming.unbind("//localhost/bank");
        System.out.println("Server closed");
    }
}
