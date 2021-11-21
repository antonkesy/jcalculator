package com.antonkesy.jcalculator.token;

import com.antonkesy.jcalculator.token.exception.UnknownTokenException;
import com.antonkesy.jcalculator.token.operator.OperatorToken;
import com.antonkesy.jcalculator.token.operator.OperatorType;
import com.antonkesy.jcalculator.token.value.constant.ConstantToken;
import com.antonkesy.jcalculator.token.value.constant.ConstantType;
import com.antonkesy.jcalculator.token.value.literal.LiteralToken;
import com.antonkesy.jcalculator.tokenizer.Tokenizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestTokenizer {

    void testGetTokenOfTypeCase(TypeRepresentation expected) {
        assertEquals(expected.createToken(), Tokenizer.getTokenOfType(expected.getTypeRepresentation()));
    }

    @Test
    void testGetTokenOfType() {
        for (TypeRepresentation[] types : Tokenizer.getAllTypes()) {
            for (TypeRepresentation type : types) {
                testGetTokenOfTypeCase(type);
            }
        }
    }

    @Test
    void testGetOptionsRegex() {
        // \Q*\E -> * not read as regex
        assertEquals("(\\Qa\\E)", Tokenizer.getOptionRegex("a"));
        assertEquals("(\\Qa\\E|\\Qb\\E)", Tokenizer.getOptionRegex("a b"));
        assertEquals("(\\Qa\\E|\\Qab\\E)", Tokenizer.getOptionRegex("a ab"));
    }

    void testGetTokenFromStringCase(TypeRepresentation type) {
        try {
            assertEquals(type.createToken(), Tokenizer.getTokenFromString(type.getTypeRepresentation()));
        } catch (UnknownTokenException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testGetTokenFromStringTypes() {
        for (TypeRepresentation[] types : Tokenizer.getAllTypes()) {
            for (TypeRepresentation type : types) {
                testGetTokenFromStringCase(type);
            }
        }
    }

    @Test
    void testGetTokenFromStringLiteral() {
        try {
            assertEquals(new LiteralToken(42), Tokenizer.getTokenFromString("42"));
            assertEquals(new LiteralToken(-42), Tokenizer.getTokenFromString("-42"));
            assertEquals(new LiteralToken(123), Tokenizer.getTokenFromString("0123"));

            assertNull(Tokenizer.getLiteralToken("+"));
            assertNull(Tokenizer.getLiteralToken("a"));
        } catch (UnknownTokenException e) {
            e.printStackTrace();
            fail();
        }
    }

    void testTokenizeCase(ArrayList<Token> expected, String input) {
        try {
            assertEquals(expected, Tokenizer.tokenize(input));
        } catch (UnknownTokenException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testTokenizeSingleTokenList() {
        ArrayList<Token> singleTokenList = new ArrayList<>();
        singleTokenList.add(new LiteralToken(42));
        testTokenizeCase(singleTokenList, "42");
        singleTokenList.clear();

        singleTokenList.add(new ConstantToken(ConstantType.PI));
        testTokenizeCase(singleTokenList, ConstantType.PI.getTypeRepresentation());
        singleTokenList.clear();

        singleTokenList.add(new OperatorToken(OperatorType.ADD));
        testTokenizeCase(singleTokenList, "+");
        singleTokenList.clear();

        assertThrows(UnknownTokenException.class, () -> Tokenizer.tokenize("un"));
        assertThrows(UnknownTokenException.class, () -> Tokenizer.tokenize(""));
    }

    @Test
    void testTokenizeMultiple() {
        ArrayList<Token> multiTokenList = new ArrayList<>();
        multiTokenList.add(new LiteralToken(42));
        multiTokenList.add(new OperatorToken(OperatorType.ADD));
        multiTokenList.add(new ConstantToken(ConstantType.PI));

        testTokenizeCase(multiTokenList, "42+pi");
        testTokenizeCase(multiTokenList, "42 +pi");
        testTokenizeCase(multiTokenList, "42 + pi");
        testTokenizeCase(multiTokenList, "42   +pi");
    }
}
