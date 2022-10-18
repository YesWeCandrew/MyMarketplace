package com.example.mymarketplace.Search;

import java.util.ArrayList;

/** This class takes a string search input and
 *  turn it into a list of tokens
 *  @author Matthew Cawley
 */

public class Tokenizer {
    private String buffer;      // String to be transformed into tokens each time next() is called.
    public ArrayList<Token> tokens;

    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    public Tokenizer(String input) {
        buffer = input;          // save input text (string)
        ArrayList<Token> tokens = new ArrayList<Token>();
        next();
    }

    public ArrayList<Token> getTokens (){
        return tokens;
    }

    public void next() {
        Token currentToken;
        String tokenType = "";
        String tokenValue = "";
        int tokenLength = 0;
        boolean isLastToken = false;

        for (int i = 0; i < 1000000; i++){ //first extract the part before the ':' to get the token type
            int remainingChars = buffer.length();
            if(i >= remainingChars){
                throw new IllegalTokenException("could not find next colon");
            }
            if(buffer.charAt(i) == ':'){
                tokenType = buffer.substring(0, i-1); //i-1 to remove the :
                for (int j = i+1; j < 1000000; j++){ //then extract the part before the + (or the end) to get the value
                    if(j == remainingChars){
                        tokenValue = buffer.substring(i+1, j);
                        tokenLength = j;
                        isLastToken = true;
                        break;
                    }else if(buffer.charAt(j) == '+'){
                        tokenValue = buffer.substring(i+1, j);
                        tokenLength = j;
                        break;
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
        buffer = buffer.substring(tokenLength+1);
        if(!isLastToken){
            next();
        }
    }
}
