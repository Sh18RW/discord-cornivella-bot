package ru.cornivella.discord.math.parser;

import ru.cornivella.discord.math.ArithmeticErrorException;

public abstract class Expression {
    protected final String meta;
    private final TokenType tokenType;

    public Expression(String meta, TokenType tokenType) {
        this.meta = meta;
        this.tokenType = tokenType;
    }

    public abstract double solve() throws ArithmeticErrorException;

    public final TokenType getType() {
        return tokenType;
    }
}
