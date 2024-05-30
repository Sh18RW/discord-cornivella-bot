package ru.cornivella.discord.math.tools;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;

public class ArgumentSeparatorToken extends Token<Character> {

    public ArgumentSeparatorToken(int tracer) {
        super(';', TokenType.ArgumentsSeparator, tracer);
    }

    public static Token<Character> makeToken(int tracer) throws ArithmeticParsingErrorException {
        return new ArgumentSeparatorToken(tracer);
    }
}
