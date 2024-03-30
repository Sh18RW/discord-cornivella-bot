package ru.cornivella.discord.math.parser.tokens;

public abstract class Token<T> {
    private final TokenType type;
    private final String meta;
    private final T value;

    protected Token(TokenType type, T value, String meta) {
        this.type = type;
        this.value = value;
        this.meta = meta;
    }

    public final TokenType getType() {
        return this.type;
    }

    public final T getValue() {
        return this.value;
    }

    public final String getMeta() {
        return this.meta;
    }


    @Override
    public String toString() {
        return "[Token:" + type + "," + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Token) {
            Token token = (Token) obj;
            return token.value.equals(value);
        }
        return false;
    }
}
