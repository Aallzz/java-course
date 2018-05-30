package ru.ifmo.rain.Petrovski.person;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {

    private final Bank bank;

    public Client() throws RemoteException, NotBoundException, MalformedURLException {
        bank = (Bank) Naming.lookup("//localhost/bank");
    }

    public static void main(final String... args) throws RemoteException {
        if (args == null || args.length != 5) {
            System.out.println("Usage: Client <Name> <Surname> <passportId> <subId> <delta balance> ");
            return;
        }
        for (int i = 0; i < 5; ++i) {
            if (args[i] == null) {
                System.out.println("Expected non null value as argument");
                return;
            }
        }
        int delta;
        try {
            delta = Integer.parseInt(args[4]);
        } catch (NumberFormatException e) {
            System.out.println("Expected number as the fifth argument (delta balance)");
            return;
        }

        try {
            System.out.println("Money on your account: " + new Client().run(args[0], args[1], args[2], args[3], delta));
        } catch (final NotBoundException e) {
            System.out.println("Bank is not bound");
        } catch (final MalformedURLException e) {
            System.out.println("Bank URL is invalid");
        }
    }

    public int run(String name, String surname, String passportId, String subId, int delta) throws RemoteException, MalformedURLException, NotBoundException {
        return run(name, surname, passportId, subId, delta, 0);
    }

    public int run(String name, String surname, String passportId, String subId, int delta, int type) throws RemoteException, MalformedURLException, NotBoundException {
        bank.updateUserBaseWith(name, surname, passportId);
        Person user = type == 0
                ? bank.getLocalUserByPassport(passportId)
                : bank.getRemoteUserByPassport(passportId);


        Account account = bank.getAccount(user, subId);
        if (account == null) {
            account = bank.createAccount(user, subId);
        }
        account.setAmount(account.getAmount() + delta);
        return account.getAmount();
    }
}