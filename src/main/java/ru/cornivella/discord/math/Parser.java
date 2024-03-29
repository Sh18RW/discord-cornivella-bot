package ru.cornivella.discord.parser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.cornivella.discord.parser.tokens.NumberToken;
import ru.cornivella.discord.parser.tokens.OperationToken;
import ru.cornivella.discord.parser.tokens.OperationToken.OperationType;
import ru.cornivella.discord.parser.tokens.ParenthesisToken;
import ru.cornivella.discord.parser.tokens.ParenthesisToken.ParenthesisType;
import ru.cornivella.discord.parser.tokens.Token;

public class Parser {
    private static final Logger logger = LogManager.getLogger(Parser.class);

    public static List<Token> parse(String expression) throws ArithmeticParsingErrorException {
        logger.debug("Try parse '" + expression + "'.");
        List<Token> tokens = new LinkedList<>();

        ParserState parserState = new ParserState(expression, tokens);
        parserState.process();

        return tokens;
    }

    private static class ParserState {
        private static final Set<Character> numberSet = new HashSet<>(List.of('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'));
        private static final Set<Character> operationSet = new HashSet<>(List.of('+', '-', '*', '/'));
        private static final Set<Character> parenthesisSet = new HashSet<>(List.of('(', ')'));

        private final String expression;
        private final List<Token> tokens;

        private int index;
        private int openedBrackets;
        private Token.Type readingType;
        private StringBuilder currentTypeValue;

        public ParserState(final String expression, final List<Token> tokens) {
            this.expression = expression.replaceAll("\s+", "\s")
                .replaceAll("^\s*", "")
                .replaceAll("\s*$", "");
            index = 0;
            openedBrackets = 0;
            this.tokens = tokens;
            readingType = Token.Type.Number;
            currentTypeValue = new StringBuilder();

            logger.debug("Final expression is '" + expression + "'.");
        }

        public void process() throws ArithmeticParsingErrorException {
            if (index >= expression.length()) {
                if (currentTypeValue.length() != 0) {
                    try {
                        tokens.add(parseDouble());
                    } catch (ArithmeticParsingErrorException e) {
                        tokens.add(parseParenthesis());
                    }
                }

                if (openedBrackets != 0) {
                    throw new ArithmeticParsingErrorException(getTrace() + "you haven't been closed brackets!");
                }

                return;
            }

            char ch = expression.charAt(index);
            if (ch == ' ') {
                parseNext();
            } else if (numberSet.contains(ch)) {
                if (readingType != Token.Type.Number) {
                    parseNext();
                }

                readingType = Token.Type.Number;
                currentTypeValue.append(ch);
            } else if (operationSet.contains(ch)) {
                if (currentTypeValue.length() != 0 && readingType != Token.Type.Operation) {
                    parseNext();
                }

                readingType = Token.Type.Operation;
                currentTypeValue.append(ch);
            } else if (parenthesisSet.contains(ch)) {
                if (currentTypeValue.length() != 0)
                    parseNext();
                readingType = Token.Type.Parenthesis;
                currentTypeValue.append(ch);
            }

            index++;
            process();
        }

        public String getTrace() {
            return (index + 1) + " symbol, waiting for " + readingType.toString() + ", value equal '" + currentTypeValue.toString() + "': ";
        }

        private void parseNext() throws ArithmeticParsingErrorException {
            switch (readingType) {
                case Number -> { tokens.add(parseDouble()); }
                case Operation -> { tokens.add(parseOperation()); }
                case Parenthesis -> { tokens.add(parseParenthesis()); }
                case None -> {}
            }
        }

        private NumberToken parseDouble() throws ArithmeticParsingErrorException {
            try {
                double value = Double.parseDouble(currentTypeValue.toString());
                currentTypeValue = new StringBuilder();
                readingType = Token.Type.Operation;
                return new NumberToken(value);
            } catch (NumberFormatException e) {
                throw new ArithmeticParsingErrorException(getTrace() + "can't parse number!");
            }
        }

        private OperationToken parseOperation() throws ArithmeticParsingErrorException {
            String value = currentTypeValue.toString();
            currentTypeValue = new StringBuilder();
            readingType = Token.Type.None;
            switch (value) {
                case "+" -> {
                    return new OperationToken(OperationType.Plus);
                }
                case "-" -> {
                    return new OperationToken(OperationType.Minus);
                }
                case "*" -> {
                    return new OperationToken(OperationType.Multiply);
                }
                case "/" -> {
                    return new OperationToken(OperationType.Divide);
                }
                default -> {
                    throw new ArithmeticParsingErrorException(getTrace() + "in operations dictionary doesn't present '" + currentTypeValue.toString() + "'!");
                }
            }
        }

        private ParenthesisToken parseParenthesis() throws ArithmeticParsingErrorException {
            String value = currentTypeValue.toString();
            currentTypeValue = new StringBuilder();
            readingType = Token.Type.Number;
            switch (value) {
                case "(" -> {
                    readingType = Token.Type.Number;
                    return new ParenthesisToken(ParenthesisType.Open);
                }
                case ")" -> {
                    readingType = Token.Type.Operation;
                    return new ParenthesisToken(ParenthesisType.Close);
                }
                default -> {
                    throw new ArithmeticParsingErrorException(getTrace() + " in parenthesis dictionary doesn't present '" + currentTypeValue.toString() + "'!");
                }
            }
        }
    }

    public static final record ParsingResult(String expression, List<Token> tokenList) {
    }
}
