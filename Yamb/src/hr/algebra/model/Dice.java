/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import javafx.scene.image.ImageView;

/**
 *
 * @author lukak
 */
public class Dice {
    
    private String name;
    private ImageView image;
    private String imageURL;
    private boolean isClicked;

    public Dice(String name, ImageView image, String imageURL, boolean isClicked) {
        this.name = name;
        this.image = image;
        this.imageURL = imageURL;
        this.isClicked = isClicked;
    }

    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    
    

    public boolean isIsClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }
    
    
}
