package com.melnykov.callmeback.model;

public class Operator {

    private final int nameResId;
    private final int logoResId;
    private final String recallPattern;

    public Operator(int nameResId, int logoResId, String recallPattern) {
        this.nameResId = nameResId;
        this.logoResId = logoResId;
        this.recallPattern = recallPattern;
    }

    public int getNameResId() {
        return nameResId;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public String getRecallPattern() {
        return recallPattern;
    }
}