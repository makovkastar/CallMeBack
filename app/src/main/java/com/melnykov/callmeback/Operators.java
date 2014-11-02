package com.melnykov.callmeback;

import com.melnykov.callmeback.model.Operator;

import java.util.Arrays;
import java.util.List;

public class Operators {

    private static final List<Operator> mList = Arrays.asList(
        new Operator(R.string.kyivstar, R.drawable.kyivstar, "*130*%s#"),
        new Operator(R.string.mts_ua, R.drawable.mts, "*104*%s#"),
        new Operator(R.string.beeline_ua, R.drawable.beeline, "*130*%s#"),
        new Operator(R.string.djuice, R.drawable.djuice, "*130*%s#"),
        new Operator(R.string.life_ua, R.drawable.life_ua, " *124*3*%s#"),
        new Operator(R.string.utel, R.drawable.utel, "*110*02*%s#"),
        new Operator(R.string.mts_ru, R.drawable.mts, "*110*%s#"),
        new Operator(R.string.beeline_ru, R.drawable.beeline, "*144*%s#"),
        new Operator(R.string.megafon, R.drawable.megafon, "*144*%s#"),
        new Operator(R.string.rostelecom, R.drawable.rostelecom, "*123*%s#"),
        new Operator(R.string.tele2_ru, R.drawable.tele2_ru, "*118*%s#"),
        new Operator(R.string.motiv, R.drawable.motiv, "*105*%s#"),
        new Operator(R.string.smarts, R.drawable.smarts, "*134*%s#"),
        new Operator(R.string.nss, R.drawable.nss, "*135*%s#"),
        new Operator(R.string.bwc, R.drawable.bwc, "*141*%s#"),
        new Operator(R.string.akoc, R.drawable.akoc, "*123*%s#"),
        new Operator(R.string.justtotalk, R.drawable.justtotalk, "*168*%s#")
    );

    private Operators() {
    } // Prevent instantiation

    public static List<Operator> list() {
        return mList;
    }
}