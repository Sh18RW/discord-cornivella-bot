package ru.cornivella.discord.math.tools;

public class ArgumentExpression extends Expression {
    private Expression argument;
    private Expression nextArgument;
    private ArgumentSeparatorToken token;

    

    public Expression getArgument() {
        return argument;
    }
    public void setArgument(Expression argument) {
        this.argument = argument;
    }
    public Expression getNextArgument() {
        return nextArgument;
    }
    public void setNextArgument(Expression nextArgument) {
        this.nextArgument = nextArgument;
    }
    public ArgumentSeparatorToken getToken() {
        return token;
    }
    public void setToken(ArgumentSeparatorToken token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return String.format("[ArgumentExpression %s %s]", argument, nextArgument);
    }
}
