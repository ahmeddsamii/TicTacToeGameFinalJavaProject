package DataAccessLayer;

import java.io.Serializable;

public class PlayerDTO implements Serializable {
      private String username;
    private int ScreenIndicator;
    private String password;
    private String email;
    private String ip;
    private String status;

    public PlayerDTO() {
    }

    public PlayerDTO(String username, int ScreenIndicator, String password, String email, int screenNum, String ip,String status) {
        this.username = username;
        this.ScreenIndicator = ScreenIndicator;
        this.password = password;
        this.email = email;
        this.ip = ip;
        this.status = status;
    }

    public String getIp() {
        return ip;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScreenIndicator() {
        return ScreenIndicator;
    }

    public void setScreenIndicator(int score) {
        this.ScreenIndicator = score;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
