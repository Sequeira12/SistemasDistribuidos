package pt.uc.sd.forms;

import java.io.Serializable;

public class TokensParaPesquisa implements Serializable {
    String token;

    /**
     * constructor for class TokensParaPesquisa
     * @param token that was searched
     */
    public TokensParaPesquisa(String token) {
        this.token = token;
    }

    /**
     * getter for the token
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * setter for the token
     * @param token that was searched
     */
    public void setToken(String token) {
        this.token = token;
    }
}
