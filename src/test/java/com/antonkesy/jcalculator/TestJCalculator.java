package com.antonkesy.jcalculator;

import com.antonkesy.jcalculator.tokenizer.token.Token;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestJCalculator {

    Token testString(String input) {
        try {
            return JCalculator.calculate(input);
        } catch (IOException ignore) {
        }
        return null;
    }

    @Test
    void testEmptyString() {
        assertNull(testString(""));
        assertNull(testString("1"));
    }

}
