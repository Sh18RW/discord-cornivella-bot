package ru.cornivella.discord.math.parser.tokens;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class OperatorToken extends Token<OperatorType> {
    
    public OperatorToken(OperatorType value, String meta) {
        super(TokenType.Operation, value, meta);
    }

    public static final OperatorToken parse(String value, String meta) throws ArithmeticParsingErrorException {
        if (value.equals("+")) {
            return new OperatorToken(OperatorType.Plus, meta);
        }
        if (value.equals("-")) {
            return new OperatorToken(OperatorType.Minus, meta);
        }
        if (value.equals("*")) {
            return new OperatorToken(OperatorType.Multiply, meta);
        }
        if (value.equals("/")) {
            return new OperatorToken(OperatorType.Divide, meta);
        }
        if (value.equals("^")) {
            return new OperatorToken(OperatorType.Degree, meta);
        }

        throw new ArithmeticParsingErrorException("Couldn't find a "  + value + " operator.");
    }

}
