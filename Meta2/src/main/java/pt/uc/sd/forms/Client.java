package pt.uc.sd.forms;

import java.io.Serializable;


public class Client implements Serializable {
    private String username;
    private String password;


    /**
     * Empty constuctor for class Client
     */
    public Client() {
    }

    /**
     * constructor for the client class
     * @param username of the client
     */
    public Client(String username) {
        this.username = username;
    }

    /**
     * constructor for the client class
     * @param username of the client
     * @param password of the client
     */
    public Client(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * getter for username
     * @return username of the client
     */
    public String getUsername() {
        return username;
    }

    /**
     * setter for username
     * @param username of the client
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * getter for password
     * @return password of the client
     */
    public String getPassword() {
        return password;
    }

    /**
     * setter for username
     * @param password of the client
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
