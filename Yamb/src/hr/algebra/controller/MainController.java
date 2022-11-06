/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;


import hr.algebra.MessengerService;
import hr.algebra.Yamb;
import hr.algebra.model.Dice;
import hr.algebra.model.Field;
import hr.algebra.model.Game;
import hr.algebra.model.Move;
import hr.algebra.model.Player;
import hr.algebra.model.PlayerHolder;
import hr.algebra.threads.ClientThread;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utilities.DOMUtils;
import utilities.MessageUtils;
import utilities.ReflectionUtils;
import utilities.SerializationUtils;

/**
 * FXML Controller class
 *
 * @author lukak
 */
public class MainController implements Initializable {

    @FXML
    private ImageView dice1;
    @FXML
    private ImageView dice2;
    @FXML
    private ImageView dice3;
    @FXML
    private ImageView dice4;
    @FXML
    private ImageView dice5;
    @FXML
    private Button btnRoll;
    
   
    @FXML
    private Label lbJ2;
    @FXML
    private Label lbJ1;
    @FXML
    private Label lbC1;
    @FXML
    private Label lbT2;
    @FXML
    private Label lbT1;
    @FXML
    private Label lbD2;
    @FXML
    private Label lbD1;
    @FXML
    private Label lbTris2;
    @FXML
    private Label lbTris1;
    @FXML
    private Label lbS2;
    @FXML
    private Label lbS1;
    @FXML
    private Label lbP2;
    @FXML
    private Label lbP1;
    @FXML
    private Label lbC2;
    @FXML
    private Label lbZbroj2;
    @FXML
    private Label lbZbroj1;
    @FXML
    private Label lbPoker2;
    @FXML
    private Label lbJamb1;
    @FXML
    private Label lbJamb2;
    @FXML
    private Label lbPoker1;
    
    @FXML
    private Button btnJedinice;
    @FXML
    private Button btnDvojke;
    @FXML
    private Button btnPoker;
    @FXML
    private Button btnTris;
    @FXML
    private Button btnŠestice;
    @FXML
    private Button btnPetice;
    @FXML
    private Button btnČetvorke;
    @FXML
    private Button btnTrojke;
    @FXML
    private Button btnJamb;
    @FXML
    private TextField tfChat;
    @FXML
    private TextArea taChat;
   
   
    private final String URL = ".\\slike\\dice";
   
    
     ObservableList<Dice> dices;
     ObservableList<Dice> clickedDices;
     ObservableList<Button> clickedButtons;
     
     Dice d1;
     Dice d2;
     Dice d3;
     Dice d4;
     Dice d5;
    
    Random random = new Random();
    
    private final File file = new File("serialization.ser");
    ObservableList<Field> gameLabels = FXCollections.observableArrayList();
    Game game = new Game();
    List<Field> allLabels = new ArrayList<>();
    List<Label> allLabelss = new ArrayList<>();

    private static final String DOC_FILENAME = "documentation.html";
    private static final String PACKAGE_LOCATION = ".\\src\\hr\\algebra";
    private static final String CLASSES_PACKAGE = PACKAGE_LOCATION.substring(PACKAGE_LOCATION.indexOf("c") + 2).replace("\\", ".").concat(".");

    boolean deserilazation = false;
    
    int counter = 0;
   
    private ClientThread clientThread;
    
    private Player player;
    private int playerNumber;
    
    private List<Move> moves;
    
    private List<Field> DOMSteps;
    private List<Field> loadedDOMSteps;
    @FXML
    private Button btnNext;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clickedDices = FXCollections.observableArrayList();
        dices = FXCollections.observableArrayList();
        initializeDices();
               
        clickedButtons= FXCollections.observableArrayList();
        clickedButtons.addAll(btnJedinice,btnDvojke,btnTrojke,btnČetvorke,btnPetice,btnŠestice,btnTris,btnPoker,btnJamb);
        moves = new ArrayList<>();
        DOMSteps = new ArrayList<>();
        loadedDOMSteps= new ArrayList<>();
        setUpListeners();  
        
        initLabels();
        
        PlayerHolder ph = PlayerHolder.getInstance();
        player = ph.getPlayer();
        playerNumber = player.getNumber();
       
        initializeChat();
               
        clientThread = new ClientThread("Klijent", MainController.this, player);
        clientThread.setDaemon(true);
        clientThread.start();
        
        
    }

    @FXML
    private void rollDices() {
           
        if (counter <3) {
                 
            btnRoll.setDisable(true);
            
            for (Dice dice : dices) {
                dice.getImage().setDisable(false);
            }

            Thread thread = new Thread() {
                public void run() {
                    
                    try {
                        for (int i = 0; i < dices.size(); i++) {

                            File file = new File(URL + (random.nextInt(6) + 1) + ".png");
                            dices.get(i).setImageURL(file.toURI().toString());
                            dices.get(i).getImage().setImage(new Image(dices.get(i).getImageURL()));

                            Thread.sleep(20);
                        }

                        btnRoll.setDisable(false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };

            thread.start();
            counter++;
        }
        
      
    
    }

    private void setUpListeners() {
        
        for (Dice dice : dices) {
            
        
         dice.getImage().setOnMouseClicked(new EventHandler() {

            @Override
            public void handle(Event event) {  
               
                if (!dice.isIsClicked()) {
                    dice.getImage().setFitWidth(75);
                    int index = dices.indexOf(dice);
                    Dice removed = dices.remove(index);
                                           
                    clickedDices.add(dice);
                    dice.setIsClicked(true);
                }
                else if(dice.isIsClicked()) {
                    dice.getImage().setFitWidth(100);
                    boolean remove = clickedDices.remove(dice);
                    dices.add(dice);
                    dice.setIsClicked(false);
                }
                
               
                }
        });
         
          
           
        }
        
        for (Button button : clickedButtons) {
            
            button.setOnMouseClicked(new EventHandler() {
                @Override
                public void handle(Event event) {
                   dices.clear();
                   clickedDices.clear();
                   dices.add(d1);
                   dices.add(d2);
                   dices.add(d3);
                   dices.add(d4);
                   dices.add(d5);
                   
                   dice1.setImage(d1.getImage().getImage());
                   dice2.setImage(d2.getImage().getImage());
                   dice3.setImage(d3.getImage().getImage());
                   dice4.setImage(d4.getImage().getImage());
                   dice5.setImage(d5.getImage().getImage());
                  
                 
                                       
                    for (Dice dice : dices) {
                        
                       dice.getImage().setFitWidth(100);
                       dice.setIsClicked(false);
                       dice.getImage().setDisable(true);
                       
                                                 
                    }
                    counter = 0;

                    
                    List<Button> left = new ArrayList<>();
                    for (Button cb : clickedButtons) {
                        if (!cb.isDisabled()) {
                            left.add(cb);
                        }
                    }

                    if (left.size() == 0) {
                        System.out.println("GOTOV PLAYER" + player.getNumber());
                        ClientThread ct = new ClientThread("Klijent1", MainController.this, player);
                        ct.trigger(moves);
                        ct.run();
                        saveDOM();
                    }

                }

            });
        }

    }

    
   
    @FXML
    private void countJedinice(ActionEvent event) {
      
        if (player.getNumber() == playerNumber) {

            if (lbJ1.getText().isEmpty()) {

                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("1")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("1")) {
                        sum++;
                    }
                }
                lbJ1.setText(String.valueOf(sum));
                btnJedinice.setDisable(true);

                Move m = new Move(String.valueOf(sum), lbJ2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbJ1", lbJ1.getId(), sum));
                sumAll(sum);
                gameLabels.add(new Field("lbJ1", lbJ1.getText(), sum));
                serializeGame();              
                
            }
            else {
                if (lbJ2.getText().isEmpty()) {

                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("1")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("1")) {
                        sum++;
                    }
                }
                lbJ2.setText(String.valueOf(sum));
                btnJedinice.setDisable(true);
                 Move m = new Move(String.valueOf(sum), lbJ2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbJ2", lbJ2.getId(), sum));
                sumAll2(sum);
                gameLabels.add(new Field("lbJ2", lbJ2.getText(), sum));
                serializeGame();
               
            }
        
        }}
        
        
    }

    @FXML
    private void countDvojke(ActionEvent event) {
        
        if (player.getNumber() == playerNumber) {

            if (lbD1.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("2")) {
                        sum++;
                    }

                }
                String substring = "";
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("2")) {
                        sum++;
                    }
                }
                int result = sum * 2;
                lbD1.setText(String.valueOf(result));
                btnDvojke.setDisable(true);
                Move m = new Move(String.valueOf(result), lbD2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbD1", lbD1.getId(), result));
                sumAll(result);
                gameLabels.add(new Field("lbD1", lbD1.getText(), result));
                serializeGame();
                
            }
        } else {
            if (lbD2.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("2")) {
                        sum++;
                    }

                }
                String substring = "";
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("2")) {
                        sum++;
                    }
                }
                int result = sum * 2;
                lbD2.setText(String.valueOf(result));
                btnDvojke.setDisable(true);
                Move m = new Move(String.valueOf(result), lbD2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbD2", lbD2.getId(), result));
                sumAll2(result);
                gameLabels.add(new Field("lbD2", lbD2.getText(), result));
                serializeGame(); 
                
            }
        }
    
    }

    @FXML
    private void countTrojke(ActionEvent event) {
       
        
        if (player.getNumber() == playerNumber) {

            if (lbT1.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("3")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("3")) {
                        sum++;
                    }
                }
                int result = sum * 3;
                lbT1.setText(String.valueOf(result));
                btnTrojke.setDisable(true);
                Move m = new Move(String.valueOf(result), lbT2.getId(), player);
                moves.add(m);   
                DOMSteps.add(new Field("lbT1", lbT1.getId(), result));
                sumAll(result);
                gameLabels.add(new Field("lbT1", lbT1.getText(), result));
                serializeGame();
                
            }
        } else {
            if (lbT2.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("3")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("3")) {
                        sum++;
                    }
                }
                int result = sum * 3;
                lbT2.setText(String.valueOf(result));
                btnTrojke.setDisable(true);
                Move m = new Move(String.valueOf(result), lbT2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbT2", lbT2.getId(), result));
                sumAll2(result);
                gameLabels.add(new Field("lbT2", lbT2.getText(), result));
                serializeGame();
                
            }
        }
        
        
    }

    @FXML
    private void countCetvorke(ActionEvent event) {
         if (player.getNumber() == playerNumber) {

            if (lbC1.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("4")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("4")) {
                        sum++;
                    }
                }
                int result = sum * 4;
                lbC1.setText(String.valueOf(result));
                btnČetvorke.setDisable(true);
                 Move m = new Move(String.valueOf(result), lbC2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbC1", lbC1.getId(), result));
                sumAll(result);
                gameLabels.add(new Field("lbC1", lbC1.getText(), result));
                serializeGame();
               
            }
        } else {
              if (lbC2.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("4")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("4")) {
                        sum++;
                    }
                }
                int result = sum * 4;
                lbC2.setText(String.valueOf(result));
                btnČetvorke.setDisable(true);
                 Move m = new Move(String.valueOf(result), lbC2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbC2", lbC2.getId(), result));
                sumAll2(result);
                gameLabels.add(new Field("lbC2", lbC2.getText(), result));
                serializeGame();
               
         }
         }
    }

    @FXML
    private void countPetice(ActionEvent event) {
        if (player.getNumber() == playerNumber) {

            if (lbP1.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("5")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("5")) {
                        sum++;
                    }
                }
                int result = sum * 5;
                lbP1.setText(String.valueOf(result));
                btnPetice.setDisable(true);
                Move m = new Move(String.valueOf(result), lbP2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbP1", lbP1.getId(), result));
                sumAll(result);
                gameLabels.add(new Field("lbP1", lbP1.getText(), result));
                serializeGame();
                

            }
        }else {
             if (lbP2.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("5")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("5")) {
                        sum++;
                    }
                }
                int result = sum * 5;
                lbP2.setText(String.valueOf(result));
                btnPetice.setDisable(true);
                Move m = new Move(String.valueOf(result), lbP2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbP2", lbP2.getId(), result));
                sumAll2(result);
                gameLabels.add(new Field("lbP2", lbP2.getText(), result));
                serializeGame();
                

            }
        }
    }

    @FXML
    private void countSestice(ActionEvent event) {
        if (player.getNumber() == playerNumber) {

            if (lbS1.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("6")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("6")) {
                        sum++;
                    }
                }
                int result = sum * 6;
                lbS1.setText(String.valueOf(result));
                btnŠestice.setDisable(true);
                Move m = new Move(String.valueOf(result), lbS2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbS1", lbS1.getId(), result));
                sumAll(result);
                gameLabels.add(new Field("lbS1", lbS1.getText(), result));
                serializeGame();
                
            }
        } else {
             if (lbS2.getText().isEmpty()) {
                int sum = 0;
                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    if (substring.equals("6")) {
                        sum++;
                    }

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.'));
                    System.out.println(substring);
                    if (substring.equals("6")) {
                        sum++;
                    }
                }
                int result = sum * 6;
                lbS2.setText(String.valueOf(result));
                btnŠestice.setDisable(true);
                Move m = new Move(String.valueOf(result), lbS2.getId(), player);
                moves.add(m);
                DOMSteps.add(new Field("lbS2", lbS2.getId(), result));
                sumAll2(result);
                gameLabels.add(new Field("lbS2", lbS2.getText(), result));
                serializeGame();
                
        }
        }
    }

    @FXML
    private void countTris(ActionEvent event) {
        if (player.getNumber() == playerNumber) {

            if (lbTris1.getText().isEmpty()) {

                List<String> allNumbers = new ArrayList<String>(); 

                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }

                String number = getStringsThatOccurNTimes(allNumbers, 3); 
                if (number == "") {
                    lbTris1.setText("0");
                    Move m = new Move("0", lbTris2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbTris1", lbTris1.getId(), 0));
                } else {
                    int result = Integer.valueOf(number) * 3;
                    lbTris1.setText(String.valueOf(result));
                    Move m = new Move(String.valueOf(result), lbTris2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbTris1", lbTris1.getId(), result));
                    sumAll(result);
                    gameLabels.add(new Field("lbTris1", lbTris1.getText(), result));
                    
                }

                btnTris.setDisable(true);             
                serializeGame();

            }
        } else {
             if (lbTris2.getText().isEmpty()) {

                List<String> allNumbers = new ArrayList<String>(); 

                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }

                String number = getStringsThatOccurNTimes(allNumbers, 3); 
                if (number == "") {
                    lbTris2.setText("0");
                    gameLabels.add(new Field("lbTris2", lbTris2.getText(), 0));
                    Move m = new Move("0", lbTris2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbTris2", lbTris2.getId(), 0));
                } else {
                    int result = Integer.valueOf(number) * 3;
                    lbTris1.setText(String.valueOf(result));
                    Move m = new Move(String.valueOf(result), lbTris2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbTris2", lbTris2.getId(), result));
                    sumAll2(result);
                    gameLabels.add(new Field("lbTris2", lbTris2.getText(), result));
                    
                }

                btnTris.setDisable(true);
                
                serializeGame();

            }
        }
    }
      
    @FXML
    private void countPoker(ActionEvent event) {
        if (player.getNumber() == playerNumber) {

            if (lbPoker1.getText().isEmpty()) {
                List<String> allNumbers = new ArrayList<String>(); 

                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }

                String number = getStringsThatOccurNTimes(allNumbers, 4); 
                if (number == "") {
                    lbPoker1.setText("0");
                     gameLabels.add(new Field("lbPoker1", lbPoker1.getText(), 0));
                    Move m = new Move("0", lbPoker2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbPoker1", lbPoker1.getId(), 0));
                } else {
                    int result = Integer.valueOf(number) * 4;
                    lbPoker1.setText(String.valueOf(result));
                    Move m = new Move(String.valueOf(result), lbPoker2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbPoker1", lbPoker1.getId(), result));
                    sumAll(result);
                     gameLabels.add(new Field("lbPoker1", lbPoker1.getText(), result));
                    
                }
                btnPoker.setDisable(true);
               
                serializeGame();

            }
        } else {
            if (lbPoker2.getText().isEmpty()) {
                List<String> allNumbers = new ArrayList<String>(); 

                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }

                String number = getStringsThatOccurNTimes(allNumbers, 4); 
                if (number == "") {
                    lbPoker2.setText("0");
                    gameLabels.add(new Field("lbPoker2", lbPoker2.getText(), 0));
                    Move m = new Move("0", lbPoker2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbPoker2", lbPoker2.getId(), 0));
                } else {
                    int result = Integer.valueOf(number) * 4;
                    lbPoker2.setText(String.valueOf(result));
                    Move m = new Move(String.valueOf(result), lbPoker2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbPoker2", lbPoker2.getId(), result));
                    sumAll2(result);
                    gameLabels.add(new Field("lbPoker2", lbPoker2.getText(), result));
                    
                }
                btnPoker.setDisable(true);
                
                serializeGame();
        }
        }
    }
    
    

    @FXML
    private void countJamb(ActionEvent event) {
        if (player.getNumber() == playerNumber) {
        
            if (lbJamb1.getText().isEmpty()) {

                List<String> allNumbers = new ArrayList<String>(); 

                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }

                String number = getStringsThatOccurNTimes(allNumbers, 5); 
                if (number == "") {
                    lbJamb1.setText("0");
                     gameLabels.add(new Field("lbJamb1", lbJamb1.getText(), 0));
                    Move m = new Move("0", lbJamb2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbJamb1", lbJamb1.getId(), 0));
                } else {
                    int result = Integer.valueOf(number) * 5;
                    lbJamb1.setText(String.valueOf(result));
                     Move m = new Move("0", lbJamb2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbJamb1", lbJamb1.getId(), result));
                    sumAll(result);
                     gameLabels.add(new Field("lbJamb1", lbJamb1.getText(), result));
                   
                }
                btnJamb.setDisable(true);
               
                serializeGame();
            }
        } else {

            if (lbJamb2.getText().isEmpty()) {

                List<String> allNumbers = new ArrayList<String>(); 

                for (Dice clickedDice : clickedDices) {
                    String imageURL = clickedDice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }
                for (Dice dice : dices) {
                    String imageURL = dice.getImageURL();
                    String substring = imageURL.substring(imageURL.lastIndexOf('.') - 1, imageURL.lastIndexOf('.')); 
                    allNumbers.add(substring);

                }

                String number = getStringsThatOccurNTimes(allNumbers, 5); 
                if (number == "") {
                    lbJamb2.setText("0");
                    gameLabels.add(new Field("lbJamb2", lbJamb2.getText(), 0));
                    Move m = new Move("0", lbJamb2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbJamb2", lbJamb2.getId(), 0));
                } else {
                    int result = Integer.valueOf(number) * 5;
                    lbJamb1.setText(String.valueOf(result));
                    Move m = new Move("0", lbJamb2.getId(), player);
                    moves.add(m);
                    DOMSteps.add(new Field("lbJamb2", lbJamb2.getId(), result));
                    sumAll2(result);
                    gameLabels.add(new Field("lbJamb2", lbJamb2.getText(), result));
                    
                }
                btnJamb.setDisable(true);
                
                serializeGame();
            }
        }
    }

        
       static String getStringsThatOccurNTimes(List<String> stringList, int n) { 
        if (stringList == null || stringList.size() == 0) {
            return null;
        }
        
        String stringThatOccurNTimes = "";
        Map<String, Integer> stringCounts = new HashMap<>();  
        for (String s : stringList) {        
            int currentStringCount = stringCounts.getOrDefault(s, 0) + 1;
            stringCounts.put(s, currentStringCount); 
            if (currentStringCount == n) { 
                stringThatOccurNTimes  = s;
            } 
        }
        return stringThatOccurNTimes;
    }

    private void initializeDices() {
        d1 = new Dice("dice1", dice1 ,URL + "1.png", false);
        d2 = new Dice("dice2", dice2, URL + "2.png", false);
        d3 = new Dice("dice3", dice3, URL + "3.png", false);
        d4 = new Dice("dice4", dice4, URL + "4.png", false);
        d5 = new Dice("dice5", dice5, URL + "5.png", false);
       dices.addAll(d1, d2, d3, d4, d5);
       
       
    }

    private int sumAll = 0;
    private void sumAll(int sum) {
        sumAll += sum;
        
        if (deserilazation) {
            int sumBefore = Integer.parseInt(lbZbroj1.getText() == "" ? "0" : lbZbroj1.getText());
            int sumAfter = sumBefore += sumAll;
            lbZbroj1.setText(String.valueOf(sumAfter));
            gameLabels.add(new Field("lbZbroj1", lbZbroj1.getText(), sumAfter));
            serializeGame();
            Move m = new Move(String.valueOf(sumAfter), lbZbroj2.getId(), player);
            moves.add(m);
            DOMSteps.add(new Field("lbZbroj1", lbZbroj1.getId(), sumAfter));
        } else {
            
            lbZbroj1.setText(String.valueOf(sumAll));
            gameLabels.add(new Field("lbZbroj1", lbZbroj1.getText(), sumAll));
            serializeGame();
            Move m = new Move(String.valueOf(sumAll), lbZbroj2.getId(), player);
            moves.add(m);
            DOMSteps.add(new Field("lbZbroj1", lbZbroj1.getId(), sumAll));
        }
        
        
        
        
    }
    
    private int sumAll2 = 0;
    private void sumAll2(int sum) {
        sumAll2 += sum;
        
        if (deserilazation) {
            int sumBefore = Integer.parseInt(lbZbroj2.getText() == "" ? "0" : lbZbroj2.getText());
            int sumAfter = sumBefore += sumAll2;
            lbZbroj2.setText(String.valueOf(sumAfter));
            gameLabels.add(new Field("lbZbroj2", lbZbroj2.getText(), sumAfter));
            serializeGame();
            Move m = new Move(String.valueOf(sumAfter), lbZbroj2.getId(), player);
            moves.add(m);
            DOMSteps.add(new Field("lbZbroj2", lbZbroj2.getId(), sumAfter));
        } else {
            lbZbroj2.setText(String.valueOf(sumAll2));
            gameLabels.add(new Field("lbZbroj2", lbZbroj2.getText(), sumAll2));
            serializeGame();
            Move m = new Move(String.valueOf(sumAll2), lbZbroj2.getId(), player);
            moves.add(m);
            DOMSteps.add(new Field("lbZbroj2", lbZbroj2.getId(), sumAll2));
        }
        

    }
    
    
    private void serializeGame() {
        game.setLabels(gameLabels);
        if (file != null) {
            try {
                SerializationUtils.write(game, file.getAbsolutePath());
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private Game deserializeGame() {
        if (file != null) {
            try {
                game = (Game) SerializationUtils.read(file.getAbsolutePath());
            } catch (IOException | ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
            return game;
        }
        return null;
    }

    @FXML
    private void continuePreviousGame(ActionEvent event) {
        
         Game deserializedGame = deserializeGame();
         deserilazation=true;
         List<Field> desLabels = deserializedGame.getLabels(); 
         
         for (Field label : allLabels) {
             for (Field desLabel : desLabels) { 
                 if (desLabel.getName().equals(label.getName())) {
                     for (Label l : allLabelss) {
                         if (l.getId().equals(label.getValue())) {
                             l.setText(desLabel.getValue());
                         }
                     }
                 }
             }
        }
    }

    private void initLabels() {
        allLabels.add(new Field("lbJ1", lbJ1.getId()));
        allLabels.add(new Field("lbD1", lbD1.getId()));
        allLabels.add(new Field("lbT1", lbT1.getId()));
        allLabels.add(new Field("lbC1", lbC1.getId()));
        allLabels.add(new Field("lbP1", lbP1.getId()));
        allLabels.add(new Field("lbS1", lbS1.getId()));
        allLabels.add(new Field("lbTris1", lbTris1.getId()));
        allLabels.add(new Field("lbPoker1", lbPoker1.getId()));
        allLabels.add(new Field("lbJamb1", lbJamb1.getId()));
        allLabels.add(new Field("lbZbroj1", lbZbroj1.getId()));
        
        allLabelss.add(lbJ1);
        allLabelss.add(lbD1);
        allLabelss.add(lbT1);
        allLabelss.add(lbC1);
        allLabelss.add(lbP1);
        allLabelss.add(lbS1);
        allLabelss.add(lbTris1);
        allLabelss.add(lbPoker1);
        allLabelss.add(lbJamb1);
        allLabelss.add(lbZbroj1);
        allLabelss.add(lbJ2);
        allLabelss.add(lbD2);
        allLabelss.add(lbT2);
        allLabelss.add(lbC2);
        allLabelss.add(lbP2);
        allLabelss.add(lbS2);
        allLabelss.add(lbTris2);
        allLabelss.add(lbPoker2);
        allLabelss.add(lbJamb2);
        allLabelss.add(lbZbroj2);
    }

    @FXML
    private void createDoc(ActionEvent event) {
        
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DOC_FILENAME))) { 

            String packageArray[] = new File(PACKAGE_LOCATION).list(); 

            StringBuilder classAndMembersInfo = new StringBuilder();
             classAndMembersInfo.append("<!DOCTYPE html>\n")
                .append("<html>\n")
                .append("<head>\n")
                .append("<title>Dokumentacija</title>\n")
                .append("</head>\n")
                .append("<body>\n");
            
            classAndMembersInfo
                            .append("<h1><b>DOKUMENTACIJA</b></h1>\n\n\n");

            for (String packageName : packageArray) {
                if (!packageName.equals(Yamb.class.getSimpleName().concat(".java"))) {

                    classAndMembersInfo
                            .append("<h1><b>")
                            .append("Naziv paketa: " + packageName)
                            .append("</h1></b>")
                            .append("\n\n\n\n");
                            

                    String classArray[] = new File(PACKAGE_LOCATION + "\\" 
                            + packageName).list();

                    for (String className : classArray) {
                       

                        if (className.substring(className.indexOf(".") + 1).equals("fxml")) {
                            classAndMembersInfo
                                .append("<h2>")
                                .append("Naziv klase: " + className)
                                .append("</h2>")
                                .append(System.lineSeparator());
                        } else if (className.substring(className.indexOf(".") + 1).equals("png")){
                            classAndMembersInfo
                                .append("<h2>")
                                .append("Naziv slike: " + className)
                                .append("</h2>")
                                .append(System.lineSeparator());
                        }
                        else {
                             classAndMembersInfo
                                .append("<h2>")
                                .append("Naziv klase: " + className)
                                .append("</h2>")
                                .append(System.lineSeparator());
                            Class<?> clazz = Class.forName(CLASSES_PACKAGE.concat(packageName).concat("." + className.substring(0, className.indexOf("."))));
                            ReflectionUtils.readClassAndMembersInfo(clazz, classAndMembersInfo);

                        }

                        classAndMembersInfo
                                .append("\n\n\n");
                                
                    }
                } 
            }

             classAndMembersInfo.append("</body>\n")
                .append("</html>\n");
             
             
            writer.write(classAndMembersInfo.toString());

             Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info");
            alert.setHeaderText("Documentation");
            alert.setContentText("Created!");
            alert.showAndWait();
        
        } catch (IOException | ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } 

    }

    public void showMove(List<Move> moves) {


            System.out.println("Šalje " + clientThread.getPlayer().getNickname() + " ovo: " + moves.get(0));
            if (!clientThread.getPlayer().getNickname().equals(moves.get(0).getPlayer().getNickname())) { 
                for (Move move : moves) {
                    System.out.println(move + "\n");
                     allLabelss.stream().filter((label) -> (move.getLabelId().equals(label.getId()))).forEachOrdered((label) -> {
                    label.setText(move.getPoints());
                    });
                
        } }else {
                System.out.println("Klijent koji je poslao paket ne zeli ga nazad");
            }
       
    }
    
    
    
    
    private MessengerService messangerService;
    private void initializeChat() {
        messangerService = MessageUtils.getMessengerService();
        updateChat();
    }

    @FXML
    private void sendMessage(ActionEvent event) {
        String message = tfChat.getText();
         if (message.length() != 0) {
             
            MessageUtils.sendMessage(player.getNickname(), tfChat.getText().trim());                              
        }        
          tfChat.setText("");

    }
    
    
     private int nbrOfMessages = 0;
      public void updateChat() {
        try {
            messangerService.clearMessages();
            nbrOfMessages = messangerService.getAllMessages().size();
            new Timer().scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    try {
                        if (messangerService.getAllMessages().size() != nbrOfMessages) { 
                            nbrOfMessages = messangerService.getAllMessages().size();
                            writeAllMessages();
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }, 0, 500);

        } catch (RemoteException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeAllMessages() throws RemoteException {
       
        
        StringBuilder sb = new StringBuilder();
        messangerService.getAllMessages().forEach(m -> { 

            m.setPlayer(m.getPlayer());
            
            sb.append(m).append("\n");
            taChat.setText(sb.toString());
                       
        });       
    }

     private void saveDOM() {
        DOMUtils.saveSteps(DOMSteps);
    }
    

    @FXML
    private void loadDOM() {
        loadedDOMSteps = DOMUtils.loadSteps();       
        btnNext.setDisable(false);
        allLabelss.forEach(l -> l.setText(""));
        
        loadedDOMSteps.forEach(s -> System.out.println(s));
    }

    
     int i = 0;
    @FXML
    private void nextStep(ActionEvent event) {
        
        if (i < loadedDOMSteps.size()) {

            Field f = loadedDOMSteps.get(i);
            String labelId = f.getValue();
            int points = f.getPoints();
            allLabelss.stream().filter((label) -> (labelId.equals(label.getId()))).forEachOrdered((label) -> {
                    label.setText(String.valueOf(points));
                    });
            
            
        }

        i++;
    }
    
        
       
        
    }

              
               
                
