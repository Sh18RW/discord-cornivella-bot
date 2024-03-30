package ru.cornivella.discord.math.parser;

import ru.cornivella.discord.math.ArithmeticErrorException;
import ru.cornivella.discord.math.parser.tokens.TokenType;

public abstract class Expression {
    protected final String meta;
    protected boolean negative;
    private final TokenType tokenType;

    public Expression(String meta, TokenType tokenType, boolean negative) {
        this.meta = meta;
        this.negative = negative;
        this.tokenType = tokenType;
    }

    public abstract double solve() throws ArithmeticErrorException;

    public final TokenType getType() {
        return tokenType;
    }

    public final void setNegative(boolean negative) {
        this.negative = negative;
    }
}
