package com.antonkesy.jcalculator.tokenizer.token.separator;

import com.antonkesy.jcalculator.tokenizer.token.Token;
import com.antonkesy.jcalculator.tokenizer.token.TypeRepresentation;

public enum SeparatorType implements TypeRepresentation {
    OPEN("("), CLOSE(")");

    public final String representation;

    SeparatorType(String representation) {
        this.representation = representation;
    }

    @Override
    public String getTypeRepresentation() {
        return representation;
    }

    @Override
    public Token createToken() {
        return new SeparatorToken(this);
    }
}