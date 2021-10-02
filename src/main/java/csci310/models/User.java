package csci310.models;

import org.bson.Document;

public class User {
    private String username;
    private String psw;

    public User(String username, String psw) {
        this.username = username;
        this.psw = psw;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getUsername() {
        return username;
    }

    public String getPsw() {
        return psw;
    }

    public final Document toDocument() {
        return new Document("username", getUsername())
                .append("password", getPsw());
    }

}
