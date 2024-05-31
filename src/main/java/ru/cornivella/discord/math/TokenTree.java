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

    private final Stack<Expression> expressionStack = new Stack<>();

    private Expression root;
    private int openedParenthesis;
    private ReadingType readingType;

    @SuppressWarnings("rawtypes")
    public TokenTree(List<Token> tokenList) {
        logger = LogManager.getLogger(getClass());
        this.tokenList = tokenList;

        openedParenthesis = 0;
        readingType = ReadingType.Expression;
    }

    @SuppressWarnings("rawtypes")
    public void build() throws ArithmeticParsingErrorException {
        logger.debug(String.format("Starting building tree with %s tokens.", tokenList.toString()));
        expressionStack.add(new NumberExpression());

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

        logger.debug(String.format("TokenTree was successful built %s.", expressionStack.firstElement().toString()));
    }

    private void processNumber(NumberToken numberToken) {
        checkMultiplyMiss(numberToken.getTracer());

        Expression currentExpression = expressionStack.peek();

        if (currentExpression instanceof NumberExpression) {
            if (currentExpression instanceof FunctionExpression) {
                NumberExpression argumentNumber = new NumberExpression();
                argumentNumber.setNumberToken(numberToken);
                ArgumentExpression argumentExpression = new ArgumentExpression();
                argumentExpression.setArgument(argumentNumber);
                FunctionExpression functionExpression = (FunctionExpression) currentExpression;
                functionExpression.setArguments(argumentExpression);
            } else {
                ((NumberExpression) currentExpression).setNumberToken(numberToken);
            }
        } else if (currentExpression instanceof OperationExpression) {
            NumberExpression numberExpression = new NumberExpression();
            numberExpression.setNumberToken(numberToken);

            OperationExpression operationExpression = (OperationExpression) currentExpression;
            if (operationExpression.getRightExpression() != null) {
                Expression rightExpression = operationExpression.getRightExpression();
                if (rightExpression instanceof OperationExpression) {
                    ((OperationExpression) operationExpression.getRightExpression())
                            .setRightExpression(numberExpression);
                } else if (rightExpression instanceof FunctionExpression) {
                    ArgumentExpression argumentExpression = new ArgumentExpression();
                    argumentExpression.setArgument(numberExpression);
                    ((FunctionExpression) rightExpression).setArguments(argumentExpression);
                } else {
                    throw new ArithmeticTokenTreeException(
                            String.format("Try to add number token branch, but it already has number at %d!",
                                    numberToken.getTracer()));
                }
            } else {
                operationExpression.setRightExpression(numberExpression);
            }
        } else if (currentExpression instanceof ArgumentExpression) {
            NumberExpression argumentNumber = new NumberExpression();
            argumentNumber.setNumberToken(numberToken);
            ArgumentExpression argumentExpression = (ArgumentExpression) currentExpression;
            argumentExpression.setNextArgument(argumentNumber);
        }

        readingType = ReadingType.Operator;
    }

    private void processOperator(OperatorToken operatorToken) {
        OperationType operationType = operatorToken.getValue();
        Expression currentExpression = expressionStack.peek();

        if (readingType != ReadingType.Operator) {
            throw new ArithmeticTokenTreeException(String.format("Wait for number, but got operator %s on %d.",
                    operatorToken.getValue().toString(), operatorToken.getTracer()));
        }

        if (operationType == OperationType.Divide || operationType == OperationType.Multiply) {
            OperationExpression newOperationExpression = new OperationExpression();
            newOperationExpression.setOperatorToken(operatorToken);

            if (currentExpression instanceof NumberExpression) {
                newOperationExpression.setLeftExpression(currentExpression);
                expressionStack.pop();
                expressionStack.add(newOperationExpression);
            } else if (currentExpression instanceof OperationExpression) {
                OperationExpression operationExpression = (OperationExpression) currentExpression;
                Expression rightExpression = operationExpression.getRightExpression();
                newOperationExpression.setLeftExpression(rightExpression);
                operationExpression.setRightExpression(newOperationExpression);
            } else {
                throw new ArithmeticTokenTreeException(
                        String.format("Can't process operator because last expression is %s on %d.",
                                currentExpression.toString(), operatorToken.getTracer()));
            }
            readingType = ReadingType.Expression;
        } else if (operationType == OperationType.Factorial) {
            OperationExpression newOperationExpression = new OperationExpression();
            newOperationExpression.setOperatorToken(operatorToken);

            if (currentExpression instanceof NumberExpression) {
                newOperationExpression.setRightExpression(currentExpression);
                expressionStack.pop();
                expressionStack.add(newOperationExpression);
            } else if (currentExpression instanceof OperationExpression) {
                OperationExpression operationExpression = (OperationExpression) currentExpression;
                Expression rightExpression = operationExpression.getRightExpression();
                newOperationExpression.setRightExpression(rightExpression);
                operationExpression.setRightExpression(newOperationExpression);
            } else {
                throw new ArithmeticTokenTreeException(
                        String.format("Can't process operator because last expression is %s on %d.",
                                currentExpression.toString(), operatorToken.getTracer()));
            }

            readingType = ReadingType.Operator;
        } else if (operationType == OperationType.Degree) {
            OperationExpression newOperationExpression = new OperationExpression();
            newOperationExpression.setOperatorToken(operatorToken);
            if (currentExpression instanceof NumberExpression) {
                newOperationExpression.setLeftExpression(currentExpression);
                expressionStack.pop();
                expressionStack.add(newOperationExpression);
            } else if (currentExpression instanceof OperationExpression) {
                OperationExpression operationExpression = (OperationExpression) currentExpression;
                Expression rightExpression = operationExpression.getRightExpression();
                newOperationExpression.setLeftExpression(rightExpression);
                operationExpression.setRightExpression(newOperationExpression);
            } else {
                throw new ArithmeticTokenTreeException(
                        String.format("Can't process operator because last expression is %s on %d.",
                                currentExpression.toString(), operatorToken.getTracer()));
            }

            readingType = ReadingType.Expression;
        } else if (operationType == OperationType.Plus || operationType == OperationType.Minus) {
            OperationExpression newOperationExpression = new OperationExpression();
            newOperationExpression.setOperatorToken(operatorToken);
            newOperationExpression.setLeftExpression(currentExpression);
            expressionStack.pop();
            expressionStack.add(newOperationExpression);

            readingType = ReadingType.Expression;
        }
    }

    private void processParenthesis(ParenthesisToken parenthesisToken) {
        if (parenthesisToken.getValue() == ParenthesisType.Open) {
            checkMultiplyMiss(parenthesisToken.getTracer());

            openedParenthesis++;

            NumberExpression newNumberExpression = new NumberExpression();
            expressionStack.add(newNumberExpression);
        } else if (parenthesisToken.getValue() == ParenthesisType.Close) {
            if (openedParenthesis-- <= 0) {
                throw new ArithmeticTokenTreeException(
                        String.format("Got closed useless parenthesis at %d!", parenthesisToken.getTracer()));
            }

            Expression rightExpression = expressionStack.pop();
            Expression currentExpression = expressionStack.peek();

            if (currentExpression instanceof FunctionExpression) {
                FunctionExpression functionExpression = (FunctionExpression) currentExpression;

                if (rightExpression instanceof NumberExpression || rightExpression instanceof OperationExpression) {
                    ArgumentExpression argumentExpression = new ArgumentExpression();
                    argumentExpression.setArgument(rightExpression);
                    functionExpression.setArguments(argumentExpression);
                } else if (rightExpression instanceof ArgumentExpression) {
                    ArgumentExpression argumentExpression = (ArgumentExpression) rightExpression;
                    functionExpression.setArguments(argumentExpression);
                } else {
                    throw new ArithmeticTokenTreeException(String.format("Tried to convert to function arguments, but it fails at %d!",
                        parenthesisToken.getTracer()));
                }
                return;
            }

            if (!(currentExpression instanceof OperationExpression)) {
                throw new ArithmeticTokenTreeException(String.format(
                        "Got parenthesis, but it doesn't match requirements at %d!", parenthesisToken.getTracer()));
            }

            OperationExpression operationExpression = (OperationExpression) currentExpression;
            operationExpression.setRightExpression(rightExpression);
        }
    }

    private void processFunction(FunctionToken functionToken) {
        checkMultiplyMiss(functionToken.getTracer());

        Expression currentExpression = expressionStack.peek();
        FunctionExpression functionExpression = new FunctionExpression();
        functionExpression.setFunctionToken(functionToken);
        if (currentExpression instanceof NumberExpression) {
            expressionStack.pop();
            expressionStack.add(functionExpression);
        } else if (currentExpression instanceof OperationExpression) {
            OperationExpression operationExpression = (OperationExpression) currentExpression;
            if (operationExpression.getRightExpression() != null) {
                if (operationExpression.getRightExpression() instanceof OperationExpression) {
                    ((OperationExpression) operationExpression.getRightExpression())
                            .setRightExpression(functionExpression);
                } else {
                    throw new ArithmeticTokenTreeException(
                            String.format("Try to add number token branch, but it already has number at %d!",
                                    functionToken.getTracer()));
                }
            } else {
                operationExpression.setRightExpression(functionExpression);
            }
        }
    }

    private void processConstant(ConstantToken constantToken) {
        checkMultiplyMiss(constantToken.getTracer());

        Expression currentExpression = expressionStack.peek();
        ConstantExpression constantExpression = new ConstantExpression();
        constantExpression.setConstantToken(constantToken);
        if (currentExpression instanceof NumberExpression) {
            expressionStack.pop();
            expressionStack.add(constantExpression);
        } else if (currentExpression instanceof OperationExpression) {
            OperationExpression operationExpression = (OperationExpression) currentExpression;
            if (operationExpression.getRightExpression() != null) {
                if (operationExpression.getRightExpression() instanceof OperationExpression) {
                    ((OperationExpression) operationExpression.getRightExpression())
                            .setRightExpression(constantExpression);
                } else {
                    throw new ArithmeticTokenTreeException(
                            String.format("Try to add number token branch, but it already has number at %d!",
                                    constantToken.getTracer()));
                }
            } else {
                operationExpression.setRightExpression(constantExpression);
            }
        } else if (currentExpression instanceof ArgumentExpression) {
            ArgumentExpression argumentExpression = (ArgumentExpression) currentExpression;

            argumentExpression.setArgument(argumentExpression);
        }
    }

    private void processArgumentSeparator(ArgumentSeparatorToken argumentSeparatorToken) {
        ArgumentExpression argumentExpression = new ArgumentExpression();
        Expression currentExpression = expressionStack.pop();
        argumentExpression.setToken(argumentSeparatorToken);
        argumentExpression.setArgument(currentExpression);
        expressionStack.add(argumentExpression);

        readingType = ReadingType.Expression;
    }

    private void checkMultiplyMiss(int tracer) {
        // something like 2 (3 + 2) means 2 * (3 + 2)
        if (readingType == ReadingType.Operator) {
            processOperator(new OperatorToken(OperationType.Multiply, tracer));
        }
    }

    public Expression getRoot() {
        return root;
    }

    private enum ReadingType {
        Expression, // number or parenthesis, functions or constants
        Operator, // any operator
    }
}
