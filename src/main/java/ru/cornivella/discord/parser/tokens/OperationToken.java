package ru.cornivella.discord.parser.tokens;

import ru.cornivella.discord.parser.tokens.OperationToken.OperationType;

public class OperationToken extends Token<OperationType> {
    public OperationToken(OperationType value) {
        super(Token.Type.Operation, value);
    }

    public enum OperationType {
        Plus,
        Minus,
        Multiply,
        Divide
    }
}
