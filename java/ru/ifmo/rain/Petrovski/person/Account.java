package ru.ifmo.rain.Petrovski.person;

import java.rmi.*;

public interface Account extends Remote {
    String getId() throws RemoteException;
    int getAmount() throws RemoteException;
    void setAmount(int amount) throws RemoteException;
}