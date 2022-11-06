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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

/**
 *
 * @author lukak
 */
public class Game implements Serializable {
    
    private final static long serialVersionUID = 1L;
    
    private ObservableList<Field> labels;

    public ObservableList<Field> getLabels() {
        return labels;
    }

    public void setLabels(ObservableList<Field> labels) {
        this.labels = labels;
    }

       
    
     private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeObject(new ArrayList(labels));
        
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        List<Field> serializedLabels = (List<Field>) ois.readObject();       
        labels = FXCollections.observableArrayList(serializedLabels);
          
    }
    
}
