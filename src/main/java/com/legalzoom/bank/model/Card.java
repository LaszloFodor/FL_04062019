package com.legalzoom.bank.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Card  implements Comparable<Card> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;

    private String nameOfBank;

    private String card;

    @JsonFormat(pattern = "MMM-yyyy")
    private Date expiryDate;

    public Card() {
    }

    public Card(String card, String nameOfBank, Date expiryDate) {
        this.card = card;
        this.nameOfBank = nameOfBank;
        this.expiryDate = expiryDate;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getNameOfBank() {
        return nameOfBank;
    }

    public void setNameOfBank(String nameOfBank) {
        this.nameOfBank = nameOfBank;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public int compareTo(Card o) {
        return this.getExpiryDate().compareTo(o.expiryDate);
    }
}
