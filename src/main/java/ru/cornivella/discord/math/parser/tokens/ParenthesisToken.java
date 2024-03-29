package ru.cornivella.discord.math.parser.tokens;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class ParenthesisToken extends Token<ParenthesisType>{

    public ParenthesisToken(ParenthesisType value, String meta) {
        super(TokenType.Parenthesis, value, meta);
    }

    public static final ParenthesisToken parse(String value, String meta) throws ArithmeticParsingErrorException {
        if (value.equals("(")) {
            return new ParenthesisToken(ParenthesisType.Open, meta);
        }
        if (value.equals(")")) {
            return new ParenthesisToken(ParenthesisType.Close, meta);
        }

        throw new ArithmeticParsingErrorException("Couldn't find a " + value + " parenthesis!");
    }

}
