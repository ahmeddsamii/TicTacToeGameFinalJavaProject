package DataAccessLayer;

import java.io.Serializable;

public class RequestDTO implements Serializable {

    String sender_username;
    String reciver_username;
    String senderIp;
    String receiverIp;
    int ScreenIndicator;

    public RequestDTO() {
    }

    public RequestDTO(String sender_username, String reciver_username, int ScreenIndicator) {
        this.sender_username = sender_username;
        this.reciver_username = reciver_username;
        this.ScreenIndicator = ScreenIndicator;
    }

    public RequestDTO(String sender_username, String reciver_username, String senderIp, int ScreenIndicator, String receiverIp) {
        this.sender_username = sender_username;
        this.reciver_username = reciver_username;
        this.senderIp = senderIp;
        this.receiverIp = receiverIp;
        this.ScreenIndicator = ScreenIndicator;
    }

    public String getReceiverIp() {
        return receiverIp;
    }

    public void setReceiverIp(String receiverIp) {
        this.receiverIp = receiverIp;
    }

    public String getSender_username() {
        return sender_username;
    }

    public void setSender_username(String sender_username) {
        this.sender_username = sender_username;
    }

    public String getReciver_username() {
        return reciver_username;
    }

    public void setReciver_username(String reciver_username) {
        this.reciver_username = reciver_username;
    }

    public String getsenderIp() {
        return senderIp;
    }

    public void setsenderIp(String senderIp) {
        this.senderIp = senderIp;
    }

    public int getScreenIndicator() {
        return ScreenIndicator;
    }

    public void setScreenIndicator(int ScreenIndicator) {
        this.ScreenIndicator = ScreenIndicator;
    }
}
