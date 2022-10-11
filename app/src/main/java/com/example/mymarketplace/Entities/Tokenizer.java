package com.example.mymarketplace.Entities;

import java.util.ArrayList;

/** This class takes a string search input and
 *  turn it into a list of tokens
 *  @author Matthew Cawley
 */

public class Tokenizer {
    private String buffer;          // String to be transformed into tokens each time next() is called.
    public ArrayList<Token> tokens;

    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    public Tokenizer(String input) {
        buffer = input;          // save input text (string)
        next();                 // extracts the first token.
    }

    public void next() {
        Token currentToken;
        if (buffer.isEmpty()) {
            return;
        }
        String tokenType = "";
        String tokenValue = "";
        int tokenLength = 0;

        for (int i = 0; i < 1000000; i++){ //first extract the part before the ':' to get the token type
            int remainingChars = buffer.length();
            if(i >= remainingChars){
                throw new IllegalTokenException("could not find next colon");
            }
            if(buffer.charAt(i) == ':'){
                tokenType = buffer.substring(0, i-1); //i-1 to remove the :
                for (int j = i+1; true; j++){ //then extract the part before the + (or the end) to get the value
                    if(j>= remainingChars || buffer.charAt(j) == '+'){
                        tokenValue = buffer.substring(i, j-1); //j-1 to remove the +
                        tokenLength = j;
                    }
                }
            }
        }

        if(tokenType == "pname"){
            currentToken = new Token(tokenValue, Token.Type.PNAME);
        }else if(tokenType == "sname"){
            currentToken = new Token(tokenValue, Token.Type.SNAME);
        }else if (tokenType == "cat"){
            currentToken = new Token(tokenValue, Token.Type.CAT);
        }else if (tokenType == "subcat"){
            currentToken = new Token(tokenValue, Token.Type.SUBCAT);
        }else if (tokenType == "max"){
            currentToken = new Token(tokenValue, Token.Type.PRICEMAX);
        }else if (tokenType == "min") {
            currentToken = new Token(tokenValue, Token.Type.PRICEMIN);
        }else {
            throw new IllegalTokenException("incorrect search type");
        }

        tokens.add(currentToken);
        // Remove the extracted token from buffer
        buffer = buffer.substring(tokenLength);
        next();
    }
}
