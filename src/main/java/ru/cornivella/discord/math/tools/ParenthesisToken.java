package ru.cornivella.discord.math.tools;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class ParenthesisToken extends Token<ParenthesisType> {
    public ParenthesisToken(ParenthesisType value, int tracer) {
        super(value, TokenType.Parentheses, tracer);
    }

    public static Token<ParenthesisType> makeToken(String value, int tracer) throws ArithmeticParsingErrorException {
        ParenthesisType parenthesisType;

        switch (value) {
            case "(":
                parenthesisType = ParenthesisType.Open;
                break;
            case ")":
                parenthesisType = ParenthesisType.Close;
                break;
            default:
                throw new ArithmeticParsingErrorException(String.format("Can't parse parenthesis '%s'.", value));
        }

        return new ParenthesisToken(parenthesisType, tracer);
    }

    @Override
    public String toString() {
        return String.format("[ParenthesisToken %s]", getValue().toString());
    }
}
