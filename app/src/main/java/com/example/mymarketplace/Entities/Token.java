package com.example.mymarketplace.Entities;

import java.util.Objects;

/** The token class that defines what types
 *  of data the user can search for, and is
 *  used by the tokenizer and the searchActivity
 * @author Matthew Cawley
 */
public class Token {
    // The following enum defines different types of tokens. Example of accessing these: Token.Type.INT
    public enum Type {PNAME, SNAME, CAT, SUBCAT, PRICEMAX, PRICEMIN}

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
}
