/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

    public Field(String name, String value) {
        this.name = new SimpleStringProperty(name);
        this.value = new SimpleStringProperty(value);
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
    
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.writeUTF(name.get());
        oos.writeUTF(value.get());
    }

    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        name = new SimpleStringProperty(ois.readUTF());
        value = new SimpleStringProperty(ois.readUTF());      
        
    }
    
}
