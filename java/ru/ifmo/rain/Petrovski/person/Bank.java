package ru.ifmo.rain.Petrovski.person;

import net.java.quickcheck.collection.Pair;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Bank extends Remote {
    Account createAccount(Person person, String subId) throws RemoteException;
    Account getAccount(Person person, String subId) throws RemoteException;
    void updateUserBaseWith(String name, String surname, String passportId) throws RemoteException ;
    Person getRemoteUserByPassport(String passportId) throws RemoteException;
    Person getLocalUserByPassport(String passportId) throws RemoteException;
}