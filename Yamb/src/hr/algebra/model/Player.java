/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 *
 * @author lukak
 */
public class Player implements Serializable {
    
    private String nickname;
    private int number;

    public Player() {
    }

    public Player(String nickname, int number) {
        this.nickname = nickname;
        this.number = number;
    }
    

   

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    
    
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(nickname);
        oos.writeInt(number);
                   
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {         
       nickname = ois.readUTF();
        number = ois.readInt();
    }
    
    
}
