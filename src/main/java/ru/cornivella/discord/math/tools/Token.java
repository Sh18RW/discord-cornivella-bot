package ru.cornivella.discord.math.tools;

public abstract class Token <T> {
    private final T value;
    private final TokenType tokenType;
    private final int tracer;

    public Token(T value, TokenType tokenType, int tracer) {
        this.value = value;
        this.tokenType = tokenType;
        this.tracer = tracer;
    }

    public T getValue() {
        return value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public int getTracer() {
        return tracer;
    }
}
