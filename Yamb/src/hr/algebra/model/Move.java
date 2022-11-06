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
public class Move implements Serializable{
    
    private String points;
    
    private String labelId;
    
    private Player player;

    public Move(String points, String labelId, Player player) {
        this.points = points;
        this.labelId = labelId;
        this.player = player;
    }

   

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    
    
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(points);
        oos.writeUTF(labelId);
        oos.writeObject(player);
               
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {         
        points = ois.readUTF();
        labelId=ois.readUTF();
        player = (Player) ois.readObject();
        
    }

    @Override
    public String toString() {
        return "Move{" + "points=" + points + ", labelId=" + labelId + ", player=" + player + '}';
    }
    
    
    
    
    
}
