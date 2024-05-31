package ru.cornivella.discord.math.tools;

public class ConstantExpression extends NumberExpression {
    private ConstantToken constantToken;

    public ConstantToken getConstantToken() {
        return constantToken;
    }

    public void setConstantToken(ConstantToken constantToken) {
        this.constantToken = constantToken;
    }

    @Override
    public String toString() {
        return String.format("[Constant %s]", constantToken);
    }
}
