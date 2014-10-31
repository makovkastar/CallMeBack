package com.melnykov.callmeback;

public class Operator {
    final int id;
    final int nameResId;
    final int logoResId;
    final String recallPattern;

    public Operator(int id, int nameResId, int logoResId, String recallPattern) {
        this.id = id;
        this.nameResId = nameResId;
        this.logoResId = logoResId;
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

    public String getRecallPattern() {
        return recallPattern;
    }
}