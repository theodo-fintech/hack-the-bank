package com.sipios.bank.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Transfer {
    private long id;
    private Double amount;
    private String targetIban;
    private String code;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTargetIban() {
        return targetIban;
    }

    public void setTargetIban(String targetIban) {
        this.targetIban = targetIban;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", amount=" + amount +
                ", targetIban='" + targetIban + '\'' +
                '}';
    }
}
