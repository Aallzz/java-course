package ru.ifmo.rain.Petrovski.person;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;


public class LocalPerson implements Person, Serializable {
    private final String name;
    private final String surname;
    private final String passportId;
//    private final Map<String, Account> accounts;

    public LocalPerson(String name, String surname, String passportId/*, Map<String, Account> accounts*/) {
        this.name = name;
        this.surname = surname;
        this.passportId = passportId;
//        this.accounts = accounts;
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
/*
    public Map<String, Account> getAccounts() {
        return accounts;
    }
*/
    @Override
    public int hashCode() {
        return (name+surname+passportId).hashCode();
    }
}