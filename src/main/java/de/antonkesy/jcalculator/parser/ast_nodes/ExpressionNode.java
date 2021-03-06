package de.antonkesy.jcalculator.parser.ast_nodes;

import de.antonkesy.jcalculator.tokenizer.token.Token;

import java.util.Objects;

public class ExpressionNode extends Node {

    public final Node leftChild, rightChild;

    public ExpressionNode(Token token, Node leftChild, Node rightChild) {
        super(token);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpressionNode that = (ExpressionNode) o;
        return Objects.equals(leftChild, that.leftChild) && Objects.equals(rightChild, that.rightChild);
    }

    @Override
    String getRepresentation() {
        return null;
    }
}
