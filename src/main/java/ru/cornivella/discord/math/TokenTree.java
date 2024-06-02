package ru.cornivella.discord.math;

import java.util.List;
import java.util.Stack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.cornivella.discord.math.tools.ArgumentExpression;
import ru.cornivella.discord.math.tools.ArgumentSeparatorToken;
import ru.cornivella.discord.math.tools.ConstantExpression;
import ru.cornivella.discord.math.tools.ConstantToken;
import ru.cornivella.discord.math.tools.Expression;
import ru.cornivella.discord.math.tools.FunctionExpression;
import ru.cornivella.discord.math.tools.FunctionToken;
import ru.cornivella.discord.math.tools.NumberExpression;
import ru.cornivella.discord.math.tools.NumberToken;
import ru.cornivella.discord.math.tools.OperationExpression;
import ru.cornivella.discord.math.tools.OperationType;
import ru.cornivella.discord.math.tools.OperatorToken;
import ru.cornivella.discord.math.tools.ParenthesisToken;
import ru.cornivella.discord.math.tools.ParenthesisType;
import ru.cornivella.discord.math.tools.Token;
import ru.cornivella.discord.math.tools.TokenType;

public class TokenTree {
    @SuppressWarnings("rawtypes")
    private final List<Token> tokenList;
    private final Logger logger;

    /*
     * For (2 + 2) * (3 + 3) it contains "master" expression as (2 + 2) * (3 + 3), 2
     * + 2, 3 + 3.
     * 
     * @see #processParenthesis(ParenthesisToken)
     */
    private final Stack<Expression> expressionStack = new Stack<>();
    private final Stack<Boolean> masterSign = new Stack<>();

    private Expression root;
    private int openedParenthesis;
    private ReadingType readingType;
    //private OperationExpression targetOperationExpression;
    // private Expression targetNumberExpression;
    private Stack<Expression> targetsNumberExpression = new Stack<>();
    private Stack<OperationExpression> targetsOperationExpression = new Stack<>();

    @SuppressWarnings("rawtypes")
    public TokenTree(List<Token> tokenList) {
        logger = LogManager.getLogger(getClass());
        this.tokenList = tokenList;

        openedParenthesis = 0;
        readingType = ReadingType.Expression;
        //targetOperationExpression = null;
        // targetNumberExpression = null;
        targetsNumberExpression.add(null);
        targetsOperationExpression.add(null);
        masterSign.add(false);
    }

    // Here is some magic :) I will try to make it some readable
    @SuppressWarnings("rawtypes")
    public void build() throws ArithmeticParsingErrorException {
        logger.debug(String.format("Starting building tree with %s tokens.", tokenList.toString()));

        for (Token token : tokenList) {
            TokenType tokenType = token.getTokenType();

            if (tokenType == TokenType.Number) {
                processNumber((NumberToken) token);
            } else if (tokenType == TokenType.Operator) {
                processOperator((OperatorToken) token);
            } else if (tokenType == TokenType.Parentheses) {
                processParenthesis((ParenthesisToken) token);
            } else if (tokenType == TokenType.Constant) {
                processConstant((ConstantToken) token);
            } else if (tokenType == TokenType.Function) {
                processFunction((FunctionToken) token);
            } else if (tokenType == TokenType.ArgumentsSeparator) {
                processArgumentSeparator((ArgumentSeparatorToken) token);
            }
        }

        if (openedParenthesis != 0) {
            throw new ArithmeticTokenTreeException(String.format("%d parenthesis was not closed.", openedParenthesis));
        }

        root = expressionStack.firstElement();
        logger.debug(String.format("TokenTree was successful built %s.", expressionStack.firstElement().toString()));
    }

    private void processNumber(NumberToken numberToken) {
        NumberExpression numberExpression = new NumberExpression();
        numberExpression.setNumberToken(numberToken);
        setCurrentNumberExpression(numberExpression);
    }

    private void processOperator(OperatorToken operatorToken) {
        if (readingType == ReadingType.Expression) {
            if (operatorToken.getValue() == OperationType.Minus) {
                masterSign.add(!masterSign.pop());
                return;
            } else {
                throw new ArithmeticTokenTreeException(
                        String.format("Wait for expression but got operator at %d.", operatorToken.getTracer()));
            }
        }

        Expression currentExpression = expressionStack.peek();
        OperationType operationType = operatorToken.getValue();
        OperationExpression newOperationExpression = new OperationExpression();
        newOperationExpression.setOperatorToken(operatorToken);

        if (operationType == OperationType.Multiply || operationType == OperationType.Divide) {
            newOperationExpression.setLeftExpression(currentExpression);
            if (isWhole(currentExpression)) { // NumberExpression or whole OperationExpression
                replaceExpressionByNew(newOperationExpression);
            } else if (currentExpression instanceof OperationExpression) {
                OperationExpression currentOperationExpression = (OperationExpression) currentExpression;
                Expression rightExpression = currentOperationExpression.getRightExpression();

                if (isWhole(rightExpression)) {
                    swapRightExpression(currentOperationExpression, newOperationExpression, rightExpression);
                } else {
                    OperationExpression rightOperationExpression = (OperationExpression) rightExpression;

                    swapRightExpression(currentOperationExpression, newOperationExpression,
                            rightOperationExpression.getRightExpression());
                }
            } else {
                throw new ArithmeticTokenTreeException(
                        String.format("Tried to process operator at %d, but got unknown last expression %s.",
                                operatorToken.getTracer(), currentExpression));
            }
        } else if (operationType == OperationType.Plus || operationType == OperationType.Minus) {
            newOperationExpression.setLeftExpression(currentExpression);
            replaceExpressionByNew(newOperationExpression);
        } else if (operationType == OperationType.Degree) {
            newOperationExpression.setLeftExpression(currentExpression);
            if (currentExpression.isWhole()) {
                replaceExpressionByNew(newOperationExpression);
            } else {
                OperationExpression currentOperationExpression = (OperationExpression) currentExpression;
                setWholeRightExpression(currentOperationExpression, newOperationExpression);
            }
        } else {
            throw new ArithmeticTokenTreeException(String.format("Tried to process unknown operator %s at %d.",
                    operationType, operatorToken.getTracer()));
        }

        //targetOperationExpression = newOperationExpression;
        // targetNumberExpression = null;
        setTargetOperationExpression(newOperationExpression);
        setTargetNumberExpression(null);
        readingType = ReadingType.Expression;
    }

    private void processParenthesis(ParenthesisToken parenthesisToken) {
        ParenthesisType parenthesisType = parenthesisToken.getValue();

        if (parenthesisType == ParenthesisType.Open) {
            openedParenthesis++;
            masterSign.add(false);
            if (readingType == ReadingType.Operator && !(targetsNumberExpression.peek() instanceof FunctionExpression)) {
                processOperator(new OperatorToken(OperationType.Multiply, parenthesisToken.getTracer()));
            }

            targetsNumberExpression.add(null);
            targetsOperationExpression.add(null);
        } else if (parenthesisType == ParenthesisType.Close) {
            if (--openedParenthesis < 0) {
                throw new ArithmeticTokenTreeException(String.format("Useless parenthesis at %d.",
                        parenthesisToken.getTracer()));
            }
            Expression expression = expressionStack.pop();
            masterSign.pop();

            targetsOperationExpression.pop();
            targetsNumberExpression.pop();

            // if (expressionStack.size() != 0) {
            //     if (expressionStack.peek() instanceof OperationExpression)
            //         targetOperationExpression = (OperationExpression) expressionStack.peek();
            // }

            expression.setWhole(true);
            setCurrentNumberExpression(expression, true);
        } else {
            // mmmm
        }
    }

    private void processFunction(FunctionToken functionToken) {
        FunctionExpression functionExpression = new FunctionExpression();
        functionExpression.setFunctionToken(functionToken);
        setCurrentNumberExpression(functionExpression);
    }

    private void processConstant(ConstantToken constantToken) {
        ConstantExpression constantExpression = new ConstantExpression();
        constantExpression.setConstantToken(constantToken);
        setCurrentNumberExpression(constantExpression);
    }

    private void processArgumentSeparator(ArgumentSeparatorToken argumentSeparatorToken) {
    }

    private boolean isOperationPriority(Expression expression) { // if operator has priority as multiply or dividing
                                                                 // (ignore degree and factorial)
        if (!(expression instanceof OperationExpression))
            return false;
        OperationExpression operationExpression = (OperationExpression) expression;
        OperatorToken operatorToken = operationExpression.getOperatorToken();

        return operatorToken != null && (operatorToken.getValue() == OperationType.Multiply
                || operatorToken.getValue() == OperationType.Divide);
    }

    private boolean isWhole(Expression expression) {
        return expression.isWhole() || isOperationPriority(expression);
    }

    private void swapRightExpression(OperationExpression target, OperationExpression swapBy, Expression swapTarget) {
        swapBy.setLeftExpression(swapTarget);
        target.setRightExpression(swapBy);
    }

    public Expression getRoot() {
        return root;
    }

    private void replaceExpressionByNew(Expression newExpression) {
        expressionStack.pop();
        expressionStack.add(newExpression);
    }

    private void setWholeRightExpression(OperationExpression target, OperationExpression setBy) {
        while (true) {
            Expression right = target.getRightExpression();

            if (right.isWhole()) {
                setBy.setLeftExpression(right);
                target.setRightExpression(setBy);
                break;
            }

            target = (OperationExpression) right;
        }
    }

    private void setCurrentNumberExpression(Expression numberExpression) {
        setCurrentNumberExpression(numberExpression, false);
    }
    private void setCurrentNumberExpression(Expression numberExpression, boolean negativeSum) {
        numberExpression.setNegative(masterSign.pop(), negativeSum);
        masterSign.add(false);
        if (targetsOperationExpression.peek() == null) { // first writing
            if (targetsNumberExpression.peek() instanceof FunctionExpression) {
                FunctionExpression functionExpression = (FunctionExpression) targetsNumberExpression.peek();

                ArgumentExpression argumentExpression = new ArgumentExpression();
                argumentExpression.setArgument(numberExpression);
                argumentExpression.setNextArgument(functionExpression.getArguments());
                functionExpression.setArguments(argumentExpression);
            } else {
                expressionStack.add(numberExpression);
            }
        } else {
            OperationExpression targetOperationExpression = targetsOperationExpression.peek();
            targetOperationExpression.setRightExpression(numberExpression);
            OperationType operationType = targetOperationExpression.getOperatorToken().getValue();
            if (operationType == OperationType.Degree) {
                targetOperationExpression.setWhole(true);
            }
            setTargetOperationExpression(null);
        }

        if (numberExpression instanceof NumberExpression)
            setTargetNumberExpression((NumberExpression) numberExpression);

        readingType = ReadingType.Operator;
    }

    private void setTargetNumberExpression(Expression expression) {
        targetsNumberExpression.pop();
        targetsNumberExpression.add(expression);
    }

    private void setTargetOperationExpression(OperationExpression expression) {
        targetsOperationExpression.pop();
        targetsOperationExpression.add(expression);
    }

    private enum ReadingType {
        Expression, // number or parenthesis, functions or constants
        Operator, // any operator
    }
}
