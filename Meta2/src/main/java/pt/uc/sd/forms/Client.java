package pt.uc.sd.forms;

import java.io.Serializable;

public class Client implements Serializable {
    private String username;
    private String password;

    public Client() {
    }
    public Client(String username) {
        this.username = username;
    }
    public Client(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
