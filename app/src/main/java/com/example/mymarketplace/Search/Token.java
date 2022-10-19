package com.example.mymarketplace.Search;

import java.util.Objects;

/** The token class that defines what types
 *  of data the user can search for, and is
 *  used by the tokenizer and the searchActivity
 * @author Matthew Cawley
 */
public class Token {
    public enum Type {PNAME, SNAME, CAT, SUBCAT, PRICEMAX, PRICEMIN, NULL}

    public static class IllegalTokenException extends IllegalArgumentException {
        public IllegalTokenException(String errorMessage) {
            super(errorMessage);
        }
    }

    // Fields of the class Token.
    private final String token; // Token representation in String form.
    private final Type type;    // Type of the token.

    public Token(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }

    public String toString(){
        return token + typeToString();
    }

    public String typeToString(){
        if(type == Type.PNAME){
            return "PNAME";
        }else if (type == Type.SNAME){
            return "SNAME";
        }else if (type == Type.CAT){
            return "CAT";
        }else if (type == Type.SUBCAT){
            return "SUBCAT";
        }else if (type == Type.PRICEMAX){
            return "PRICEMAX";
        }else if (type == Type.PRICEMIN){
            return "PRICEMIN";
        }else if (type == Type.NULL){
            return "NULL";
        }
        return null;
    }
}
