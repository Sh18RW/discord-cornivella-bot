package ru.cornivella.discord.parser.tokens;

public abstract class Token<T> {
    private final Type type;
    private final T value;

    public Token(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    public enum Type {
        Number,
        Operation,
        Parenthesis,

        None
    }
}
