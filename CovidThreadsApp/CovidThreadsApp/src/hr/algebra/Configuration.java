/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

/**
 *
 * @author lukak
 */
public class Configuration {
    
    public static final String PUTANJA_SLIKE_STATION= "src/hr/algebra/slike/station.png";
    public static final Image IMAGE_STATION;
    
    public static final String PUTANJA_SLIKE_OSOBA = "src/hr/algebra/slike/person.png";
    public static final Image IMAGE_OSOBA;
    
    public static final String PUTANJA_SLIKE_DOLAZAK = "src/hr/algebra/slike/truck.png"; 
    public static final Image IMAGE_DOLAZAK;
    
    
    public static final int KAPACITET_AMBULANTE = 3;    
    public static final int MAX_BR_LJUDI_U_REDU = 7;   
    public static final int MAX_VRIJEME_CEKANJA = 800;
     public static final int MAX_GUZVA = 1000;
    public static final int MAX_VRIJEME_CEKANJA_TESTOVA = 500;

    public static boolean isTestiranjeUTijeku = false;   
    public static boolean isDolazak = false;         
    public static int brLjudiURedu = 0;
    public static int brMjestaUAmbulanti = KAPACITET_AMBULANTE;  
       
    
     static {
        FileInputStream imgStation = null;
        try {
            imgStation = new FileInputStream(new File(PUTANJA_SLIKE_STATION));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        IMAGE_STATION = new Image(imgStation);
        
        FileInputStream slikaOsoba = null;
        try {
            slikaOsoba = new FileInputStream(
                    PUTANJA_SLIKE_OSOBA);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        IMAGE_OSOBA = new Image(slikaOsoba);
        
        FileInputStream slikaDolazak = null;
        try {
            slikaDolazak = new FileInputStream(
                    PUTANJA_SLIKE_DOLAZAK);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        IMAGE_DOLAZAK = new Image(slikaDolazak);
        
        
     }
    
}
