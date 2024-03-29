package ru.cornivella.discord.math.parser;

import ru.cornivella.discord.math.parser.tokens.TokenType;

public class NumberExpression extends Expression {

    private final double value;

    public NumberExpression(double value, String meta) {
        super(meta, TokenType.Number);
        this.value = value;
    }

    @Override
    public double solve() {
        return value;
    }

}
