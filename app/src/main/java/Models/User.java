package Models;

/**
 * Created by Ngoc Khanh on 8/17/2017.
 */

public class User {
    public User(){}

    private String Username;
    private String Status;
    private String Email;
    public User(String username,String email,String status){
        this.Email=email;
        this.Username=username;
        this.Status=status;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
