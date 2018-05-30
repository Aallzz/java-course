package ru.ifmo.rain.Petrovski.person;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;


public class LocalPerson implements Person, Serializable {
    private final String name;
    private final String surname;
    private final String passportId;

    public LocalPerson(String name, String surname, String passportId) {
        this.name = name;
        this.surname = surname;
        this.passportId = passportId;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    @Override
    public String getPassportId() {
        return passportId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof LocalPerson))
            return false;
        return hashCode() == obj.hashCode() && toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return (name+surname+passportId).hashCode();
    }
}