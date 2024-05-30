package ru.cornivella.discord.math.tools;

import java.util.Arrays;
import java.util.List;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class FunctionToken extends Token<FunctionType> {
    private static final List<String> supportedFunctions = Arrays.asList("cos", "sin", "ctg", "tg");

    private FunctionToken(FunctionType value, int tracer) {
        super(value, TokenType.Function, tracer);
    }

    public static Token<FunctionType> makeToken(String value, int tracer) throws ArithmeticParsingErrorException {
        FunctionType functionType;

        switch (value) {
            case "cos":
                functionType = FunctionType.Cos;
                break;
            case "sin":
                functionType = FunctionType.Sin;
                break;
            case "ctg":
                functionType = FunctionType.Ctg;
                break;
            case "tg":
                functionType = FunctionType.Tg;
                break;
            default:
                throw new ArithmeticParsingErrorException(String.format("Unknown function %s.", value));
        }

        return new FunctionToken(functionType, tracer);
    }

    public static boolean isFunction(String value)
    {
        return supportedFunctions.contains(value);
    }

    @Override
    public String toString() {
        return String.format("[FunctionToken %s]", getValue().toString());
    }
}
