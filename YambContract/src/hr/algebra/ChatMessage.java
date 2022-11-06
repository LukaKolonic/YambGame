/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author lukak
 */
public class ChatMessage implements Serializable{
    
    private String player;
    private String message;
    private LocalDateTime time;

    public ChatMessage(String username, String message, LocalDateTime time) {
        this.player = username;
        this.message = message;
        this.time = time;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return  " [" + (time.getHour() < 10 ? "0"+time.getHour() : time.getHour()) + ":" + (time.getMinute()< 10 ? "0"+time.getMinute() : time.getMinute()) + "]\n" + player + ": " + message;
    }
    
}
