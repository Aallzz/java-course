package ru.ifmo.rain.Petrovski.person;

import java.rmi.RemoteException;

public class TronBankAccount implements Account {

    private final Person person;
    private final String accountNumber;
    private int amount;

    public TronBankAccount(Person person, String subId) throws RemoteException {
        this.person = person;
        this.accountNumber = person.getPassportId() + ":" + subId;
        amount = 0;
    }

    @Override
    public int hashCode() {
        return (person.hashCode() << 9) ^ accountNumber.hashCode();
    }

    public String getId() {
        return accountNumber;
    }

    public Person getPerson() throws RemoteException{
        return person;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
