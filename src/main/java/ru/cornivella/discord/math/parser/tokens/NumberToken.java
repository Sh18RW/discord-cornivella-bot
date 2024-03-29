package ru.cornivella.discord.math.parser.tokens;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class NumberToken extends Token<Double> {

    protected NumberToken(double value, String meta) {
        super(TokenType.Number, value, meta);
    }

    public static final NumberToken parse(String value, String meta) throws ArithmeticParsingErrorException {
        try {
            double numberValue = Double.parseDouble(value.toString());
            return new NumberToken(numberValue, meta);
        } catch (Exception e) {
            throw new ArithmeticParsingErrorException("can't parse number value '" + value.toString() + '!', meta);
        }
    }

}
