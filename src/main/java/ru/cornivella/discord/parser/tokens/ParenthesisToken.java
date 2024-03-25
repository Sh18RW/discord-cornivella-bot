package ru.cornivella.discord.parser.tokens;

import ru.cornivella.discord.parser.tokens.ParenthesisToken.ParenthesisType;

public class ParenthesisToken extends Token<ParenthesisType> {
    public ParenthesisToken(ParenthesisType value) {
        super(Token.Type.Parenthesis, value);
    }

    public enum ParenthesisType {
        Open,
        Close
    }
}
