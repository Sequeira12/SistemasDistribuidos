package pt.uc.sd.forms;

import java.io.Serializable;

public class TokensParaPesquisa implements Serializable {
    String token;

    public TokensParaPesquisa(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
