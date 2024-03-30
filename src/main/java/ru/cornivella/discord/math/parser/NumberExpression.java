package ru.cornivella.discord.math.parser;

import ru.cornivella.discord.math.parser.tokens.TokenType;

public class NumberExpression extends Expression {

    private final double value;

    public NumberExpression(double value, String meta, boolean negative) {
        super(meta, TokenType.Number, negative);
        this.value = value;
    }

    @Override
    public double solve() {
        double result = value;
        if (negative)
            result *= -1;
        return result;
    }

    @Override
    public String toString() {
        return "[NumberExpression:" + value + "]";
    }

}
