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
        assertEquals(Dialer.getRecallNumber(operator, "380666051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "38(066)605-15-15"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "0666051515"), expected);
        assertEquals(Dialer.getRecallNumber(operator, "066 605 15 15"), expected);
    }
}
