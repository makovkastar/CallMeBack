package com.melnykov.callmeback.model;

public class Operator {

    private final int id;
    private final int nameResId;
    private final int logoResId;
    private final Country country;
    private final String recallPattern;

    public Operator(int id, int nameResId, int logoResId, Country country, String recallPattern) {
        this.id = id;
        this.nameResId = nameResId;
        this.logoResId = logoResId;
        this.country = country;
        this.recallPattern = recallPattern;
    }

    public int getId() {
        return id;
    }

    public int getNameResId() {
        return nameResId;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public Country getCountry() {
        return country;
    }

    public String getRecallPattern() {
        return recallPattern;
    }
}