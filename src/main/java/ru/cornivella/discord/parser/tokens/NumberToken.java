package ru.cornivella.discord.parser.tokens;

public class NumberToken extends Token<Double> {
    public NumberToken(Double value) {
        super(Token.Type.Number, value);
    }
}
