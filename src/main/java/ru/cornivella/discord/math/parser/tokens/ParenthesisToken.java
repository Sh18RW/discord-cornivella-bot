package ru.cornivella.discord.math.parser.tokens;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class ParenthesisToken extends Token<ParenthesisType>{
    private final boolean negative;

    public ParenthesisToken(ParenthesisType value, String meta, boolean negative) {
        super(TokenType.Parenthesis, value, meta);
        this.negative = negative;
    }

    public final boolean isNegative() {
        return negative;
    }

    public static final ParenthesisToken parse(String value, String meta, boolean isNegative) throws ArithmeticParsingErrorException {
        if (value.equals("(")) {
            return new ParenthesisToken(ParenthesisType.Open, meta, isNegative);
        }
        if (value.equals(")")) {
            return new ParenthesisToken(ParenthesisType.Close, meta, false);
        }

        throw new ArithmeticParsingErrorException("Couldn't find a " + value + " parenthesis!");
    }

    @Override
    public String toString() {
        return "[Token:" + getType() + "," + getValue() + "," + negative + "]";
    }

}
