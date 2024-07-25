
package DataAccessLayer;

import java.io.Serializable;


public class MoveDTO implements Serializable
{
   private String sender_userName ;
   private String reciver_userName;
    
   private int row ;
   private int col ;
    
   private int turn ;

    public MoveDTO() {
    }
   

    public MoveDTO(String sender_userName, String reciver_userName, int row, int col, int turn) {
        this.sender_userName = sender_userName;
        this.reciver_userName = reciver_userName;
        this.row = row;
        this.col = col;
        this.turn = turn;
    }

    public String getSender_userName() {
        return sender_userName;
    }

    public void setSender_userName(String sender_userName) {
        this.sender_userName = sender_userName;
    }

    public String getReciver_userName() {
        return reciver_userName;
    }

    public void setReciver_userName(String reciver_userName) {
        this.reciver_userName = reciver_userName;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
    
    
    
    
    
}
