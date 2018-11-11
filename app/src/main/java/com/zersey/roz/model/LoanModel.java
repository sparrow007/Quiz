package com.zersey.roz.model;



import java.util.Date;


public class LoanModel {


    public int id;
    private int amount;
    private Date date;
    private float rate;
    private int duration;
    private float InterestAccrued;
    private int durationType;
    private float emi;

    public LoanModel() {

    }

    public float getEmi() {
        return emi;
    }

    public void setEmi(float emi) {
        this.emi = emi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getInterestAccrued() {
        return InterestAccrued;
    }

    public void setInterestAccrued(float interestAccrued) {
        InterestAccrued = interestAccrued;
    }

    public int getDurationType() {
        return durationType;
    }

    public void setDurationType(int durationType) {
        this.durationType = durationType;
    }
}
