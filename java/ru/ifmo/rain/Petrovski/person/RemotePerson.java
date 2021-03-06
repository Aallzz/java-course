package ru.ifmo.rain.Petrovski.person;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemotePerson extends UnicastRemoteObject implements Remote, Person {
    private final String name;
    private final String surname;
    private final String passportId;

    public RemotePerson(String name, String surname, String passportId) throws RemoteException {
        this.name = name;
        this.surname = surname;
        this.passportId = passportId;
    }

    @Override
    public String getSurname() throws RemoteException {
        return surname;
    }

    @Override
    public String getPassportId() throws RemoteException {
        return passportId;
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof RemoteException))
            return false;
        return hashCode() == obj.hashCode() && toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return (name+surname+passportId).hashCode();
    }
}