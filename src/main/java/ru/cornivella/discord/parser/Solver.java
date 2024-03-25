package ru.cornivella.discord.parser;

import java.util.List;

import ru.cornivella.discord.parser.tokens.NumberToken;
import ru.cornivella.discord.parser.tokens.OperationToken;
import ru.cornivella.discord.parser.tokens.OperationToken.OperationType;
import ru.cornivella.discord.parser.tokens.ParenthesisToken;
import ru.cornivella.discord.parser.tokens.Token;

public class Solver {
    public static double solve(String expression) throws ArithmeticErrorException {
        List<Token> tokenList = Parser.parse(expression);
        double result = 0;

        for (int i = 0;i < tokenList.size();i++) {
            SolverState solverState = iteration(tokenList, i);

            result += solverState.value();
            i = solverState.index();
        }

        return result;
    }

    private static SolverState iteration(List<Token> tokenList, int index) throws ArithmeticSolveErrorException {
            double result = 0;

        OperationType operationType = OperationType.None;
        OperationType priorityType = OperationType.None;
        double currentNumber = 0;
        boolean isNegative = false;
        boolean waitForNumber = true;
        boolean firstWriting = true;

        MASTER_LOOP:
        for (; index < tokenList.size();index++) {
            Token currentToken = tokenList.get(index);

            if (currentToken.getType() == Token.Type.Parenthesis) {
                ParenthesisToken parenthesisToken = (ParenthesisToken) currentToken;
                
                switch (parenthesisToken.getValue()) {
                    case Open -> {
                        if (!waitForNumber)
                            priorityType = OperationType.Multiply;
                        
                        SolverState solverState = iteration(tokenList, index+1);
                        index = solverState.index();

                        double mul = 1;

                        if (isNegative) {
                            mul = -1;
                        }

                        if (priorityType != OperationType.None) {
                            if (priorityType == OperationType.Multiply) {
                                currentNumber *= solverState.value();
                            } else if (priorityType == OperationType.Divide) {
                                double divider = solverState.value();
                                if (divider == 0)
                                    throw new ArithmeticSolveErrorException("Divide by zero");
                                currentNumber /= divider;
                            }
                        } else if (operationType == OperationType.Minus) {
                            currentNumber -= solverState.value();
                        } else if (operationType == OperationType.None || operationType == OperationType.Plus) {
                            currentNumber += solverState.value();
                        }
                    }
                    case Close -> {
                        if (waitForNumber) {
                            throw new ArithmeticSolveErrorException("Wait for number, but got ')'");
                        }
                        break MASTER_LOOP;
                    }
                    default -> {
                        throw new ArithmeticSolveErrorException("Inner error in solve method. Parenthesis has None type.≥");
                    }
                }
            } else if (currentToken.getType() == Token.Type.Operation) {
                OperationToken operationToken = (OperationToken) currentToken;

                switch (operationToken.getValue()) {
                    case Plus:
                    case Minus:
                        if (waitForNumber && operationToken.getValue() == OperationType.Minus) {
                            isNegative = !isNegative;
                        }
                        result += currentNumber;
                        firstWriting = false;
                        currentNumber = 0;
                        priorityType = OperationType.None;
                        operationType = operationToken.getValue();
                        break;
                    case Multiply:
                    case Divide:
                        priorityType = operationToken.getValue();
                        break;
                    default:
                        throw new ArithmeticSolveErrorException("Inner solver error: operation time has value equals None");
                }

                waitForNumber = true;
            } else if (currentToken.getType() == Token.Type.Number) {
                NumberToken numberToken = (NumberToken) currentToken;
                double mul = 1;
                if (isNegative) {
                    mul = -1;
                    isNegative = false;
                }

                if (priorityType != OperationType.None) {
                    if (priorityType == OperationType.Divide) {
                        double divider = numberToken.getValue();
                        if (divider == 0)
                            throw new ArithmeticSolveErrorException("Divide by zero");
                        currentNumber /= divider * mul;
                    } else if (priorityType == OperationType.Multiply) {
                        currentNumber *= numberToken.getValue()* mul;
                    }
                } else if (operationType == OperationType.None) {
                    currentNumber += numberToken.getValue() * mul; // must be once
                } else if (operationType == OperationType.Plus) {
                    currentNumber += numberToken.getValue() * mul;
                }else if (operationType == OperationType.Minus) {
                    currentNumber = numberToken.getValue() * -1 * mul;
                }

                waitForNumber = false;
            }
        }

        double mul = 1;

        if (isNegative) {
            mul = -1;
        }

        if (currentNumber != 0) {
            if (firstWriting){
                result = currentNumber * mul;
            } else if (operationType == OperationType.Plus || operationType == OperationType.Minus) {
                result += currentNumber * mul;
            } else if (operationType == OperationType.Divide) {
                if (currentNumber == 0) {
                    throw new ArithmeticSolveErrorException("Divide by zero.");
                }
                result /= currentNumber * mul;
            } else if (operationType == OperationType.Multiply) {
                result *= currentNumber * mul;
            }
        }

        return new SolverState(index, result);
    }

    private record SolverState(int index, double value) {
    }


    public final record SolveResult(String expression, double result) {
    }
}
