/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.models;

/**
 *
 * @author lukak
 */
public class Move {
    
    private String points;
    
    private String labelId;

    public Move(String points, String labelId) {
        this.points = points;
        this.labelId = labelId;
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
    
    
    
}
