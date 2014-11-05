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
    }

    public void testBeelineRu() {
        Operator operator = Operators.getById(101);
        testRuNumbersValid(operator);
    }

    public void testMegafon() {
        Operator operator = Operators.getById(102);
        testRuNumbersValid(operator);
    }

    public void testRostelecom() {
        Operator operator = Operators.getById(103);
        testRuNumbersValid(operator);
    }

    public void testTele2() {
        Operator operator = Operators.getById(104);
        testRuNumbersValid(operator);
    }

    public void testMotiv() {
        Operator operator = Operators.getById(105);
        testRuNumbersValid(operator);
    }

    public void testSmarts() {
        Operator operator = Operators.getById(106);
        testRuNumbersValid(operator);
    }

    public void testHcc() {
        Operator operator = Operators.getById(107);
        testRuNumbersValid(operator);
    }

    public void testBwc() {
        Operator operator = Operators.getById(108);
        testRuNumbersValid(operator);
    }

    public void testAkoc() {
        Operator operator = Operators.getById(109);
        testRuNumbersValid(operator);
    }

    public void testEtk() {
        Operator operator = Operators.getById(110);
        testRuNumbersValid(operator);
    }

    public void testJustToTalk() {
        Operator operator = Operators.getById(111);
        testRuNumbersValid(operator);
    }


    public void testVelcom() {
        Operator operator = Operators.getById(200);
        testByNumbersValid(operator);
    }

    public void testMtsBy() {
        Operator operator = Operators.getById(201);
        testByNumbersValid(operator);
    }

    public void testLifeBy() {
        Operator operator = Operators.getById(202);
        testByNumbersValid(operator);
    }

    public void testIdc() {
        Operator operator = Operators.getById(300);
        testMdNumbersValid(operator);
    }

    public void testMoldCell() {
        Operator operator = Operators.getById(301);
        testMdNumbersValid(operator);
    }

    public void testKCell() {
        Operator operator = Operators.getById(400);
        testKzNumbersValid(operator);
    }

    public void testActivKz() {
        Operator operator = Operators.getById(401);
        testKzNumbersValid(operator);
    }

    public void testBeelineKz() {
        Operator operator = Operators.getById(402);
        testKzNumbersValid(operator);
    }

    public void testTele2Kz() {
        Operator operator = Operators.getById(403);
        testKzNumbersValid(operator);
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

    private void testUaRecallNumber(Operator operator, String expected) {
        assertEquals(Dialer.getRecallNumber(operator, "+380666051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "+380 66 605 15 15"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "380666051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "38(066)605-15-15"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "0666051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "066 605 15 15"), expected);
    }

    private void testRuNumbersValid(Operator operator) {
        assertTrue(Dialer.isNumberValid(operator, "+79806051515"));
        assertTrue(Dialer.isNumberValid(operator, "+7 980 605 15 15"));
        assertTrue(Dialer.isNumberValid(operator, "79806051515"));
        assertTrue(Dialer.isNumberValid(operator, "7(980)6051515"));
        assertTrue(Dialer.isNumberValid(operator, "9806051515"));
        assertTrue(Dialer.isNumberValid(operator, "980 605 15 15"));

        assertFalse(Dialer.isNumberValid(operator, "6051515"));
        assertFalse(Dialer.isNumberValid(operator, "+89806051515"));
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

    private void testMdNumbersValid(Operator operator) {
        assertTrue(Dialer.isNumberValid(operator, "+373060605151"));
        assertTrue(Dialer.isNumberValid(operator, "+373 060 605151"));
        assertTrue(Dialer.isNumberValid(operator, "373060605151"));
        assertTrue(Dialer.isNumberValid(operator, "373(060)605151"));
        assertTrue(Dialer.isNumberValid(operator, "060605151"));
        assertTrue(Dialer.isNumberValid(operator, "0606 05 151"));
        assertTrue(Dialer.isNumberValid(operator, "606 05151"));

        assertFalse(Dialer.isNumberValid(operator, "605151"));
        assertFalse(Dialer.isNumberValid(operator, "+473060605151"));
        assertFalse(Dialer.isNumberValid(operator, "160605151"));
    }

    private void testKzNumbersValid(Operator operator) {
        assertTrue(Dialer.isNumberValid(operator, "+77016051515"));
        assertTrue(Dialer.isNumberValid(operator, "+7 701 605 15 15"));
        assertTrue(Dialer.isNumberValid(operator, "77016051515"));
        assertTrue(Dialer.isNumberValid(operator, "7(701)6051515"));
        assertTrue(Dialer.isNumberValid(operator, "7016051515"));
        assertTrue(Dialer.isNumberValid(operator, "701 605 15 15"));

        assertFalse(Dialer.isNumberValid(operator, "6051515"));
        assertFalse(Dialer.isNumberValid(operator, "+87016051515"));
        assertFalse(Dialer.isNumberValid(operator, "88806051515"));
    }
}