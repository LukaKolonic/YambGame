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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author lukak
 */
public class Field implements Serializable {
    
    private final static long serialVersionUID = 2L;
    
    private StringProperty name;
    
    private StringProperty value;
    
    private IntegerProperty points;

    public Field(String name, String value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
    }

    public Field(String name, String value, int points) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
        this.points = new SimpleIntegerProperty(points);
    }
    
    
    
    

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    public int getPoints() {
        return points.get();
    }

    public void setPoints(int points) {
        this.points.set(points);
    }
    
    
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(name.get());
        oos.writeUTF(value.get());
        oos.writeInt(points.get());
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        name = new SimpleStringProperty(ois.readUTF());
        value = new SimpleStringProperty(ois.readUTF());      
        points = new SimpleIntegerProperty(ois.readInt());
        
    }

    @Override
    public String toString() {
        return "Field{" + "name=" + name + ", value=" + value + ", points=" + points + '}';
    }
    
    
    
}
