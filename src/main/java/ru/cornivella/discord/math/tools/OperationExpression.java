package ru.cornivella.discord.math.tools;

public class OperationExpression extends Expression{
    private Expression leftExpression;
    private Expression rightExpression;
    private OperatorToken operatorToken;

    public void setLeftExpression(Expression leftExpression) {
        this.leftExpression = leftExpression;
    }

    public void setRightExpression(Expression rightExpression) {
        this.rightExpression = rightExpression;
    }

    public void setOperatorToken(OperatorToken operatorToken) {
        this.operatorToken = operatorToken;
    }

    public Expression getLeftExpression() {
        return leftExpression;
    }

    public Expression getRightExpression() {
        return rightExpression;
    }

    public OperatorToken getOperatorToken() {
        return operatorToken;
    }

    

    @Override
    public String toString() {
        if (operatorToken.getValue() != OperationType.Factorial) {
            return String.format("OperationExpression: { \n%s \n\t%s \n%s\n}", leftExpression.toString(), operatorToken.toString(), rightExpression.toString());
        } else {
            return String.format("OperationExpression: { \n\t%s \n%s }", operatorToken.toString(), rightExpression.toString());
        }
    }
}
