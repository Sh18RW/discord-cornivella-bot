package ru.cornivella.discord.math.tools;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class NumberToken extends Token<Double>
{
    private NumberToken(Double value, int tracer) {
        super(value, TokenType.Number, tracer);
    }

    public static Token<Double> makeToken(String value, int tracer) throws ArithmeticParsingErrorException {
        try {
            double numberValue = Double.parseDouble(value);
            return new NumberToken(numberValue, tracer);
        } catch (NumberFormatException e) {
            throw new ArithmeticParsingErrorException(String.format("Can't parse number '%s'.", value));
        }
    }

    @Override
    public String toString() {
        return String.format("[NumberToken %s]", getValue().toString());
    }
}
