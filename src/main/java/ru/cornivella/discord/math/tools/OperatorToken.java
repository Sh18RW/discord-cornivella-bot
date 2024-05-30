package ru.cornivella.discord.math.tools;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class OperatorToken extends Token<OperationType>
{

    public OperatorToken(OperationType value, int tracer)
    {
        super(value, TokenType.Operator, tracer);
    }

    public static Token<OperationType> makeToken(String value, int tracer) throws ArithmeticParsingErrorException
    {
        OperationType operationType;
        switch (value) {
            case "+":
                operationType = OperationType.Plus;
                break;
            case "-":
                operationType = OperationType.Minus;
                break;
            case "*":
                operationType = OperationType.Multiply;
                break;
            case "/":
                operationType = OperationType.Divide;
                break;
            case "^":
                operationType = OperationType.Degree;
                break;
            case "!":
                operationType = OperationType.Factorial;
                break;
            default:
                throw new ArithmeticParsingErrorException(String.format("Can't parse operator %s.", value));
        }

        return new OperatorToken(operationType, tracer);
    }

    @Override
    public String toString() {
        return String.format("[OperatorToken %s]", getValue().toString());
    }
}
