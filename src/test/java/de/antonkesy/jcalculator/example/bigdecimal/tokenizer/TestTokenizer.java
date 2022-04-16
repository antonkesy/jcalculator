package de.antonkesy.jcalculator.example.bigdecimal.tokenizer;

import de.antonkesy.jcalculator.example.bigdecimal.BigDecimalNumber;
import de.antonkesy.jcalculator.example.bigdecimal.BigDecimalTokenMap;
import de.antonkesy.jcalculator.tokenizer.Tokenizer;
import de.antonkesy.jcalculator.tokenizer.exception.UnknownTokenException;
import de.antonkesy.jcalculator.tokenizer.token.IToken;
import de.antonkesy.jcalculator.tokenizer.token.OperatorToken;
import de.antonkesy.jcalculator.tokenizer.token.Token;
import de.antonkesy.jcalculator.tokenizer.token.ValueToken;
import de.antonkesy.jcalculator.tokenizer.token.map.ITokenMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestTokenizer {
    private final ITokenMap tokenMap = new BigDecimalTokenMap();
    private final ValueToken PI = new ValueToken("pi", new BigDecimalNumber(Math.PI));
    private final OperatorToken add = new OperatorToken("+", 4, null);
    private final OperatorToken multi = new OperatorToken("*", 3, null);
    private final OperatorToken div = new OperatorToken("/", 3, null);
    private final OperatorToken expo = new OperatorToken("^", 2, null);
    private final Token open = new Token("(", 1);
    private final Token close = new Token(")", 1);

    @Test
    void testGetTokenFromStringLiteral() {
        try {
            testTokenizeCase(new ValueToken(new BigDecimalNumber(42)), "42");
            //testTokenizeCase(new ValueToken(new BigDecimalNumber(-42)), "-42");
            testTokenizeCase(new ValueToken(new BigDecimalNumber(123)), "0123");
            testTokenizeCase(new ValueToken(new BigDecimalNumber(0)), "0");

            Assertions.assertEquals(new ValueToken(new BigDecimalNumber(123)), new Tokenizer("0123", tokenMap).getToken().get(0));
            assertEquals(new ValueToken(new BigDecimalNumber(123)), new Tokenizer("0123", tokenMap).getToken().get(0));
        } catch (UnknownTokenException e) {
            fail();
        }
    }

    void testTokenizeCase(IToken token, String input) {
        ArrayList<IToken> tokenList = new ArrayList<>();
        tokenList.add(token);
        testTokenizeCase(tokenList, input);
    }

    void testTokenizeCase(ArrayList<IToken> expected, String input) {
        try {
            List<IToken> actualToken = new Tokenizer(input, tokenMap).getToken();
            assertEquals(expected, actualToken);
        } catch (UnknownTokenException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testTokenizeSingleTokenList() {
        testTokenizeCase(new ValueToken(new BigDecimalNumber(42)), "42");
        testTokenizeCase(add, "+");
        assertThrows(UnknownTokenException.class, () -> new Tokenizer("un", tokenMap));
        assertThrows(UnknownTokenException.class, () -> new Tokenizer("", tokenMap));
    }

    @Test
    void testTokenizeMultiple1() {
        ArrayList<IToken> multiTokenList = new ArrayList<>();
        multiTokenList.add(new ValueToken(new BigDecimalNumber(42)));
        multiTokenList.add(add);
        multiTokenList.add(PI);

        testTokenizeCase(multiTokenList, "42+pi");
        testTokenizeCase(multiTokenList, "42 +pi");
        testTokenizeCase(multiTokenList, "42 + pi");
        testTokenizeCase(multiTokenList, "42   +pi");
    }

    @Test
    void testTokenizeMultiple2() {
        ArrayList<IToken> multiTokenList = new ArrayList<>();
        multiTokenList.add(open);
        multiTokenList.add(new ValueToken(new BigDecimalNumber(42)));
        multiTokenList.add(div);
        multiTokenList.add(new ValueToken(new BigDecimalNumber(73)));
        multiTokenList.add(close);

        testTokenizeCase(multiTokenList, "(42/73)");
        testTokenizeCase(multiTokenList, "(42 /73)");
        testTokenizeCase(multiTokenList, "(42 / 73)");
        testTokenizeCase(multiTokenList, "( 42   /73)");
    }

    @Test
    void testExponent() {
        testTokenizeCase(expo, "^");
        ArrayList<IToken> expectList = new ArrayList<>();
        expectList.add(new ValueToken(new BigDecimalNumber(2)));
        expectList.add(expo);
        expectList.add(new ValueToken(new BigDecimalNumber(2)));
        testTokenizeCase(expectList, "2^2");
    }

    /* not supported yet
    @Test
    void testSignedLiterals() {
        //42+(-3+5) = 44
        ArrayList<IToken> expected = new ArrayList<>();
        expected.add(new ValueToken(new BigDecimalNumber(42)));
        expected.add(add);
        expected.add(open);
        expected.add(new ValueToken(new BigDecimalNumber(-3)));
        expected.add(add);
        expected.add(new ValueToken(new BigDecimalNumber(5)));
        expected.add(close);
        testTokenizeCase(expected, "42+(-3+5)");
    }
    */

    @Test
    void testDecimalLiterals() {
        testTokenizeCase(new ValueToken(new BigDecimalNumber("1.1")), "1.1");
        testTokenizeCase(new ValueToken(new BigDecimalNumber("42.123")), "42.123");
        //testTokenizeCase(new ValueToken(new BigDecimalNumber("-521.1")), "-521.1");
        //testTokenizeCase(new ValueToken(new BigDecimalNumber("-42.1234567891011121314151617181920212223")), "-42.1234567891011121314151617181920212223");
    }

 /* not supported
    @Test
    void testAddMultiplyLiteralParentheses() {
        ArrayList<IToken> expected = new ArrayList<>();
        expected.add(new ValueToken(new BigDecimalNumber(3)));
        expected.add(multi);
        expected.add(open);
        expected.add(new ValueToken(new BigDecimalNumber(-5)));
        expected.add(add);
        expected.add(new ValueToken(new BigDecimalNumber(4)));
        expected.add(close);
        testTokenizeCase(expected, "3*(-5+4)");
        //not supported yet
        //testTokenizeCase(expected, "3(-5+4)");
    }
  */

    @Test
    void testParentheses() {
        ArrayList<IToken> expected = new ArrayList<>();
        expected.add(open);
        expected.add(new ValueToken(new BigDecimalNumber(3)));
        expected.add(close);
        expected.add(add);
        expected.add(new ValueToken(new BigDecimalNumber(2)));
        testTokenizeCase(expected, "(3)+2");

    }

    @Test
    void testAddMultiplyLiteralConstant() {
        ArrayList<IToken> expected = new ArrayList<>();
        expected.add(new ValueToken(new BigDecimalNumber(3)));
        expected.add(multi);
        expected.add(PI);
        testTokenizeCase(expected, "3*pi");
        //not supported yet
        //testTokenizeCase(expected, "3pi");
    }
}