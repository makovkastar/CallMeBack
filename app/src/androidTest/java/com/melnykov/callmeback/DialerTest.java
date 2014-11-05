package com.melnykov.callmeback;

import com.melnykov.callmeback.model.Operator;

import junit.framework.TestCase;

public class DialerTest extends TestCase {

    public void testKyivstar() {
        Operator operator = Operators.getById(0);
        testUaNumbersValid(operator);
        testUaRecallNumber(operator, "*130*380666051515#");
    }

    public void testMtsUa() {
        Operator operator = Operators.getById(1);
        testUaNumbersValid(operator);
        testUaRecallNumber(operator, "*104*0666051515#");
    }

    public void testBeelineUa() {
        Operator operator = Operators.getById(2);
        testUaNumbersValid(operator);
        testUaRecallNumber(operator, "*130*380666051515#");
    }

    public void testDjuice() {
        Operator operator = Operators.getById(3);
        testUaNumbersValid(operator);
        testUaRecallNumber(operator, "*130*380666051515#");
    }

    public void testLifeUa() {
        Operator operator = Operators.getById(4);
        testUaNumbersValid(operator);
        testUaRecallNumber(operator, "*123*3*380666051515#");
    }

    public void testUtel() {
        Operator operator = Operators.getById(5);
        testUaNumbersValid(operator);
        testUaRecallNumber(operator, "*110*0666051515#");
    }

    public void testMtsRu() {
        Operator operator = Operators.getById(100);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*110*89806051515#");
    }

    public void testBeelineRu() {
        Operator operator = Operators.getById(101);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*144*9806051515#");
    }

    public void testMegafon() {
        Operator operator = Operators.getById(102);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*144*89806051515#");
    }

    public void testRostelecom() {
        Operator operator = Operators.getById(103);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*123*89806051515#");
    }

    public void testTele2() {
        Operator operator = Operators.getById(104);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*118*89806051515#");
    }

    public void testMotiv() {
        Operator operator = Operators.getById(105);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*105*89806051515#");
    }

    public void testSmarts() {
        Operator operator = Operators.getById(106);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*134*9806051515#");
    }

    public void testHcc() {
        Operator operator = Operators.getById(107);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*135*9806051515#");
    }

    public void testBwc() {
        Operator operator = Operators.getById(108);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*141*9806051515#");
    }

    public void testAkoc() {
        Operator operator = Operators.getById(109);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*123*9806051515#");
    }

    public void testEtk() {
        Operator operator = Operators.getById(110);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*102*50*9806051515#");
    }

    public void testJustToTalk() {
        Operator operator = Operators.getById(111);
        testRuNumbersValid(operator);
        testRuRecallNumber(operator, "*168*89806051515#");
    }

    public void testVelcom() {
        Operator operator = Operators.getById(200);
        testByNumbersValid(operator);
        testByRecallNumber(operator, "*131*256051515#");
    }

    public void testMtsBy() {
        Operator operator = Operators.getById(201);
        testByNumbersValid(operator);
        testByRecallNumber(operator, "*120*375256051515#");
    }

    public void testLifeBy() {
        Operator operator = Operators.getById(202);
        testByNumbersValid(operator);
        testByRecallNumber(operator, "*120*2*375256051515#");
    }

    public void testKCell() {
        Operator operator = Operators.getById(400);
        testKzNumbersValid(operator);
        testKzRecallNumber(operator, "*130*87016051515#");
    }

    public void testActivKz() {
        Operator operator = Operators.getById(401);
        testKzNumbersValid(operator);
        testKzRecallNumber(operator, "*130*87016051515#");
    }

    public void testBeelineKz() {
        Operator operator = Operators.getById(402);
        testKzNumbersValid(operator);
        testKzRecallNumber(operator, "*144*87016051515#");
    }

    public void testTele2Kz() {
        Operator operator = Operators.getById(403);
        testKzNumbersValid(operator);
        testKzRecallNumber(operator, "*144*87016051515#");
    }

    private void testUaNumbersValid(Operator operator) {
        assertTrue(Dialer.isNumberValid(operator, "+380666051515"));
        assertTrue(Dialer.isNumberValid(operator, "380666051515"));
        assertTrue(Dialer.isNumberValid(operator, "38(066)605-15-15"));
        assertTrue(Dialer.isNumberValid(operator, "0666051515"));
        assertTrue(Dialer.isNumberValid(operator, "066 605 15 15"));

        assertFalse(Dialer.isNumberValid(operator, "6051515"));
        assertFalse(Dialer.isNumberValid(operator, "06660515151513"));
        assertFalse(Dialer.isNumberValid(operator, "480666051515"));
    }

    private void testRuNumbersValid(Operator operator) {
        assertTrue(Dialer.isNumberValid(operator, "+79806051515"));
        assertTrue(Dialer.isNumberValid(operator, "+7 980 605 15 15"));
        assertTrue(Dialer.isNumberValid(operator, "79806051515"));
        assertTrue(Dialer.isNumberValid(operator, "7(980)6051515"));
        assertTrue(Dialer.isNumberValid(operator, "9806051515"));
        assertTrue(Dialer.isNumberValid(operator, "980 605 15 15"));

        assertFalse(Dialer.isNumberValid(operator, "6051515"));
        assertFalse(Dialer.isNumberValid(operator, "78806051515"));
    }

    private void testByNumbersValid(Operator operator) {
        assertTrue(Dialer.isNumberValid(operator, "+375256051515"));
        assertTrue(Dialer.isNumberValid(operator, "+375 25 605 15 15"));
        assertTrue(Dialer.isNumberValid(operator, "375256051515"));
        assertTrue(Dialer.isNumberValid(operator, "375(25)6051515"));
        assertTrue(Dialer.isNumberValid(operator, "256051515"));
        assertTrue(Dialer.isNumberValid(operator, "(25) 605 15 15"));

        assertFalse(Dialer.isNumberValid(operator, "6051515"));
        assertFalse(Dialer.isNumberValid(operator, "+475 25 605 15 15"));
        assertFalse(Dialer.isNumberValid(operator, "4375256051515"));
    }

    private void testKzNumbersValid(Operator operator) {
        assertTrue(Dialer.isNumberValid(operator, "+77016051515"));
        assertTrue(Dialer.isNumberValid(operator, "+7 701 605 15 15"));
        assertTrue(Dialer.isNumberValid(operator, "77016051515"));
        assertTrue(Dialer.isNumberValid(operator, "7(701)6051515"));
        assertTrue(Dialer.isNumberValid(operator, "7016051515"));
        assertTrue(Dialer.isNumberValid(operator, "701 605 15 15"));

        assertFalse(Dialer.isNumberValid(operator, "6051515"));
        assertFalse(Dialer.isNumberValid(operator, "88806051515"));
    }

    private void testUaRecallNumber(Operator operator, String expected) {
        assertEquals(Dialer.getRecallNumber(operator, "+380666051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "+380 66 605 15 15"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "380666051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "38(066)605-15-15"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "0666051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "066 605 15 15"), expected);
    }

    private void testRuRecallNumber(Operator operator, String expected) {
        assertEquals(Dialer.getRecallNumber(operator, "+79806051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "+7 980 605 15 15"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "79806051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "7(980)6051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "9806051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "980 605 15 15"), expected);
    }

    private void testByRecallNumber(Operator operator, String expected) {
        assertEquals(Dialer.getRecallNumber(operator, "+375256051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "+375 25 605 1515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "375256051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "375(25)6051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "256051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "25 605 15 15"), expected);
    }

    private void testKzRecallNumber(Operator operator, String expected) {
        assertEquals(Dialer.getRecallNumber(operator, "+77016051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "+7 701 605 15 15"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "77016051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "7(701)6051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "7016051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "701 605 15 15"), expected);
    }
}