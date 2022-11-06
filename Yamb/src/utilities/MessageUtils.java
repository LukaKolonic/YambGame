/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;


import hr.algebra.ChatMessage;
import hr.algebra.MessengerService;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author dnlbe
 */
public class MessageUtils {
    
    public static void showInfoMessage(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
    
    public static void sendMessage(String player, String message) {

            MessengerService messangerService = getMessengerService();  
            ChatMessage newMessage = new ChatMessage(player, message, LocalDateTime.now());
        try {
            messangerService.sendMessage(newMessage);
        } catch (RemoteException ex) {
            Logger.getLogger(MessageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static MessengerService getMessengerService() {
        Registry registry;
        
        try {
            registry = LocateRegistry.getRegistry();
            MessengerService messengerService = (MessengerService) registry.lookup("MessengerService"); 
            return messengerService;

        } catch (RemoteException | NotBoundException ex) {
            Logger.getLogger(MessageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
     public static List<ChatMessage> getAllMessages() {
      
            MessengerService messengerService = getMessengerService();
        try {
            return messengerService.getAllMessages();
        } catch (RemoteException ex) {
            Logger.getLogger(MessageUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
                  
        return null;
    }
}
