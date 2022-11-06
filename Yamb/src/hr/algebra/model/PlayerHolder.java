/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

/**
 *
 * @author lukak
 */
public class PlayerHolder {
    
    private Player player;
    private final static PlayerHolder INSTANCE = new PlayerHolder();

    private PlayerHolder() {
    }

    public static PlayerHolder getInstance() {
        return INSTANCE;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
