package ru.ifmo.rain.Petrovski.person;

import net.java.quickcheck.collection.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class TronBank implements Bank {

    private final Map<String, Pair<String, String>> users = new HashMap<>();
    private final Map<String, Map<String, Account>> accounts = new HashMap<>();
    private final int port;

    public TronBank(final int port) {
        this.port = port;
    }

    public Account createAccount(Person person, String subId) throws RemoteException {
        Account account = new TronBankAccount(person, subId);
        accounts.putIfAbsent(person.getPassportId(), new HashMap<>());
        accounts.get(person.getPassportId()).putIfAbsent(subId, account);
        UnicastRemoteObject.exportObject(account, port);
        return account;
    }

    public Account getAccount(Person person, String subId) throws RemoteException {
        Map<String, Account> stringAccountMap = accounts.get(person.getPassportId());
        return stringAccountMap == null ? null : stringAccountMap.get(subId);
    }

    public void updateUserBaseWith(String name, String surname, String passportId) throws RemoteException {
        users.putIfAbsent(passportId, new Pair<>(name, surname));
    }

    public Person getRemoteUserByPassport(String passportId) throws RemoteException {
        return new RemotePerson(users.get(passportId).getFirst(), users.get(passportId).getSecond(), passportId);
    }

    public Person getLocalUserByPassport(String passportId) throws RemoteException {
        return new LocalPerson(users.get(passportId).getFirst(), users.get(passportId).getSecond(), passportId);
    }

}
