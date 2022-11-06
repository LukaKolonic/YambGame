/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author lukak
 */
public class Covid extends Application {
    
    private static final int THREAD_NUMBER = 7;
    
    private StackPane stack;
    private GridPane grid;
    ImageView station;
    ImageView truck;
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        stack = new StackPane();
        grid = new GridPane();
        
        grid.widthProperty().add(300);
        grid.heightProperty().add(450);
        grid.setPadding(new Insets(200, 0, 0, 360));
        
        setStation(Configuration.IMAGE_STATION);
        
        stack.getChildren().add(grid);
  
        
        Scene scene = new Scene(stack, 1000, 400);
            
        primaryStage.setTitle("Covid19");
        primaryStage.setScene(scene);
        primaryStage.show();
        
         pokreniNiti();
        
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(500),
                        event -> {
                            osvjeziPrikazReda(grid);

                           
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    
   
    public static void main(String[] args) {
        launch(args);
    }

    private void setStation(Image image) {
        stack.getChildren().remove(station);
        station = new ImageView(image);
        station.setFitHeight(200.0);
        station.setFitWidth(300.0);
        station.setTranslateX(-320);
 
       
        stack.getChildren().add(station);
    }

    private void pokreniNiti() {
        resetirajRedCekanja();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);
        for (int i = 0; i <= THREAD_NUMBER; i++) {
            executor.execute(new NitCekanjeURedu(i));
        }
        ExecutorService executor2 = Executors.newFixedThreadPool(THREAD_NUMBER);
        for (int i = 0; i <= THREAD_NUMBER; i++) {
            executor2.execute(new NitTestiranje(i));
        }
        ExecutorService executor3 = Executors.newFixedThreadPool(THREAD_NUMBER);
        for (int i = 0; i <= THREAD_NUMBER; i++) {
            executor3.execute(new NitDolazakTestova(i));
        }
    }

    public synchronized void osvjeziPrikazReda(GridPane grid) {
        while (Configuration.isTestiranjeUTijeku == true) {
            try {
                System.out.println("Pokušaj osvježavanja reda nije uspio!");
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Covid.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        
        Configuration.isTestiranjeUTijeku = true;
        grid.getChildren().clear();
        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < Configuration.brLjudiURedu; j++) {
                
                ImageView slikaOsobe = new ImageView(Configuration.IMAGE_OSOBA);
                slikaOsobe.setTranslateX(120);
                slikaOsobe.setFitHeight(150);
                slikaOsobe.setFitWidth(65);
                grid.add(slikaOsobe, j, i);
                
                }
            }
        
        if (Configuration.isDolazak){
            setTruck(Configuration.IMAGE_DOLAZAK);
         } else {
            setTruck(null);
        }
        
        
        Configuration.isTestiranjeUTijeku = false;
        
        notifyAll();
        System.out.println("Broj ljudi u redu: " + Configuration.brLjudiURedu + " Broj mjesta u ambulanti: " + Configuration.brMjestaUAmbulanti + " Dolazak: " +Configuration.isDolazak);
    
    }

    public void resetirajRedCekanja() {

         Configuration.brLjudiURedu = 0;
         Configuration.brMjestaUAmbulanti = Configuration.KAPACITET_AMBULANTE;
         setStation(Configuration.IMAGE_STATION);
        
                               
    }

    private void setTruck(Image image) {
        if (image == null) {
            stack.getChildren().remove(truck);
        }
        else {
        stack.getChildren().remove(truck);
        truck = new ImageView(image);
        truck.setFitHeight(100.0);
        truck.setFitWidth(200.0);
        truck.setTranslateX(-100);
        
        stack.getChildren().add(truck);
        }
    }
    
}
