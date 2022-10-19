package com.example.mymarketplace.Search;

import java.util.ArrayList;
import java.util.Scanner;

/** This class takes a string search input and
 *  turn it into a list of tokens
 *  @author Matthew Cawley
 */

public class Tokenizer {
    private String buffer;      // String to be transformed into tokens each time next() is called.
    public ArrayList<Token> tokens;

    public Tokenizer() {
        buffer = "";
        tokens = new ArrayList<Token>();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        Tokenizer tokenizer = new Tokenizer();
        ArrayList<Token> output = tokenizer.Tokenize(input);
        for(Token t : output){
            System.out.println(t.toString());
        }
    }

    public ArrayList<Token> Tokenize (String input){
        buffer = input;
        next();
        return tokens;
    }

    public void next() {
        Token currentToken = null;
        String tokenType = "";
        String tokenValue = "";
        int tokenLength = 0;
        boolean isLastToken = false;
        boolean finished = false;

        for (int i = 0; i < 1000000; i++){ //first extract the part before the ':' to get the token type
            int remainingChars = buffer.length();
            if(i >= remainingChars){
                tokens.clear();
                tokens.add(new Token ("noColonError", Token.Type.NULL));
                return;
            }
            if(buffer.charAt(i) == ':'){
                if(i == 0){
                    tokens.clear();
                    tokens.add(new Token ("misplacedColonError", Token.Type.NULL));
                    return;
                }
                tokenType = buffer.substring(0, i); //i-1 to remove the :
                for (int j = i+1; j < 1000000; j++){ //then extract the part before the + (or the end) to get the value
                    if(j == remainingChars){
                        tokenValue = buffer.substring(i+1, j);
                        tokenLength = j;
                        isLastToken = true;
                        finished = true;
                        break;
                    }else if(buffer.charAt(j) == '+'){
                        tokenValue = buffer.substring(i+1, j);
                        tokenLength = j;
                        finished = true;
                        break;
                    }
                }
                if(finished) {break;}
            }
        }

        if(tokenType.equals("pname")){
            currentToken = new Token(tokenValue, Token.Type.PNAME);
        }else if(tokenType.equals("sname")){
            currentToken = new Token(tokenValue, Token.Type.SNAME);
        }else if (tokenType.equals("cat")){
            currentToken = new Token(tokenValue, Token.Type.CAT);
        }else if (tokenType.equals("subcat")){
            currentToken = new Token(tokenValue, Token.Type.SUBCAT);
        }else if (tokenType.equals("max")){
            currentToken = new Token(tokenValue, Token.Type.PRICEMAX);
        }else if (tokenType.equals("min")) {
            currentToken = new Token(tokenValue, Token.Type.PRICEMIN);
        }else {
            tokens.clear();
            tokens.add(new Token ("incorrectSearchType", Token.Type.NULL));
            return;
        }
        tokens.add(currentToken);

        if(!isLastToken){
            buffer = buffer.substring(tokenLength+1);
            next();
        }
    }
}

