package com.antonkesy.jcalculator;

import com.antonkesy.jcalculator.parser.Parser;
import com.antonkesy.jcalculator.parser.ast_nodes.ExpressionNode;
import com.antonkesy.jcalculator.parser.ast_nodes.FactorNode;
import com.antonkesy.jcalculator.parser.ast_nodes.Node;
import com.antonkesy.jcalculator.parser.exception.MissingTokenException;
import com.antonkesy.jcalculator.tokenizer.Tokenizer;
import com.antonkesy.jcalculator.tokenizer.exception.UnknownTokenException;
import com.antonkesy.jcalculator.tokenizer.token.Token;
import com.antonkesy.jcalculator.tokenizer.token.operator.OperatorToken;
import com.antonkesy.jcalculator.tokenizer.token.value.ValueToken;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class JCalculator {
    public static BigDecimal calculate(Tokenizer tokenizer) {
        Parser parser = new Parser(tokenizer);
        Node rootAst = parser.parse();
        return calculateAst(rootAst);
    }

    public static BigDecimal calculate(String expressionString) throws UnknownTokenException, MissingTokenException {
        return calculate(new Tokenizer(expressionString));
    }

    public static BigDecimal calculate(List<Token> tokenList) {
        Tokenizer tokenizer = new Tokenizer(tokenList);
        return calculate(tokenizer);
    }

    private static BigDecimal calculateAst(Node astNode) {
        if (astNode == null) {
            return new BigDecimal(0);
        } else if (astNode instanceof FactorNode) {
            return ((ValueToken) astNode.token).getValue();
        } else if (astNode instanceof ExpressionNode) {
            return calculateExpression((ExpressionNode) astNode);
        }
        return new BigDecimal(0);
    }

    private static BigDecimal calculateExpression(ExpressionNode node) {
        BigDecimal result = new BigDecimal(0);
        BigDecimal leftValue = calculateAst(node.leftChild);
        BigDecimal rightValue = calculateAst(node.rightChild);
        switch (((OperatorToken) node.token).operator) {
            case ADD:
                result = leftValue.add(rightValue);
                break;
            case SUB:
                result = leftValue.subtract(rightValue);
                break;
            case MULTIPLY:
                result = leftValue.multiply(rightValue);
                break;
            case DIVIDE:
                result = leftValue.divide(rightValue);
                break;
            case EXPONENT:
                int exponent = rightValue.intValueExact();
                result = leftValue.pow(exponent);
                break;
        }
        return result;
    }
}

