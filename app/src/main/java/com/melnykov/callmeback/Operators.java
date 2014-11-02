package com.melnykov.callmeback;

import com.melnykov.callmeback.model.Operator;

import java.util.Arrays;
import java.util.List;

public class Operators {

    private static final List<Operator> mList = Arrays.asList(
        new Operator(R.string.kyivstar, R.drawable.kyivstar, "*130*%s#"),
        new Operator(R.string.mts_ua, R.drawable.mts_ua, "*104*%s#"),
        new Operator(R.string.beeline_ua, R.drawable.beeline_ua, "*130*%s#"),
        new Operator(R.string.djuice, R.drawable.djuice, "*130*%s#"),
        new Operator(R.string.life_ua, R.drawable.life_ua, " *124*3*%s#"),
        new Operator(R.string.utel, R.drawable.utel, "*110*02*%s#")
    );

    private Operators() {
    } // Prevent instantiation

    public static List<Operator> list() {
        return mList;
    }
}