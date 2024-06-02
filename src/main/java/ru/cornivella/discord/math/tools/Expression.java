package ru.cornivella.discord.math.tools;

public abstract class Expression {
    private boolean isNegative;
    private boolean isWhole; // surrounded by parenthesis or can't be changed (used in operator processor)

    public void switchNegative()
    {
        isNegative = !isNegative;
    }

    public boolean isNegative() {
        return isNegative;
    }

    public void setNegative(boolean isNegative) {
        setNegative(isNegative, false);
    }

    public void setNegative(boolean isNegative, boolean negativeSum) {
        this.isNegative = isNegative ^ (this.isNegative && negativeSum);
    }

    public boolean isWhole() {
        return isWhole;
    }

    public void setWhole(boolean isWhole) {
        this.isWhole = isWhole;
    }
}
