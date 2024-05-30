package ru.cornivella.discord.math.tools;

import java.util.Arrays;
import java.util.List;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class ConstantToken extends Token<ConstantType> {
    private final static List<String> supportedConstants = Arrays.asList("pi");

    private ConstantToken(ConstantType value, int tracer) {
        super(value, TokenType.Constant, tracer);
    }

    public static Token<ConstantType> makeToken(String value, int tracer) throws ArithmeticParsingErrorException {
        ConstantType constantType;

        switch (value) {
            case "pi":
                constantType = ConstantType.Pi;
                break;
            default:
                throw new ArithmeticParsingErrorException(String.format("Unknown function %s.", value));
        }

        return new ConstantToken(constantType, tracer);
    }

    public static boolean isConstant(String value)
    {
        return supportedConstants.contains(value);
    }

    @Override
    public String toString() {
        return String.format("[ConstantToken %s]", getValue().toString());
    }
}
