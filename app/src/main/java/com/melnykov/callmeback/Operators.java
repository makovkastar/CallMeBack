package com.melnykov.callmeback;

import com.melnykov.callmeback.model.Country;
import com.melnykov.callmeback.model.Operator;

import java.util.Arrays;
import java.util.List;

public class Operators {

    private static final List<Operator> mList = Arrays.asList(
        // Ukraine
        new Operator(0, R.string.kyivstar, R.drawable.kyivstar, Country.UKRAINE, "*130*38%s#"),
        new Operator(1, R.string.mts_ua, R.drawable.mts, Country.UKRAINE, "*104*%s#"),
        new Operator(2, R.string.beeline_ua, R.drawable.beeline, Country.UKRAINE, "*130*38%s#"),
        new Operator(3, R.string.djuice, R.drawable.djuice, Country.UKRAINE, "*130*38%s#"),
        new Operator(4, R.string.life_ua, R.drawable.life, Country.UKRAINE, "*123*3*38%s#"),
        new Operator(5, R.string.utel, R.drawable.utel, Country.UKRAINE, "*110*02*%s#"),
        // Russia
        new Operator(100, R.string.mts_ru, R.drawable.mts, Country.RUSSIA, "*110*8%s#"),
        new Operator(101, R.string.beeline_ru, R.drawable.beeline, Country.RUSSIA, "*144*%s#"),
        new Operator(102, R.string.megafon, R.drawable.megafon, Country.RUSSIA, "*144*8%s#"),
        new Operator(103, R.string.rostelecom, R.drawable.rostelecom, Country.RUSSIA, "*123*8%s#"),
        new Operator(104, R.string.tele2_ru, R.drawable.tele2, Country.RUSSIA, "*118*8%s#"),
        new Operator(105, R.string.motiv, R.drawable.motiv, Country.RUSSIA, "*105*8%s#"),
        new Operator(106, R.string.smarts, R.drawable.smarts, Country.RUSSIA, "*134*%s#"),
        new Operator(107, R.string.nss, R.drawable.nss, Country.RUSSIA, "*135*%s#"),
        new Operator(108, R.string.bwc, R.drawable.bwc, Country.RUSSIA, "*141*%s#"),
        new Operator(109, R.string.akoc, R.drawable.akoc, Country.RUSSIA, "*123*%s#"),
        new Operator(110, R.string.etk, R.drawable.etk, Country.RUSSIA, "*102*50*%s#"),
        new Operator(111, R.string.justtotalk, R.drawable.justtotalk, Country.RUSSIA, "*168*%s#"),
        // Belarus
        new Operator(200, R.string.velcom, R.drawable.velcom, Country.BELARUS, "*131*%s#"),
        new Operator(201, R.string.mts_by, R.drawable.mts, Country.BELARUS, "*120*%s#"),
        new Operator(202, R.string.life_by, R.drawable.life, Country.BELARUS, "*120*2*%s#"),
        // Moldova
        new Operator(300, R.string.idc, R.drawable.idc, Country.MOLDOVA, "887#%s"),
        new Operator(301, R.string.mold_cell, R.drawable.mold_cell, Country.MOLDOVA, "*111*7%s#"),
        // Kazakhstan
        new Operator(400, R.string.kcell, R.drawable.kcell, Country.KAZAKHSTAN, "*130*8%s#"),
        new Operator(401, R.string.activ_kz, R.drawable.activ, Country.KAZAKHSTAN, "*130*8%s#"),
        new Operator(402, R.string.beeline_kz, R.drawable.beeline, Country.KAZAKHSTAN, "*144*8%s#"),
        new Operator(403, R.string.tele2_kz, R.drawable.tele2, Country.KAZAKHSTAN, "*144*8%s#")
    );

    private Operators() {
        // Prevent instantiation
    }

    public static List<Operator> list() {
        return mList;
    }

    public static Operator getById(int id) {
        for (Operator operator : mList) {
            if (operator.getId() == id) return operator;
        }
        throw new IllegalArgumentException("No operator with id = " + id);
    }

    public static int getPosition(int operatorId) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getId() == operatorId) return i;
        }
        throw new IllegalArgumentException("No operator with id = " + operatorId);
    }
}