package model;

import java.time.LocalDateTime;

public class Operations {

    private int id;
    private double sum;
    private String codeCurrency;
    private int idAccount;
    private LocalDateTime localDateTime;
    private TypeOperation typeOperation;

    public Operations(int id, double sum, String codeCurrency, int idAccount, TypeOperation typeOperation) {
        this.id = id;
        this.sum = sum;
        this.codeCurrency = codeCurrency;
        this.idAccount = idAccount;
        this.typeOperation = typeOperation;
        this.localDateTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getCodeCurrency() {
        return codeCurrency;
    }

    public void setCodeCurrency(String codeCurrency) {
        this.codeCurrency = codeCurrency;
    }

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public TypeOperation getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(TypeOperation typeOperation) {
        this.typeOperation = typeOperation;
    }

    public enum TypeOperation {
        DEPOSIT, WITHDRAWAL, EXCHANGE, CLOSE_ACCOUNT,TRANSFER_OUT,TRANSFER_IN;
    }

    @Override
    public String toString() {
        return "Operations{" +
                "id=" + id +
                ", sum=" + sum +
                ", codeCurrency='" + codeCurrency + '\'' +
                ", idAccount=" + idAccount +
                ", localDateTime=" + localDateTime +
                ", typeOperation=" + typeOperation +
                '}';
    }
}
