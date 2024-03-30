package ru.cornivella.discord.math.parser;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ru.cornivella.discord.math.ArithmeticParsingErrorException;
import ru.cornivella.discord.math.parser.tokens.NumberToken;
import ru.cornivella.discord.math.parser.tokens.OperatorToken;
import ru.cornivella.discord.math.parser.tokens.ParenthesisToken;
import ru.cornivella.discord.math.parser.tokens.ParenthesisType;
import ru.cornivella.discord.math.parser.tokens.Token;
import ru.cornivella.discord.math.parser.tokens.TokenTree;
import ru.cornivella.discord.math.parser.tokens.TokenType;

public class Parser {
    private static final Logger logger = LogManager.getLogger(Parser.class);

    public static TokenTree parse(String expresstion) throws ArithmeticParsingErrorException {
        logger.debug("Try to parse '" + expresstion + "'.");

        ParserState parserState = new ParserState(expresstion);
        List<Token> tokenList = parserState.getTokenList();
        logger.debug("Tokens: " + tokenList);
        return null;
    }

    private static final class ParserState {
        private static final String numberSet = "0123456789";
        private static final String operationSet = "-+*/^";
        private static final String functionSet = "qwertyuiopasdfghjklzxcvbnm";

        private boolean nextNumberNegative; // if true next number sets to negative
        private final String expression; // contains full expression to parse
        private StringBuilder word; // contains current parsing word
        private int index; // value with current index of character in expression

        public ParserState(String expression) {
            this.expression = expression;
            this.index = 0;
            this.word = new StringBuilder();
            this.nextNumberNegative = false;
        }

        /*
         * Parses got expression.
         * @return List of tokens of parsed expression.
         * @throws ArithmeticParsingException if while parsing were errors.
         */
        public List<Token> getTokenList() throws ArithmeticParsingErrorException {
            TokenType currentReadingType = TokenType.Number;
            // if equals to parenthesis, means that parenthesis was opened, and if next symbol is operation minus than nextNumberNegative sets to true.
            // if equals to number, means that next token must be operation.
            // if equals to operation, means that next token must be number or parenthesis.
            // my English is very well :)
            TokenType lastRodeType = TokenType.Parenthesis;
            List<Token> tokenList = new LinkedList<>();
            int currentOpenedParenthesis = 0;

            for (;index < expression.length();index++) {
                char ch = expression.charAt(index);

                if (characterContains(numberSet, ch)) {
                    if (currentReadingType != TokenType.Number) {
                        Token parsedToken = parseNextToken(currentReadingType);
                        tokenList.add(parsedToken);

                        currentReadingType = TokenType.Number;
                    }

                    lastRodeType = TokenType.Number;
                    word.append(ch);
                } else if (characterContains("()", ch)) {
                    if (lastRodeType == TokenType.Number) {
                        tokenList.add(parseNextToken(TokenType.Number));
                    }
                    word.append(ch);

                    ParenthesisToken parsedToken = (ParenthesisToken) parseNextToken(TokenType.Parenthesis);
                    if (parsedToken.getValue() == ParenthesisType.Open) {
                        currentOpenedParenthesis++;
                        lastRodeType = TokenType.Operation;
                        currentReadingType = TokenType.Number;
                    } else if (parsedToken.getValue() == ParenthesisType.Close) {
                        currentOpenedParenthesis--;
                        if (currentOpenedParenthesis < 0) {
                            throw new ArithmeticParsingErrorException("excess close parenthesis!", getTrace());
                        }

                        lastRodeType = TokenType.Parenthesis;
                        currentReadingType = TokenType.Operation;
                    }
                    tokenList.add(parsedToken);
                } else if (characterContains(operationSet, ch)) {
                    if (lastRodeType == TokenType.Operation) {
                        if (ch != '-') {
                            throw new ArithmeticParsingErrorException("unnecessary operation value '" + ch + "'", getTrace());
                        }
                        nextNumberNegative = true;
                    } else {
                        if (lastRodeType != TokenType.Parenthesis)
                            tokenList.add(parseNextToken(currentReadingType));
                        word.append(ch);
                        tokenList.add(parseNextToken(TokenType.Operation));

                        lastRodeType = TokenType.Operation;

                        currentReadingType = TokenType.Number;
                    }
                } else if (characterContains(functionSet, ch)) {
                    if (currentReadingType != TokenType.Function) {
                        Token parsedToken = parseNextToken(currentReadingType);
                        tokenList.add(parsedToken);

                        lastRodeType = parsedToken.getType();

                        currentReadingType = TokenType.Function;
                    }
                    word.append(ch);
                } else if (ch == ' ') {
                    // just ignore
                } else {
                    throw new ArithmeticParsingErrorException("Unknown symbol '" + ch + "'", getTrace());
                }
            }

            if (word.length() != 0) {
                tokenList.add(parseNextToken(currentReadingType));
            }

            if (currentOpenedParenthesis != 0) {
                throw new ArithmeticParsingErrorException("not all parenthesis closed!", getTrace());
            }
            return tokenList;
        }

        /*
         * Parse next token.
         * @param type contains token type, which will be parsed. If it is not this type, throws exception.
         */
        private final Token parseNextToken(TokenType type) throws ArithmeticParsingErrorException {
            String value = word.toString(); // get value to clear StringBuilder
            word = new StringBuilder(); // clears string builder.

            if (value.isEmpty()) {
                throw new ArithmeticParsingErrorException("value is empty!", getTrace());
            }

            switch (type) {
                case Number -> {
                    boolean isNegative = nextNumberNegative;
                    if (charactersContainsIn(numberSet, value)) {
                        nextNumberNegative = false;
                        return NumberToken.parse(value, getTrace(), isNegative);
                    }
                    
                    throw new ArithmeticParsingErrorException(value + " is not a number!", getTrace());
                }
                case Operation -> {
                    if (charactersContainsIn(operationSet, value))
                        return OperatorToken.parse(value, getTrace());
                }
                case Function -> {
                    // if (charactersContainsIn(functionSet, value))
                    //     return FunctionToken.parse(value, getTrace());
                    throw new ArithmeticParsingErrorException("math functions not supported yet.", getTrace());
                }
                case Parenthesis -> {
                    if (charactersContainsIn("()", value)) {
                        boolean isNegative = false;

                        if (value.toString().equals("(")) {
                            isNegative = nextNumberNegative;
                            nextNumberNegative = false;
                        }

                        return ParenthesisToken.parse(value, getTrace(), isNegative);
                    }
                }
                default -> {
                    throw new ArithmeticParsingErrorException("can't find " + type + " type parser.", getTrace());
                }
            }
            return null; // can't be reached.
        }

        /*
         * @return trace to know where is misspell.
         */
        private final String getTrace() {
            return "At " + index + " of '" + expression + "': ";
        }

        /*
         * @returns If ch contains in set, returns true
         */
        private static final boolean characterContains(String set, char ch) {
            return set.indexOf(ch) != -1;
        }

        /*
         * @return If all characters contains in set, returns true
         * @todo Maybe I should remade it
         */
        private static final boolean charactersContainsIn(String set, String characters) {
            for (char ch : characters.toCharArray()) {
                if (!characterContains(set, ch)) {
                    return false;
                }
            }

            return true;
        }
    }
}
