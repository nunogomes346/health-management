package dtos;

import java.io.Serializable;

public class UserDTO implements Serializable{

    protected String username;
    protected String password;    
    protected String name;
    protected String mail;

    public UserDTO() { }    
    
    public UserDTO(String username, String password, String name, String mail) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.mail = mail;
    }
    
    public void reset() {
        setUsername(null);
        setPassword(null);
        setName(null);
        setMail(null);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
