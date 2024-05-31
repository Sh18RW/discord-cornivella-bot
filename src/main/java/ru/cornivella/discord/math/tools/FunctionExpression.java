package ru.cornivella.discord.math.tools;

public class FunctionExpression extends NumberExpression {
    private ArgumentExpression arguments;
    private FunctionToken functionToken;

    public FunctionToken getFunctionToken() {
        return functionToken;
    }

    public void setFunctionToken(FunctionToken functionToken) {
        this.functionToken = functionToken;
    }

    @Override
    public String toString() {
        return String.format("[Function %s %s]", functionToken, arguments);
    }

    public ArgumentExpression getArguments() {
        return arguments;
    }

    public void setArguments(ArgumentExpression arguments) {
        this.arguments = arguments;
    }
}
