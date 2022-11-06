/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.threads;

import hr.algebra.models.Move;
import hr.algebra.utilities.ByteUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 *
 * @author lukak
 */
public class ServerThread extends Thread {
    
    private static final String PORT = "PORT";
    private static final String GROUP = "GROUP";
    private static final String FILE_LOCATION = ".\\";
        
    private static String group;
    public static Integer port;
    
    static {
        
        
        Hashtable conf = new Hashtable();

        conf.put(Context.INITIAL_CONTEXT_FACTORY, 
                "com.sun.jndi.fscontext.RefFSContextFactory"); 

        conf.put(Context.PROVIDER_URL, "file:" + FILE_LOCATION); 
        
        try {
            Context context = new InitialContext(conf);

            NamingEnumeration enumeration = context.listBindings(""); 

            List<String> fileNames = new ArrayList<>();

            while (enumeration.hasMore()) {
                Binding fileItem = (Binding) enumeration.next();
                fileNames.add(fileItem.getName());
            }

            String configurationFileName = fileNames
                    .stream()
                    .filter(fileName
                            -> new File(FILE_LOCATION + fileName)
                                    .isDirectory() == false)
                    .filter(fileName -> fileName.endsWith(".properties"))
                    .findFirst()
                    .get();

            System.out.println("Configuration file name found: "
                    + configurationFileName);

            Properties appProps = new Properties(); 
            appProps.load(new FileInputStream(FILE_LOCATION
                    + configurationFileName));
            
            String portString = appProps.getProperty(PORT);
            String groupString = appProps.getProperty(GROUP);
          
            
            System.out.println("Client port: " + portString);
            System.out.println("Server group: " + groupString);
            
            
            group = groupString;
            port = Integer.parseInt(portString);
            

        } catch (NamingException | IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }
    
    @Override
    public void run() {
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            System.err.println("Server listening on port: " + serverSocket.getLocalPort());
            while (true) {

                getPacketFromClient(serverSocket);
                getPacketFromClient(serverSocket);
                getPacketFromClient(serverSocket);
               
            }

        } catch (SocketException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void getPacketFromClient(final DatagramSocket serverSocket) throws IOException {
        
        byte[] numberOfBytes = new byte[64];                                                      
        DatagramPacket packet = new DatagramPacket(numberOfBytes, numberOfBytes.length);
        serverSocket.receive(packet);
        int length = ByteUtils.byteArrayToInt(numberOfBytes);
        
        byte[] bytes = new byte[length];                                            
        packet = new DatagramPacket(bytes, bytes.length);
        serverSocket.receive(packet);
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);           
                ObjectInputStream ois = new ObjectInputStream(bais)) {
            List<Move> move = (List<Move>) ois.readObject();
           
            Platform.runLater(() -> {
                sendToClients(move, serverSocket); 
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendToClients(List<Move> move, DatagramSocket serverSocket)  {
       byte[] bytes;
      
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();         
                    ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(move);
                bytes = baos.toByteArray();

                 InetAddress groupAddress = InetAddress.getByName(group);
                
                byte[] numberOfWordBytes = ByteUtils.intToByteArray(bytes.length);     
                DatagramPacket packet = new DatagramPacket(
                        numberOfWordBytes,
                        numberOfWordBytes.length,
                        groupAddress, Integer.valueOf(port)
                );
                serverSocket.send(packet);

                packet = new DatagramPacket(                   
                        bytes,
                        bytes.length,
                        groupAddress, Integer.valueOf(port));
                serverSocket.send(packet);
                System.out.println(getName() + " sent move" + move);

    }   catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
