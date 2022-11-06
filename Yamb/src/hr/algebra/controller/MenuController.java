/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.Yamb;
import hr.algebra.model.Player;
import hr.algebra.model.PlayerHolder;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author lukak
 */
public class MenuController implements Initializable {

    @FXML
    private TextField tfNickname;
    @FXML
    private Button btnPlay;
    
    private Player player;
    
    private boolean isFirst;
    
    Random r = new Random();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player = new Player();
    }    

    @FXML
    private void goToGame(ActionEvent event) {
        player.setNickname(tfNickname.getText().trim()); 
        player.setNumber(r.nextInt(50));
        
        try {
            PlayerHolder playerHolder = PlayerHolder.getInstance();
            playerHolder.setPlayer(player);
            
            Parent root = FXMLLoader.load(getClass().getResource("/hr/algebra/view/Main.fxml"));  
            
            Scene scene = new Scene(root, 1100, 800);
            Stage stage = new Stage();
            stage.setTitle("Yamb");
            stage.setScene(scene);
            stage.show();
            
            ((Stage) btnPlay.getScene().getWindow()).close();
        } catch (IOException ex) {
            Logger.getLogger(Yamb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handleBtnPlay(KeyEvent event) {
        btnPlay.setDisable(tfNickname.getText().trim().isEmpty());
    }
    
}
